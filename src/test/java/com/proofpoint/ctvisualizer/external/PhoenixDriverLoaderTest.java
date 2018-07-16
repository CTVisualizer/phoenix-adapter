package com.proofpoint.ctvisualizer.external;

import com.proofpoint.ctvisualizer.PhoenixConfig;
import com.proofpoint.ctvisualizer.PhoenixHelper;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhoenixDriverLoaderTest {

    @Test @Ignore
    public void testPhoenixDriverLoader() throws Exception {
        String sql = "SELECT * FROM threat_annotation LIMIT 5";
        Path driverFile = Paths.get("phoenix-client.jar");
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(loader, driverFile.toUri().toURL());
        PhoenixHelper helper = new PhoenixHelper(new PhoenixConfig());
        Connection connection = helper.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            System.out.println(resultSet.getString("threat"));
        }
    }
}
