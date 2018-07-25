package com.proofpoint.ctvisualizer.executequery;

import com.proofpoint.ctvisualizer.PhoenixHelper;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static com.proofpoint.ctvisualizer.Utils.escapeStringValue;

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
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String execute(String query, Response response) {
        try {
            shouldStop.set(false);
            response.type("application/json");
            Logger.getLogger("QueryExecutionManager").info("Received query: " + query);
            currentStatement = connection.prepareStatement(query);
            Logger.getLogger("QueryExecutionManager").info("PreparedStatement created.");
            boolean hasResults = currentStatement.execute();
            Logger.getLogger("QueryExecutionManager").info("PreparedStatement executed.");
            if (hasResults) {
                return handleQuery(currentStatement.getResultSet(), response);
            } else {
                return handleMutation(currentStatement.getUpdateCount(), response);
            }

        } catch (SQLException | RuntimeException e) {
            for(StackTraceElement element: e.getStackTrace()) {
                Logger.getLogger("QueryExecutionManager").warning(element.toString());
            }
            response.status(550);
            return String.format("{ \"metadata\": { \"columns\": [ {\"name\": \"EXCEPTION\" , \"type\": \"VARCHAR\"}], \"multipleTablesRepresented\": true }, \"data\":[{\"EXCEPTION\":\"%s\"}]}", escapeStringValue(e.getMessage()));
        }
    }

    private String handleQuery(ResultSet resultSet, Response response) {
        try {
            currentResultSet = resultSet;
            Logger.getLogger("QueryExecutionManager").info("ResultSet created.");
            String output = converter.convert(currentResultSet);
            Logger.getLogger("QueryExecutionManager").info("ResultSet converted.");
            currentStatement.close();
            currentResultSet.close();
            Logger.getLogger("QueryExecutionManager").info("PreparedStatement and ResultSet closed.");
            response.status(200);
            return output;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String handleMutation(int updateCount, Response response) {
        try {
            Logger.getLogger("QueryExecutionManager").info(String.format("%d rows updated.", updateCount));
            currentStatement.close();
            Logger.getLogger("QueryExecutionManager").info("PreparedStatement and ResultSet closed.");
            response.status(202);
            return String.format("{ \"metadata\": { \"columns\": [ {\"name\": \"UPDATE\" , \"type\": \"VARCHAR\"}], \"multipleTablesRepresented\": true }, \"data\":[{\"UPDATE\":\"%d rows updated.\"}]}", updateCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
