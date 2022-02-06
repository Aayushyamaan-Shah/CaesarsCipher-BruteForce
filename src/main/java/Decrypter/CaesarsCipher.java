package Decrypter;

import java.util.HashMap;

import static Decrypter.DecryptUtils.getValidPercent;
import static Decrypter.DecryptUtils.insertInvalid;

public class CaesarsCipher {
    String encryptedString;
    HashMap<Integer, HashMap<Double, String>> decryptPercent;

    public CaesarsCipher(String encryptedString){
        this.encryptedString = encryptedString.toLowerCase();
        System.out.println(this.encryptedString);
        decryptPercent = new HashMap<>();
    }

    public boolean decrypt(int key){

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < encryptedString.length(); i++){
            if(encryptedString.charAt(i) < 'a' || encryptedString.charAt(i) > 'z'){
                stringBuilder.append(encryptedString.charAt(i));
                continue;
            }
            int currentChar = encryptedString.charAt(i);
            int newChar = currentChar - key;
            if((char)newChar < 'a'){
                newChar += 26;
            }
            stringBuilder.append((char)newChar);
        }
        double validPercent = getValidPercent(stringBuilder.toString());
        HashMap<Double, String> tempHashMap = new HashMap<>();
        tempHashMap.put(validPercent, stringBuilder.toString());
        decryptPercent.put(key, tempHashMap);
        if(validPercent > 60){
            System.out.println("Key "+key+" has a confidence of "+validPercent+"%");
            System.out.println("String: "+stringBuilder);
            insertInvalid(stringBuilder.toString());
        }
        return validPercent >= 60.0;
    }

    public boolean decrypt(){
        boolean keyFound = false;
        for(int i = 0; i < 26; i++){
            if(decrypt(i)){
                keyFound = true;
            }
        }
        return keyFound;
    }



}
