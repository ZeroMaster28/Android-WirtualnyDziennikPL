package com.example.e_dziennik.Backend.Database;


public class QueryBuilder {

    private QueryBuilder()
    {
        //
    }

    public static String buildSelectQuery(String tableName, String condition, String... columns)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        for(int i = 0; i<columns.length; i++) {
            query.append(columns[i]);
            if(i<columns.length-1) query.append(",");
        }
        query.append(" FROM ");
        query.append(tableName);
        if(!"".equals(condition)) {
            query.append(" WHERE ");
            query.append(condition);
        }
        return query.toString();
    }

    public static String eq(String columnName, Object value)
    {
        if(value instanceof Integer) return columnName+"="+value;
        if(value instanceof String) return columnName+"=\'"+value+"\'";
        return null;
    }

    public static String eq(String columnName, int value)
    {
        Object wrapper = value;
        return eq(columnName, wrapper);
    }
}
