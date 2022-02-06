package Decrypter;

public class DecryptMeh {
    // NOTE: To use the caesars cipher, you will need DB connection which contains english words in it
    // If you want to create your own DB, please follow the steps given in the "DB Filler.txt" file in
    // the DBFiller package
    public static void main(String[] args) {
        // To decrypt a message that is encrypted with the Caesars cipher, use the following function
        // First create the object of the CaesarsCipher with the encrypted message as the argument
        // Then call the decrypt() method on it
        // NOTE: This method will return boolean as the output
        CaesarsCipher caesarsCipher = new CaesarsCipher("Mu xqlu q iushuj cuujydw myjx jxu qbyudi ed jxu 25jx ev zqd");
        if(caesarsCipher.decrypt()){
            System.out.println("Key is found");
        }else {
            System.out.println("No suitable key is found");
        }


        // To encrypt a given text, use the following methods
        // First set the normal text using setNormalText([STRING])
        // Then set the key using the setKey([STRING]) method
        // Finally call encrypt()

        // To decrypt a given text, use the following methods
        // First set the encrypted text using setEncryptedText([STRING])
        // Then set the key using the setKey([STRING]) method
        // Finally call decrypt()
        PlayFairCipher playFairCipher = new PlayFairCipher();
        playFairCipher.setKey("charusat");
        playFairCipher.setNormalText("attack");
        playFairCipher.setEncryptedText("npemel");
        playFairCipher.decrypt();
    }
}
