package com.proofpoint.ctvisualizer.executequery;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ResultSetConverterTest {

    private ResultSet mockResultSet;
    private ResultSetMetaData mockResultSetMetaData;
    private ResultSetConverter resultSetConverter;

    @BeforeEach
    public void setup() {
        mockResultSet = mock(ResultSet.class);
        mockResultSetMetaData = mock(ResultSetMetaData.class);
        Injector injector = Guice.createInjector(new ResultSetConverterModule());
        resultSetConverter = injector.getInstance(ResultSetConverter.class);
    }

    @Test
    public void testConvertVarchar() throws SQLException {
        int varcharColumnIndex = 1;

        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        when(mockResultSet.getString(varcharColumnIndex)).thenReturn("value");

        when(mockResultSetMetaData.getColumnName(varcharColumnIndex)).thenReturn("key");
        when(mockResultSetMetaData.getColumnTypeName(varcharColumnIndex)).thenReturn("VARCHAR");

        String result = resultSetConverter.convertColumn(mockResultSet, varcharColumnIndex);

        assertEquals("\"key\": \"value\"", result);

    }

    @Test
    public void testConvertBoolean() throws SQLException {
        int booleanColumnIndex = 1;

        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        when(mockResultSet.getBoolean(booleanColumnIndex)).thenReturn(true);

        when(mockResultSetMetaData.getColumnName(booleanColumnIndex)).thenReturn("key");
        when(mockResultSetMetaData.getColumnTypeName(booleanColumnIndex)).thenReturn("BOOLEAN");

        String result = resultSetConverter.convertColumn(mockResultSet, booleanColumnIndex);

        assertEquals("\"key\": true", result);

    }

    @Test @Ignore
    public void testConvertVarcharArray() throws SQLException {
        int varcharArrayColumnIndex = 1;
//        Array mockSQLArray = new PhoenixArray(PVarchar.INSTANCE, new String[] {"cool", "beans"});
        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
//        when(mockResultSet.getArray(varcharArrayColumnIndex)).thenReturn(mockSQLArray);

        when(mockResultSetMetaData.getColumnName(varcharArrayColumnIndex)).thenReturn("key");
        when(mockResultSetMetaData.getColumnTypeName(varcharArrayColumnIndex)).thenReturn("VARCHAR ARRAY");

        String result = resultSetConverter.convertColumn(mockResultSet, varcharArrayColumnIndex);

        assertEquals("\"key\": [ \"cool\", \"beans\" ]", result);
    }

    @Test
    public void testConvertDate() throws SQLException {
        int dateColumnIndex = 1;
        Timestamp mockTimestamp = new Timestamp(117,7,22,18,11,16,0);

        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        when(mockResultSet.getTimestamp(dateColumnIndex)).thenReturn(mockTimestamp);

        when(mockResultSetMetaData.getColumnName(dateColumnIndex)).thenReturn("key");
        when(mockResultSetMetaData.getColumnTypeName(dateColumnIndex)).thenReturn("DATE");

        String result = resultSetConverter.convertColumn(mockResultSet, dateColumnIndex);

        assertEquals("\"key\": \"2017-08-22 18:11:16.0\"", result);
    }

    @Test @Ignore
    public void testConvertEntireResultSet() throws SQLException {

        final AtomicInteger numInvocations = new AtomicInteger(0);
        int rowCount = 1;
        int columnCount = 4;
//        Array mockSQLArray = new PhoenixArray(PVarchar.INSTANCE, new String[] {"cool", "beans"});
        Timestamp mockTimestamp = new Timestamp(117,7,22,18,11,16,0);

        when(mockResultSetMetaData.getColumnCount()).thenReturn(columnCount);
        when(mockResultSetMetaData.getColumnName(1)).thenReturn("description");
        when(mockResultSetMetaData.getColumnTypeName(1)).thenReturn("VARCHAR");
        when(mockResultSetMetaData.getColumnName(2)).thenReturn("event_time");
        when(mockResultSetMetaData.getColumnTypeName(2)).thenReturn("DATE");
        when(mockResultSetMetaData.getColumnName(3)).thenReturn("notable");
        when(mockResultSetMetaData.getColumnTypeName(3)).thenReturn("BOOLEAN");
        when(mockResultSetMetaData.getColumnName(4)).thenReturn("verticals");
        when(mockResultSetMetaData.getColumnTypeName(4)).thenReturn("VARCHAR ARRAY");


        when(mockResultSet.next()).thenAnswer(invocation -> numInvocations.getAndIncrement() < rowCount);
        when(mockResultSet.getString(1)).thenReturn("description value");
        when(mockResultSet.getTimestamp(2)).thenReturn(mockTimestamp);
        when(mockResultSet.getBoolean(3)).thenReturn(true);
//        when(mockResultSet.getArray(4)).thenReturn(mockSQLArray);
        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);


        String result = resultSetConverter.convert(mockResultSet);
        StringBuilder expected = new StringBuilder();
        expected.append("{ ");
        expected.append("\"metadata\": { ");
        expected.append("\"columns\": [ ");
        expected.append("{ \"name\": \"description\", \"type\": \"VARCHAR\" }, ");
        expected.append("{ \"name\": \"event_time\", \"type\": \"DATE\" }, ");
        expected.append("{ \"name\": \"notable\", \"type\": \"BOOLEAN\" }, ");
        expected.append("{ \"name\": \"verticals\", \"type\": \"VARCHAR ARRAY\" } ");
        expected.append("] ");
        expected.append("}, ");
        expected.append("\"data\": [ { ");
        expected.append("\"description\": \"description value\", ");
        expected.append("\"event_time\": \"2017-08-22 18:11:16.0\", ");
        expected.append("\"notable\": true, ");
        expected.append("\"verticals\": [ \"cool\", \"beans\" ] ");
        expected.append("} ] ");
        expected.append("}");

        assertEquals(expected.toString(), result);

    }
}
