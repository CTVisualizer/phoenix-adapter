package com.proofpoint.ctvisualizer.executequery;

import com.proofpoint.ctvisualizer.PhoenixHelper;

import javax.inject.Inject;
import javax.management.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExecutionManager {

    private PhoenixHelper phoenixHelper;
    private ResultSetConverter converter;

    @Inject
    public QueryExecutionManager(PhoenixHelper phoenixHelper, ResultSetConverter converter) {
        this.phoenixHelper = phoenixHelper;
        this.converter = converter;
    }

    public String executeQuery(String query) {
        try {
            System.out.println("Received query: " + query);
            Connection connection = phoenixHelper.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(rs);
            String output = converter.convert(rs);
            System.out.println("Converted output: " + output);
            preparedStatement.close();
            rs.close();
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
