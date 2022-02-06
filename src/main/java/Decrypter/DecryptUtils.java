package Decrypter;

import JDBCHelpers.Runners;

import java.util.Scanner;

import static JDBCHelpers.Runners.removeAnythingButLetters;

public class DecryptUtils {
    public static int getWordCount(String string){
        String[] strings = string.split(" ");
        return strings.length;
    }

    public static double getValidPercent(String toValidate){
        double toReturn = 0.0;
        for (String word:toValidate.split(" ")) {
            if(Runners.isWordCorrect(word)){
                toReturn += 1;
            }
        }
        toReturn = (toReturn / getWordCount(toValidate)) * 100 ;
        return toReturn;
    }

    public static void insertInvalid(String toInsert){
        for (String word:toInsert.split(" ")) {
            word = removeAnythingButLetters(word);
            if(!Runners.isWordCorrect(word)){
                System.out.println("\""+word+"\" was not found in the database, do you want to add it to the database? (Y/N)");
                Scanner in = new Scanner(System.in);
                String toDo = in.nextLine();
                switch (toDo.toLowerCase()){
                    case "y":
                    case "yes":
                        System.out.println("Adding \""+word+"\" to the database");
                        System.out.println(word + ((Runners.addToDBWord(word))?" was added to the DB":" was not added to the DB"));
                        break;
                    case "n":
                    case "no":
                        System.out.println("\""+word+"\" was not added to the database");
                        break;
                }
            }
        }
    }

}
