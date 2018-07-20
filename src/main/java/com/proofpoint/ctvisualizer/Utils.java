package com.proofpoint.ctvisualizer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static String escapeBacktick(String input) {
        return input.replaceAll("`", "\\'");
    }
    public static List<String> tablesRepresentedBy(ResultSet resultSet) {
        try {
            List<String> tables = new LinkedList<>();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for(int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String currentTableName = resultSetMetaData.getTableName(i);
                if(!tables.contains(currentTableName)) {
                    tables.add(currentTableName);
                }
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean columnMemberOf(ResultSet resultSet, String columnName) {
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for(int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                if (resultSetMetaData.getColumnName(i).equals(columnName)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
