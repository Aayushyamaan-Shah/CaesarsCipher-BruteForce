package JDBCHelpers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ResultSets {
    List<ResultSet> resultSets;
    List<ResultSetMetaData> resultSetsMetaData;
    List<String> sqlQueries;
    int currentResultSetIndex;
    boolean isIndexSet = false;

    public ResultSets(){
        resultSets = new ArrayList<>();
        resultSetsMetaData = new ArrayList<>();
        sqlQueries = new ArrayList<>();
        currentResultSetIndex = -1;
    }

    public int getCurrentResultSetIndex() {
        return currentResultSetIndex;
    }

    public void setCurrentResultSetIndex(@NotNull int currentResultSetIndex) {
        if(currentResultSetIndex >=0 && currentResultSetIndex < resultSets.size()){
            this.currentResultSetIndex = currentResultSetIndex;
            isIndexSet = true;
        }else System.out.println("UNCHANGED INDEX");
    }

    public boolean addScrollableResultSet(@NotNull java.sql.Connection connection, @NotNull String sqlQuery){
        ResultSet resultSet = JDBCHelpers.getScrollableResultSet(connection, sqlQuery);
        if(resultSet == null){
            return false;
        }
        ResultSetMetaData resultSetMetaData = JDBCHelpers.getResultSetMetaData(resultSet);
        if(resultSetMetaData == null){
            return false;
        }
        resultSets.add(resultSet);
        resultSetsMetaData.add(resultSetMetaData);
        sqlQueries.add(sqlQuery);
        currentResultSetIndex++;
        return true;
    }

    public boolean removeResultSet(int index){
        if(index > -1 && index < resultSets.size()){
            resultSets.remove(index);
            resultSetsMetaData.remove(index);
            sqlQueries.remove(index);
            if(!isIndexSet){
                currentResultSetIndex--;
            }
            if(resultSets.size() == 0) currentResultSetIndex = -1;
            return true;
        }
        return false;
    }

    public ResultSet getResultSet(@NotNull int index){
        if(index > 0 && index <= resultSets.size()) return resultSets.get(index);
        else return null;
    }

    public int getResultsCount(){
        return resultSets.size();
    }

    public void printCurrentResultSetData(@Nullable String message){
        System.out.println(resultSets.get(currentResultSetIndex));
        JDBCHelpers.printResultSetData(resultSets.get(currentResultSetIndex), message);
    }

    public List<Object> getResultSetDataAsList() {
        return JDBCHelpers.getResultSetDataAsList(resultSets.get(currentResultSetIndex));
    }

    public void printCurrentResultSetData(){
        printCurrentResultSetData(null);
    }

    // Will always return data of the current result set
    public String toString(){
        return JDBCHelpers.getPrintableResultSetData(resultSets.get(currentResultSetIndex));
    }

    public boolean reopen(@NotNull java.sql.Connection connection, int index){
        ResultSet resultSet = JDBCHelpers.getScrollableResultSet(connection, sqlQueries.get(index));
        if(resultSet == null){
            return false;
        }
        ResultSetMetaData resultSetMetaData = JDBCHelpers.getResultSetMetaData(resultSet);
        if(resultSetMetaData == null){
            return false;
        }
        resultSets.set(index, resultSet);
        resultSetsMetaData.set(index, resultSetMetaData);
        return true;
    }

    public boolean reopenAllResultSets(@NotNull java.sql.Connection connection){
        for(int i = 0; i < sqlQueries.size(); i++) {
            if(!reopen(connection, i))
                return false;
        }
        return true;
    }


    public int getCurrentResultSetRowCount() {
        return JDBCHelpers.getRowCount(resultSets.get(currentResultSetIndex));
    }

}
