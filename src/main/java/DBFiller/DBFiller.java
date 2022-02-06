package DBFiller;

import JDBCHelpers.Runners;

public class DBFiller {
    public static void main(String[] args) {
        try{
            Runners.addToDBFromFile("E:\\College (CSPIT)\\Sem 6\\INS\\DictionaryAttack\\BG_wordlist_and_digits_1-1_all_combinations.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
