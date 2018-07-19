package com.proofpoint.ctvisualizer.external;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proofpoint.ctvisualizer.PhoenixConfig;
import com.proofpoint.ctvisualizer.PhoenixHelper;
import com.proofpoint.ctvisualizer.cliparser.CLIParserModule;
import com.proofpoint.ctvisualizer.executequery.ResultSetConverter;
import com.proofpoint.ctvisualizer.executequery.ResultSetConverterModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoenixDriverLoaderTest {

    private PhoenixHelper helper;

    @BeforeAll
    public void initialize() throws Exception {
        Path driverFile = Paths.get("phoenix-client.jar");
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(loader, driverFile.toUri().toURL());
        String[] args = new String[] {
                "-quorum=\"m0091032.cintel.lab.ppops.net,m0091033.cintel.lab.ppops.net,m0091031.cintel.lab.ppops.net\"",
                "-port=\"2181\"",
                "-hbaseNode=\"/hbase\"",
                "-principal=\"coboyle@CINTEL-CDH.PROOFPOINT.COM\"",
                "-keytab=\"/Users/coboyle/coboyle.keytab\"",
                "-phoenixClient=\"/Users/coboyle/phoenix-adapter/lib/phoenix-client.jar\""
        };
        helper = Guice.createInjector(new ResultSetConverterModule(), new CLIParserModule(args)).getInstance(PhoenixHelper.class);
    }

    @Test
    public void testPhoenixDriverLoader() throws Exception {
        String sql = "SELECT * FROM threat_annotation LIMIT 5";

        Connection connection = helper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            System.out.format("Col: %s, Type: %s\n", metaData.getColumnName(i), metaData.getColumnTypeName(i));
        }
    }

    @Test
    public void testSQLArray() throws Exception {
        String sql = "SELECT * FROM threat_annotation LIMIT 5";

        Connection connection = helper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            System.out.println(resultSet.getArray("verticals"));
        }
    }

    @Test
    public void testDate() throws Exception {
        String sql = "SELECT * FROM threat_annotation LIMIT 5";

        Connection connection = helper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            System.out.println(resultSet.getTimestamp("EVENT_TIME"));
        }
    }

    @Test
    public void testAsync() throws Exception {
        String sql = "SELECT * FROM url_scan LIMIT 5000000";

        Connection connection = helper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);


        Runnable r1 = () -> {
            try {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("URL"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        Thread t1 = new Thread(r1);
        t1.start();

//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            System.out.println(resultSet.getTimestamp("EVENT_TIME"));
//        }
    }

    @Test
    public void testPrimaryKeys() throws SQLException {
        String sql = "SELECT * FROM threat_annotation";
        Connection connection = helper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet keys = preparedStatement.getGeneratedKeys();

        Injector injector = Guice.createInjector(new ResultSetConverterModule());
        ResultSetConverter converter = injector.getInstance(ResultSetConverter.class);

        System.out.println(converter.convert(keys));
    }
}
