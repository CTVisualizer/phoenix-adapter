package com.proofpoint.ctvisualizer;

public class PhoenixConfig {

    private String hbaseZookeeperTimeout = "60000";
    private String hbaseClientWriteBuffer = "8388608";
    private String hbaseSecurityAuthentication = "kerberos";
    private String hadoopSecurityAuthentication = "kerberos";
    private String hbaseRPCProtection = "privacy";
    private String hadoopRPCProtection = "privacy";
    private String hbaseMasterPrincipal = "hbase/_HOST@CINTEL-CDH.PROOFPOINT.COM";
    private String hbaseRegionserverPrincipal = "hbase/_HOST@CINTEL-CDH.PROOFPOINT.COM";
    private int maxConnectionPoolSize = 200;
    private long connectionTimeout = 30000;
    private String phoenixClientMaxMetadataCacheSize = "10240000";


    public String getHBaseZookeeperTimeout()
    {
        return this.hbaseZookeeperTimeout;
    }

    public void setHBaseZookeeperTimeout(String timeout)
    {
        this.hbaseZookeeperTimeout = timeout;
    }


    public String getHBaseClientWriteBuffer()
    {
        return this.hbaseClientWriteBuffer;
    }

    public void setHBaseClientWriteBuffer(String clientBuffer)
    {
        this.hbaseClientWriteBuffer = clientBuffer;
    }


    public String getHBaseSecurityAuthentication()
    {
        return this.hbaseSecurityAuthentication;
    }

    public void setHBaseSecurityAuthentication(String securityAuth)
    {
        this.hbaseSecurityAuthentication = securityAuth;
    }


    public String getHadoopSecurityAuthentication()
    {
        return this.hadoopSecurityAuthentication;
    }

    public void setHadoopSecurityAuthentication(String securityAuth)
    {
        this.hadoopSecurityAuthentication = securityAuth;
    }


    public String getHBaseRPCProtection()
    {
        return this.hbaseRPCProtection;
    }

    public void setHBaseRPCProtection(String rpcProtection)
    {
        this.hbaseRPCProtection = rpcProtection;
    }


    public String getHadoopRPCProtection()
    {
        return this.hadoopRPCProtection;
    }

    public void setHadoopRPCProtection(String rpcProtection)
    {
        this.hadoopRPCProtection = rpcProtection;
    }


    public String getHBaseMasterPrincipal()
    {
        return this.hbaseMasterPrincipal;
    }

    public void setHBaseMasterPrincipal(String principal)
    {
        this.hbaseMasterPrincipal = principal;
    }


    public String getHBaseRegionserverPrincipal()
    {
        return this.hbaseRegionserverPrincipal;
    }

    public void setHBaseRegionserverPrincipal(String principal)
    {
        this.hbaseRegionserverPrincipal = principal;
    }

    public int getMaxConnectionPoolSize()
    {
        return this.maxConnectionPoolSize;
    }

    public void setMaxConnectionPoolSize(int maxConnectionPoolSize)
    {
        this.maxConnectionPoolSize = maxConnectionPoolSize;
    }

    public long getConnectionTimeout()
    {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(long timeout)
    {
        this.connectionTimeout = timeout;
    }

    public String getPhoenixClientMaxMetaDataCacheSize()
    {
        return this.phoenixClientMaxMetadataCacheSize;
    }


    public void setPhoenixClientMaxMetaDataCacheSize(String cacheSize)
    {
        this.phoenixClientMaxMetadataCacheSize = cacheSize;
    }
}
