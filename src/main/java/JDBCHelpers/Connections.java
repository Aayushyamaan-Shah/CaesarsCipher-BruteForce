package JDBCHelpers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Connections {
    List<Connection> connections;
    int currentConnectionIndex = -1;
    boolean isCurrentIndexSet = false;

    public Connections(){
        connections = new ArrayList<>();
    }

    public boolean open(@NotNull String url, @NotNull String username, @NotNull String password, @Nullable String database) {
        Connection connection = new Connection();
        if(connection.open(url,username, password, database)){
            connections.add(connection);
            if(!isCurrentIndexSet)
                currentConnectionIndex++;
            return true;
        }
        return false;
    }

    public boolean open(@NotNull String url, @NotNull String username, @NotNull String password) {
        return open(url, username, password, null);
    }

    public boolean addScrollableResultSet(String sqlQuery){
        return connections.get(currentConnectionIndex).addScrollableResultSet(sqlQuery);
    }

    public int getCurrentConnectionIndex() {
        return currentConnectionIndex;
    }

    public void setCurrentConnectionIndex(int currentConnectionIndex) {
        if(currentConnectionIndex > -1 && currentConnectionIndex < this.connections.size()){
            this.currentConnectionIndex = currentConnectionIndex;
            isCurrentIndexSet = true;
        }else System.out.println("Index unchanged");
    }

    public void setCurrentResultSetIndex(int index){
        connections.get(currentConnectionIndex).setCurrentResultSetIndex(index);
    }

    public boolean isCurrentIndexSet() {
        return isCurrentIndexSet;
    }

    public void setCurrentIndexFlag(boolean currentIndexSet) {
        isCurrentIndexSet = currentIndexSet;
    }

    public void printCurrentResultSetData(@Nullable String message){
        connections.get(currentConnectionIndex).printCurrentResultSetData(message);
    }

    //TODO
//    public ResultSet getCurrentResultSet(){
//        connections.get(currentConnectionIndex).getCurrentResultSetData();
//    }


    public void printCurrentResultSetData() {
        printCurrentResultSetData(null);
    }

    public List<Object> getCurrentResultDataAsList() {
        return connections.get(currentConnectionIndex).getResultDataAsList();
    }

    public boolean closeCurrent(){
        return connections.get(currentConnectionIndex).close();
    }

    public List<String[]> getConnections(){
        List<String[]> toReturn = new ArrayList<>();
        for(Connection connection : connections){
            String[] strings = new String[2];
            strings[0] = connection.getID();
            strings[1] = connection.getStatus();
            toReturn.add(strings);
        }
        return toReturn;
    }

    public boolean closeAll(){
        for (Connection connection : connections) {
            if (connection.getStatus().equals("OPEN") && !connection.close()) {
                return false;
            }
        }
        return true;
    }

    public Connection currentConnection(){
        return connections.get(currentConnectionIndex);
    }

    public Connection connection(int index){
        if(index > -1 && index < connections.size())
            return connections.get(currentConnectionIndex);
        return null;
    }

    public boolean reopen(int index){
        return connections.get(index).reopen();
    }

    public boolean reopenAll(){
        for(Connection connection : connections){
            if(connection.getStatus().equals( "CLOSE") && !connection.reopen())
                return false;
        }
        return true;
    }

    public boolean executeDDL(String sqlQuery) {
        return connections.get(currentConnectionIndex).executeDDL(sqlQuery);
    }

    public int executeDML(String sqlQuery) {
        return connections.get(currentConnectionIndex).executeDML(sqlQuery);
    }

    public boolean executeResult(String sqlQuery){
        return connections.get(currentConnectionIndex).executeResult(sqlQuery);
    }



}
