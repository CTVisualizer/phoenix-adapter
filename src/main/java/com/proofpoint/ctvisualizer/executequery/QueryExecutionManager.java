package com.proofpoint.ctvisualizer.executequery;

import com.proofpoint.ctvisualizer.PhoenixHelper;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class QueryExecutionManager {

    private PhoenixHelper phoenixHelper;
    private ResultSetConverter converter;
    private Connection connection;
    private PreparedStatement currentStatement;

    @Inject
    public QueryExecutionManager(PhoenixHelper phoenixHelper, ResultSetConverter converter) {
        this.phoenixHelper = phoenixHelper;
        this.converter = converter;
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            this.connection = phoenixHelper.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String executeQuery(String query) {
        try {
            Logger.getGlobal().info("Received query: " + query);
            currentStatement = connection.prepareStatement(query);
            ResultSet rs = currentStatement.executeQuery();
            String output = converter.convert(rs);
            currentStatement.close();
            rs.close();
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String stop() {
        try {
            currentStatement.close();
            return "Stopped query.";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String health() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT NOW()");
            ResultSet rs = statement.executeQuery();
            rs.close();
            statement.close();
            return "Healthy!";
        } catch (SQLException e) {
            return "Not Healthy.";
        }


    }
}
