package com.proofpoint.ctvisualizer.executequery.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.proofpoint.ctvisualizer.Utils.tablesRepresentedBy;
import static com.proofpoint.ctvisualizer.Utils.columnMemberOf;

public class PrimaryKeyMetadataGenerationStep implements MetadataGenerationStep {
    @Override
    public String generateMetadata(ResultSet resultSet) {
        String tableName = tablesRepresentedBy(resultSet).get(0);
        StringBuilder builder = new StringBuilder();
        builder.append("\"primaryKeys\": [");
        primaryKeysFor(tableName, resultSet)
                .stream()
                .filter(primaryKey -> columnMemberOf(resultSet, primaryKey))
                .forEach(representedPrimaryKey -> builder.append(String.format("{ \"name\": \"%s\" }, ", representedPrimaryKey)));
        builder.delete(builder.length()-2, builder.length()-1);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean shouldGenerateMetadata(ResultSet resultSet) {
        return tablesRepresentedBy(resultSet).size() == 1;
    }

    private List<String> primaryKeysFor(String tableName, ResultSet resultSet) {
        try {
            List<String> primaryKeys = new LinkedList<>();
            Connection connection = resultSet.getStatement().getConnection();
            ResultSet primaryKeysResultSet = connection.getMetaData().getPrimaryKeys(null, null, tableName);
            while(primaryKeysResultSet.next()) {
                primaryKeys.add(primaryKeysResultSet.getString("COLUMN_NAME"));
            }
            return primaryKeys;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
