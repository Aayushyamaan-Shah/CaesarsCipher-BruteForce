package Decrypter;

import com.sun.istack.internal.Nullable;

public class PlayFairCipher {
    private String normalText;
    private StringBuilder encryptedText;
    private String key;
    private String[][] keyMatrix;

    public PlayFairCipher(@Nullable String normalText, @Nullable String key){
        this.normalText = (normalText == null)? "" : normalText.toLowerCase() ;
        this.key = (key == null)? "":key;
        this.keyMatrix = new String[5][5];
        encryptedText = new StringBuilder();
    }

    public PlayFairCipher(String normalText){
        new PlayFairCipher(normalText, null);
    }

    public PlayFairCipher(){
        new PlayFairCipher(null, null);
    }

    public void setKey(String key){
        this.key = key.toLowerCase();
    }

    public void setNormalText(String normalText){
        this.normalText = normalText.toLowerCase();
    }
    public void setEncryptedText(String encryptedText){
        this.encryptedText = new StringBuilder(encryptedText);
    }

    public void decrypt(){
        String[] choppedEncryptedText = getEncryptedChoppedStrings();
        generateKeyMatrix();
        System.out.println("Encrypted Text: "+encryptedText);
        normalText = "";
        for(String choppedStr: choppedEncryptedText) {
            normalText = normalText + getDecryptedText(choppedStr);
        }
        System.out.println("Decrypted Text: "+normalText);
    }

    public void encrypt(){
        generateKeyMatrix();
        System.out.println("Normal Text: "+normalText);
        String[] choppedNormalText = getChoppedStrings();
        encryptedText = new StringBuilder();
        for (String choppedStr: choppedNormalText) {
            encryptedText.append(getEncryptedText(choppedStr));
        }
        System.out.println("Encrypted Text: "+encryptedText.toString());
    }

    private String[] getEncryptedChoppedStrings(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(encryptedText);
        String[] strings = new String[stringBuilder.length() / 2];
        for(int i = 0; i < stringBuilder.length(); i+=2){
            strings[i/2] = stringBuilder.substring(i,i+2);
        }
        return strings;
    }

    private String getDecryptedText(String choppedStr){
        int[] pos1;
        int[] pos2;
        pos1 = getPosInKeyMatrix(choppedStr.charAt(0));
        pos2 = getPosInKeyMatrix(choppedStr.charAt(1));
        if(pos1[0] == pos2[0]){
            return keyMatrix[pos1[0]]
                    [((pos1[1]-1) < 0 )? pos1[1]+4: pos1[1]-1]+""
                    + keyMatrix[pos2[0]]
                    [((pos2[1]-1) < 0 )? pos2[1]+4: pos2[1]-1];
        }else if(pos1[1] == pos2[1]){
            return keyMatrix[((pos1[0]-1) < 0 )? pos1[0]+4: pos1[0]-1]
                    [pos1[1]]+""
                    + keyMatrix[((pos2[0]-1) < 0 )? pos2[0]+4: pos2[0]-1]
                    [pos2[1]];
        }else{
            return keyMatrix[pos1[0]][pos2[1]]+""+ keyMatrix[pos2[0]][pos1[1]];
        }
    }

    private String getEncryptedText(String choppedStr) {
        int[] pos1;
        int[] pos2;
        pos1 = getPosInKeyMatrix(choppedStr.charAt(0));
        pos2 = getPosInKeyMatrix(choppedStr.charAt(1));
        if(pos1[0] == pos2[0]){
            return keyMatrix[pos1[0]][(pos1[1]+1)%5]+""+ keyMatrix[pos2[0]][(pos2[1]+1)%5];
        }else if(pos1[1] == pos2[1]){
            return keyMatrix[(pos1[0]+1)%5][pos1[1]]+""+ keyMatrix[(pos2[0]+1)%5][pos2[1]];
        }else{
            return keyMatrix[pos1[0]][pos2[1]]+""+ keyMatrix[pos2[0]][pos1[1]];
        }
    }
    private int[] getPosInKeyMatrix(char ch){
        int[] pos = new int[2];
        for(int i =0; i < 5;i++){
            for(int j =0; j < 5; j++){
                if(keyMatrix[i][j].charAt(0) == ch){
                    pos[0] = i;
                    pos[1] = j;
                    break;
                }
            }
        }
        return pos;
    }

    public void generateKeyMatrix(){
        if(key.length() < 1)
            throw new IllegalArgumentException("The key must be set before generating key matrix can be generated.");
        setKeyMatrix(getKeyChars());
    }

    public void printKeyMatrix(){
        if(keyMatrix[0][0] == null || keyMatrix[0][0].isEmpty())
            throw new IllegalStateException("You need to call generateKeyMatrix() before you can call printKeyMatrix().");
        System.out.println("======KEY MATRIX======");
        for(String[] strArr : keyMatrix){
            for(String str:strArr){
                System.out.print(str+" ");
            }
            System.out.println();
        }
        System.out.println("======================");
    }

    private String[] getChoppedStrings(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(normalText);
        for(int i = 0; i < stringBuilder.length()-1; i+=2){
            if(stringBuilder.charAt(i)==stringBuilder.charAt(i+1))
                stringBuilder.insert(i+1, "x");
        }
        if(stringBuilder.length() %2 != 0)
            stringBuilder.append("x");
        String[] strings = new String[stringBuilder.length() / 2];
        for(int i = 0; i < stringBuilder.length(); i+=2){
            strings[i/2] = stringBuilder.substring(i,i+2);
        }
        return strings;
    }

    private String getKeyChars(){
        StringBuilder keyToReturn = new StringBuilder();

        for(int i = 0; i < key.length(); i++) {
            if(key.charAt(i) == 'j'){
                if (!keyToReturn.toString().contains("i")) {
                    keyToReturn.append("i");
                }
            }else{
                if (!keyToReturn.toString().contains(key.charAt(i) + "")) {
                    keyToReturn.append(key.charAt(i));
                }
            }
        }

        for(char a2z = 'a'; a2z <= 'z'; a2z += 1 ){
            if(a2z == 'j'){
                continue;
            }
            if(!keyToReturn.toString().contains(a2z+"")){
                keyToReturn.append(a2z);
            }
        }
        return keyToReturn.toString();
    }

    private void setKeyMatrix(String keyChars){
        this.keyMatrix = new String[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                keyMatrix[i][j] = keyChars.charAt((5*i)+j)+"";
            }
        }
    }

}
