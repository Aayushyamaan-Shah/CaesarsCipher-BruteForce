package JDBCHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runners {
    static private final String dbUrl = "localhost:5432";
    static private final String dbName = "INSWords";
    static private final String dbUser = "ins_api";
    static private final String dbPassword = "InsPassword";

    public static String removeAnythingButLetters(String toRemoveFrom){
        StringBuilder stringBuilder = new StringBuilder();
        toRemoveFrom = toRemoveFrom.toLowerCase();
        for(int i = 0; i < toRemoveFrom.length(); i++){
            if(toRemoveFrom.charAt(i) < 'a' || toRemoveFrom.charAt(i) > 'z')
                continue;
            stringBuilder.append(toRemoveFrom.charAt(i));
        }
        return stringBuilder.toString();
    }

    public static void addToDBFromFile(String filePath) throws FileNotFoundException {
        Connections connections = new Connections();
        connections.open(dbUrl + "/"+dbName, dbUser,dbPassword);

        Scanner in = new Scanner(new File(filePath));
        while(in.hasNext()){
            String toInsert = in.nextLine().replace("'","\\'");
            try{
                connections.executeDML("insert into public.\"generalWords\"(words) values('"+toInsert+"')");
            }catch (Exception e){
                System.out.println(toInsert + "was not inserted");
            }
        }
    }

    public static boolean isWordCorrect(String word) {
        Connections connections = new Connections();
        connections.open(dbUrl + "/"+dbName, dbUser,dbPassword);
        word = word.replace("_", " ").replace("*", "").replace("'", "").replace(",","");
        connections.executeResult("select count(*) from public.\"generalWords\" where words like('"+word+"')");
        connections.closeAll();
        int isValid = Integer.parseInt(connections.getCurrentResultDataAsList().get(0).toString());
        return isValid > 0;
    }

    public static boolean addToDBWord(String word) {
        Connections connections = new Connections();
        connections.open(dbUrl + "/"+dbName, dbUser,dbPassword);
        try{
            connections.executeDML("insert into public.\"generalWords\"(words) values('"+word+"')");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
