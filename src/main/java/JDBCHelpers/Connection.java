package JDBCHelpers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.sql.CallableStatement;
import java.sql.Savepoint;
import java.util.List;

public class Connection {
    private java.sql.Connection connection;
    private String url, username, password, database;
    Savepoints savepoints;
    ResultSets resultSets;

    public Connection(){
        resultSets = new ResultSets();
    }

    public boolean open(@NotNull String url, @NotNull String username, @NotNull String password, @Nullable String database){
        connection = JDBCHelpers.getConnection(url, username, password, database);
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
        if(connection != null){
            resultSets.reopenAllResultSets(connection);
            return true;
        }
        else return false;
    }

    public boolean reopen(){
        return open(url, username, password, database);
    }

    public String getID(){
        if(database != null) return url+"@"+database;
        else return url.replace('/','@');
    }

    public String getStatus(){
        if(connection == null) return "CLOSE";
        return "OPEN";
    }

    public boolean addScrollableResultSet(String sqlQuery){
        return resultSets.addScrollableResultSet(connection, sqlQuery);
    }

    public boolean removeResultSet(int index){
        return resultSets.removeResultSet(index);
    }

    public void printCurrentResultSetData(@Nullable String message){
        resultSets.printCurrentResultSetData(message);
    }
    public List<Object> getResultDataAsList() {
        return resultSets.getResultSetDataAsList();
    }
    // TODO
    /*
    public ResultSet getCurrentResultSetData(){
        resultSets.getResultSet();
    }*/

    public void printCurrentResultSetData() {
        printCurrentResultSetData(null);
    }

    public boolean close(){
        boolean toReturn = JDBCHelpers.closeConnection(connection);
        if(toReturn) connection = null;
        return toReturn;
    }
    public void setCurrentResultSetIndex(int index){
        resultSets.setCurrentResultSetIndex(index);
    }

    public boolean executeDDL(String sqlQuery) {
        return JDBCHelpers.executeDDL(connection, sqlQuery);
    }

    public int executeDML(String sqlQuery) {
        return JDBCHelpers.executeDML(connection, sqlQuery);
    }

    public boolean executeResult(String sqlQuery){
        return addScrollableResultSet(sqlQuery);
    }

    public CallableStatement prepareCall(String sqlQuery){
        return JDBCHelpers.prepareCall(connection, sqlQuery);
    }

    public void setAutoCommit(boolean b) {
        JDBCHelpers.setAutoCommit(connection, b);
    }

    public void commit() {
        JDBCHelpers.commit(connection);
    }

    //TODO Add savepoint wrappers
    public void rollbackToLastSavepoint(){
        JDBCHelpers.rollBack(connection);
    }

    public Savepoint setSavePoint(String s) {
        return JDBCHelpers.setSavePoint(connection, s);
    }

    public int getRowCount() {
        return resultSets.getCurrentResultSetRowCount();
    }


}
