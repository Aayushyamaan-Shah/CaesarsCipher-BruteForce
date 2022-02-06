package JDBCHelpers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCHelpers {

    public static Connection getConnection(@NotNull String url, @NotNull String username, @NotNull String password, @Nullable String database) {
        try{
            Class.forName("org.postgresql.Driver");
            //TODO Add support for other drivers by changing the static jdbc:mysql: string to some object
            if(database != null) return DriverManager.getConnection("jdbc:postgresql://"+url+"/"+database, username, password);
            else return DriverManager.getConnection("jdbc:postgresql://"+url, username, password);
        }catch (SQLException e){
            System.out.println("ERROR: Cannot create connection object.");
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            System.out.println("ERROR: JDBC Class was not found.");
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection(@NotNull String url, @NotNull String username, @NotNull String password) {
        return getConnection(url, username, password, null);
    }

    public static ResultSet getScrollableResultSet(@NotNull Connection connection, @NotNull String sqlQuery){
        try{
            return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
                    .executeQuery(sqlQuery);
        }catch (SQLException e){
            System.out.println("ERROR: Cannot create scrollable result set.");
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSetMetaData getResultSetMetaData(@NotNull ResultSet resultSet){
        try {
            return resultSet.getMetaData();
        }catch (SQLException e){
            System.out.println("ResultSet meta data cannot be fetched.");
            e.printStackTrace();
        }
        return null;
    }

    public static int getRowCount(@NotNull ResultSet resultSet){
        int toReturn = -1;
        try{
            int currentPosition = resultSet.getRow();
            resultSet.last();
            toReturn = resultSet.getRow();
            resultSet.absolute(currentPosition);
        }catch (SQLException e){
            System.out.println("The result set either forward only or some other error occurred.");
            e.printStackTrace();
        }
        return toReturn;
    }

    public static void printResultSetData(@NotNull ResultSet resultSet, String message){
        System.out.println(getPrintableResultSetData(resultSet, message));
    }

    public static void printResultSetData(ResultSet resultSet) {
        printResultSetData(resultSet, null);
    }

    public static String getPrintableResultSetData(@NotNull ResultSet resultSet, @Nullable String message){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if(columnCount > 0){
                if(message != null) stringBuilder.append("\n").append(message);
                stringBuilder.append("\nRow No.\t");
                for(int i = 1; i <= columnCount; i++){
                    stringBuilder.append(metaData.getColumnLabel(i)).append("\t");
                }
                stringBuilder.append("\n");
                int rowCounter = 0;
                while(resultSet.next()){
                    stringBuilder.append(++rowCounter).append("\t");
                    for(int i = 1; i <= columnCount; i++){
                        stringBuilder.append(resultSet.getString(i)).append("\t");
                    }
                    stringBuilder.append("\n");
                }
                stringBuilder.append("Total rows: ").append(getRowCount(resultSet)).append("\n");
                stringBuilder.append("Total columns: ").append(columnCount).append("\n");
            }
            else stringBuilder.append("\nThere are no rows to print.\n");
            return stringBuilder.toString();
        }catch (SQLException e){
            System.out.println("\nSome error occurred while printing.\n");
            e.printStackTrace();
        }
        return "";
    }

    public static String getPrintableResultSetData(@NotNull ResultSet resultSet){
        return getPrintableResultSetData(resultSet, null);
    }

    public static boolean closeConnection(@NotNull Connection connection) {
        try{
            connection.close();
            return true;
        }catch (SQLException e){
            System.out.println("The connection was already closed or some other error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean executeDDL(Connection connection, String sqlQuery) {
        try{
            return connection.createStatement().execute(sqlQuery);
        }catch (SQLException e){
            System.out.println("Cannot execute the given query.\n"+sqlQuery+"\n");
            e.printStackTrace();
        }
        return false;
    }

    public static int executeDML(Connection connection, String sqlQuery) {
        try{
            return connection.createStatement().executeUpdate(sqlQuery);
        }catch (SQLException e){
            System.out.println("Cannot execute the given query.\n"+sqlQuery+"\n");
            e.printStackTrace();
        }
        return -1;
    }

    public static CallableStatement prepareCall(Connection connection, String sqlQuery){
        try{
            return connection.prepareCall(sqlQuery);
        }catch (SQLException e){
            System.out.println("Cannot create a callable statement");
            e.printStackTrace();
        }
        return null;
    }

    public static void setAutoCommit(Connection connection, boolean b){
        try {
            connection.setAutoCommit(b);
        }catch (SQLException e){
            System.out.println("Connection commit type cannot be altered.");
            e.printStackTrace();
        }
    }

    public static void commit(Connection connection) {
        try {
            connection.commit();
        }catch (SQLException e){
            System.out.println("Connection commit type cannot be altered.");
            e.printStackTrace();
        }
    }

    public static Savepoint setSavePoint(Connection connection, @Nullable String s) {
        try {
            if(s != null){
                return connection.setSavepoint(s);
            }
            else connection.setSavepoint();
        }catch (SQLException e){
            System.out.println("Connection commit type cannot be altered.");
            e.printStackTrace();
        }
        return null;
    }
    public static Savepoint setSavePoint(Connection connection) {
        return setSavePoint(connection, null);
    }

    public static void rollBack(Connection connection, @Nullable Savepoint savepoint) {
        try{
            if(savepoint == null)
                connection.rollback();
            else connection.rollback(savepoint);
        }catch (SQLException e){
            System.out.println("Rollback failed.");
            e.printStackTrace();
        }
    }

    public static void rollBack(Connection connection) {
        rollBack(connection, null);
    }

    public static List<Object> getResultSetDataAsList(ResultSet resultSet) {
        try{
            List<Object> objectList = new ArrayList<>();
            resultSet.beforeFirst();
            int columnCount = resultSet.getMetaData().getColumnCount();
            while(resultSet.next()){
                for(int i = 1; i <=columnCount; i++){
                    objectList.add(resultSet.getObject(i));
                }
            }
            return objectList;
        }catch (SQLException e){
            System.out.println("Some error occurred");
            e.printStackTrace();
        }
        return null;
    }
}
