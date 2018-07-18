package com.proofpoint.ctvisualizer;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import javax.inject.Named;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class PhoenixHelper
{

    private String connectionString = "";
    private Properties props = new Properties();
    private HikariDataSource ds;
    private PhoenixConfig config;

    @Inject
    public PhoenixHelper(PhoenixConfig phoenixConfig, @Named("params") Map<String, String> params) {
        this.config = phoenixConfig;

        this.props.setProperty("hbase.client.write.buffer", phoenixConfig.getHBaseClientWriteBuffer());
        this.props.setProperty("zookeeper.session.timeout", phoenixConfig.getHBaseZookeeperTimeout());
        this.props.setProperty("hbase.security.authentication", phoenixConfig.getHBaseSecurityAuthentication());
        this.props.setProperty("hadoop.security.authentication", phoenixConfig.getHadoopSecurityAuthentication());
        this.props.setProperty("hbase.rpc.protection", phoenixConfig.getHadoopRPCProtection());
        this.props.setProperty("hadoop.rpc.protection", phoenixConfig.getHadoopRPCProtection());
        this.props.setProperty("hbase.master.kerberos.principal", phoenixConfig.getHBaseMasterPrincipal());
        this.props.setProperty("hbase.regionserver.kerberos.principal", phoenixConfig.getHBaseRegionserverPrincipal());
        this.props.setProperty("phoenix.client.maxMetaDataCacheSize",
                phoenixConfig.getPhoenixClientMaxMetaDataCacheSize());

        String zkQuorum = params.get("quorum");
        String zkPort = params.get("port");
        String hbaseNode = params.get("hbaseNode");
        String principalUser = params.get("principal");
        String keytabFile = params.get("keytab");
        int maxConnectionPoolSize = phoenixConfig.getMaxConnectionPoolSize();
        long connectionTimeout = phoenixConfig.getConnectionTimeout();

        this.connectionString = String.format("jdbc:phoenix:%s:%s:%s:%s:%s", zkQuorum, zkPort, hbaseNode, principalUser, keytabFile);

        if (phoenixConfig.getPoolConnections()) {
            HikariConfig config = new HikariConfig();
            config.setDataSourceProperties(props);
            config.setJdbcUrl(connectionString);
            config.setMinimumIdle(5);
            config.setMaximumPoolSize(maxConnectionPoolSize);
            config.setConnectionTimeout(connectionTimeout);
            config.setConnectionTestQuery("select 1");
            ds = new HikariDataSource(config);
        }
    }

    /**
     * From: https://phoenix.apache.org/faq.html#Should_I_pool_Phoenix_JDBC_Connections
     * Phoenix Connection objects are different from most other JDBC Connections due to the underlying HBase connection.
     * The Phoenix Connection object is designed to be a thin object that is inexpensive to create.
     * If Phoenix Connections are reused, it is possible that the underlying HBase connection is not always left in a
     * healthy state by the previous user.
     * It is better to create new Phoenix Connections to ensure that you avoid any potential issues.
     * You can use Java try with resources to for any closables, that way java ensures the connection is closed without
     * having to specify a finally clause.
     *
     * Strike all that non-sense about lightweight connections.  This things a liar.  The connection may be light, but the
     * connections require querying a limited resourced table that causes contention issues.  Moving to hikari.
     *
     * @return Connection
     */
    public Connection getConnection()
            throws SQLException
    {
        if (config.getPoolConnections()) {
            return ds.getConnection();
        } else {
            return DriverManager.getConnection(this.connectionString, props);

        }
    }
}