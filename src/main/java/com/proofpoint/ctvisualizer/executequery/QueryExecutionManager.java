package com.proofpoint.ctvisualizer.executequery;

import com.proofpoint.ctvisualizer.PhoenixHelper;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static com.proofpoint.ctvisualizer.Utils.escapeQuotes;

public class QueryExecutionManager {

    private PhoenixHelper phoenixHelper;
    private ResultSetConverter converter;
    private AtomicBoolean shouldStop;
    private Connection connection;
    private PreparedStatement currentStatement;
    private ResultSet currentResultSet;

    @Inject
    public QueryExecutionManager(PhoenixHelper phoenixHelper, ResultSetConverter converter, @Named("should-stop-flag") AtomicBoolean shouldStop) {
        this.phoenixHelper = phoenixHelper;
        this.converter = converter;
        this.shouldStop = shouldStop;
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
            shouldStop.set(false);
            Logger.getLogger("QueryExecutionManager").info("Received query: " + query);
            currentStatement = connection.prepareStatement(query);
            Logger.getLogger("QueryExecutionManager").info("PreparedStatement created.");
            currentResultSet = currentStatement.executeQuery();
            Logger.getLogger("QueryExecutionManager").info("ResultSet created.");
            String output = converter.convert(currentResultSet);
            Logger.getLogger("QueryExecutionManager").info("ResultSet converted.");
            currentStatement.close();
            currentResultSet.close();
            Logger.getLogger("QueryExecutionManager").info("PreparedStatement and ResultSet closed.");
            return output;
        } catch (SQLException | RuntimeException e) {
            Logger.getLogger("QueryExecutionManager").warning(e.getMessage());
            return String.format("{ \"metadata\": { \"columns\": [ {\"name\": \"EXCEPTION\" , \"type\": \"VARCHAR\"}]}, \"data\":[{\"EXCEPTION\":\"%s\"}]}", escapeQuotes(e.getMessage()));
        }
    }

    public String stop() {
        try {
            shouldStop.set(true);
            currentStatement.close();
            currentResultSet.close();
            return "Stopped query.\n";
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
