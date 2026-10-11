/**
 * This is a utility class that encrypts and decrypts a phrase using three
 * different approaches. 
 * 
 * The first approach is called the Vigenere Cipher.Vigenere encryption 
 * is a method of encrypting alphabetic text based on the letters of a keyword.
 * 
 * The second approach is Playfair Cipher. It encrypts two letters (a digraph) 
 * at a time instead of just one.
 * 
 * The third approach is Caesar Cipher. It is a simple replacement cypher. 
 * 
 * @author Huseyin Aygun
 * @version 8/3/2025
 */

public class CryptoManager { 

    private static final char LOWER_RANGE = ' ';
    private static final char UPPER_RANGE = '_';
    private static final int RANGE = UPPER_RANGE - LOWER_RANGE + 1;
    // Use 64-character matrix (8X8) for Playfair cipher  
    private static final String ALPHABET64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !\"#$%&'()*+,-./:;<=>?@[\\]^_";

    public static boolean isStringInBounds(String plainText) {
        for (int i = 0; i < plainText.length(); i++) {
            if (!(plainText.charAt(i) >= LOWER_RANGE && plainText.charAt(i) <= UPPER_RANGE)) {
                return false;
            }
        }
        return true;
    }

	/**
	 * Vigenere Cipher is a method of encrypting alphabetic text 
	 * based on the letters of a keyword. It works as below:
	 * 		Choose a keyword (e.g., KEY).
	 * 		Repeat the keyword to match the length of the plaintext.
	 * 		Each letter in the plaintext is shifted by the position of the 
	 * 		corresponding letter in the keyword (A = 0, B = 1, ..., Z = 25).
	 */   

    public static String vigenereEncryption(String plainText, String key) {
        if(!isStringInBounds(plainText) || !isStringInBounds(key)) {
        	return "The selected string is not in bounds. Try again.";
        }
        
        String finalEncrypt="";
        
        for(int i=0; i<plainText.length(); i++) {
        	char currentPlainChar=plainText.charAt(i);
        	char currentKeyChar=key.charAt(i%key.length());
        	
        	int plainIndex=ALPHABET64.indexOf(currentPlainChar);
        	int keyIndex=ALPHABET64.indexOf(currentKeyChar);
        	
        	int encryptIndex=(plainIndex+keyIndex)%64;
        	
        	finalEncrypt+=ALPHABET64.charAt(encryptIndex);
        }
        
        return finalEncrypt;
    }

    // Vigenere Decryption
    public static String vigenereDecryption(String encryptedText, String key) {
        if(!isStringInBounds(encryptedText) || !isStringInBounds(key)) {
        	return "The selected string is not in bounds. Try again.";
        }
        
        String plainText="";
        
        for(int i=0; i<encryptedText.length(); i++) {
        	char currentEncryptChar=encryptedText.charAt(i);
        	char currentKeyChar=key.charAt(i%key.length());
        	
        	int encryptIndex=ALPHABET64.indexOf(currentEncryptChar);
        	int keyIndex=ALPHABET64.indexOf(currentKeyChar);
        	
        	int plainIndex=(encryptIndex-keyIndex+64)%64;
        	
        	plainText+=ALPHABET64.charAt(plainIndex);
        }
        
        return plainText;
            
    }


	/**
	 * Playfair Cipher encrypts two letters at a time instead of just one.
	 * It works as follows:
	 * A matrix (8X8 in our case) is built using a keyword
	 * Plaintext is split into letter pairs (e.g., ME ET YO UR).
	 * Encryption rules depend on the positions of the letters in the matrix:
	 *     Same row: replace each letter with the one to its right.
	 *     Same column: replace each with the one below.
	 *     Rectangle: replace each letter with the one in its own row but in the column of the other letter in the pair.
	 */    

    public static String playfairEncryption(String plainText, String key) {
        if(!isStringInBounds(plainText) || !isStringInBounds(key)) {
            return "The selected string is not in bounds, Try again.";
        }
        
        char[][] charMatrix = new char[8][8];
        String usedChars = "";
        String combinedKey = key + ALPHABET64;
        int row = 0, col = 0;
        
        for(int i = 0; i < combinedKey.length(); i++) {
            char currentChar = combinedKey.charAt(i);
            if(usedChars.indexOf(currentChar) == -1) {
                usedChars += currentChar;
                charMatrix[row][col] = currentChar;
                col++;
                if(col == 8) {
                    col = 0;
                    row++;
                }
            }
        }
        
        String finalEncrypt = "";
        
        // Only process complete pairs
        int limit = plainText.length() - (plainText.length() % 2);
        
        for(int i = 0; i < limit; i += 2) {
            char c1 = plainText.charAt(i);
            char c2 = plainText.charAt(i+1);
            
            int row1 = -1, col1 = -1, row2 = -1, col2 = -1;
            for(int r = 0; r < 8; r++) {
                for(int c = 0; c < 8; c++) {
                    if(charMatrix[r][c] == c1) { 
                    	row1 = r; col1 = c; 
                    }
                    
                    if(charMatrix[r][c] == c2) {
                    	row2 = r; col2 = c;
                    }
                }
            }
            
            if(row1 == row2) {
                int newCol1 = (col1 + 1) % 8;
                int newCol2 = (col2 + 1) % 8;
                finalEncrypt += charMatrix[row1][newCol1];
                finalEncrypt += charMatrix[row2][newCol2];
            } 
            else if(col1 == col2) {
                int newRow1 = (row1 + 1) % 8;
                int newRow2 = (row2 + 1) % 8;
                finalEncrypt += charMatrix[newRow1][col1];
                finalEncrypt += charMatrix[newRow2][col2];              
            } 
            else {
                finalEncrypt += charMatrix[row1][col2];
                finalEncrypt += charMatrix[row2][col1];
            }
        }
        
        // Append the last odd character exactly as it is
        if(plainText.length() % 2 != 0) {
            finalEncrypt += plainText.charAt(plainText.length() - 1);
        }
        
        return finalEncrypt;
    }

    public static String playfairDecryption(String encryptedText, String key) {
        if(!isStringInBounds(encryptedText) || !isStringInBounds(key)) {
            return "The selected string is not in bounds, Try again.";
        }
        
        char[][] charMatrix = new char[8][8];
        String usedChars = "";
        String combinedKey = key + ALPHABET64;
        int row = 0, col = 0;
        
        for(int i = 0; i < combinedKey.length(); i++) {
            char currentChar = combinedKey.charAt(i);
            if(usedChars.indexOf(currentChar) == -1) {
                usedChars += currentChar;
                charMatrix[row][col] = currentChar;
                col++;
                if(col == 8) {
                    col = 0;
                    row++;
                }
            }
        }
        
        String plainText = "";
        int limit = encryptedText.length() - (encryptedText.length() % 2);
        
        for(int i = 0; i < limit; i += 2) {
            char c1 = encryptedText.charAt(i);
            char c2 = encryptedText.charAt(i+1);
            
            int row1 = -1, col1 = -1, row2 = -1, col2 = -1;
            for(int r = 0; r < 8; r++) {
                for(int c = 0; c < 8; c++) {
                    if(charMatrix[r][c] == c1) {
                    	row1 = r; col1 = c;
                    }
                    if(charMatrix[r][c] == c2) {
                    	row2 = r; col2 = c;
                    	}
                }
            }
            
            if(row1 == row2) {
                int newCol1 = (col1 - 1 + 8) % 8;
                int newCol2 = (col2 - 1 + 8) % 8;
                plainText += charMatrix[row1][newCol1];
                plainText += charMatrix[row2][newCol2];
            } 
            else if(col1 == col2) {
                int newRow1 = (row1 - 1 + 8) % 8;
                int newRow2 = (row2 - 1 + 8) % 8;
                plainText += charMatrix[newRow1][col1];
                plainText += charMatrix[newRow2][col2];              
            } 
            else {
                plainText += charMatrix[row1][col2];
                plainText += charMatrix[row2][col1];
            }
        }
        
        // Append the last odd character exactly as it is
        if(encryptedText.length() % 2 != 0) {
            plainText += encryptedText.charAt(encryptedText.length() - 1);
        }
        
        return plainText;
    }

    /**
     * Caesar Cipher is a simple substitution cipher that replaces each letter in a message 
     * with a letter some fixed number of positions down the alphabet. 
     * For example, with a shift of 3, 'A' would become 'D', 'B' would become 'E', and so on.
     */    
 
    public static String caesarEncryption(String plainText, int key) {
        if(!isStringInBounds(plainText)) {
            return "The selected string is not in bounds, Try again.";
        }
        
        String finalEncrypt = "";
        int shift = key % RANGE;
        
        for(int i = 0; i < plainText.length(); i++) {
            char currentChar = plainText.charAt(i);
            int encryptIndex = ((currentChar - LOWER_RANGE) + shift) % RANGE;
            if (encryptIndex < 0) {
            	encryptIndex += RANGE;
            }
            
            finalEncrypt += (char)(encryptIndex + LOWER_RANGE);
        }
        
        return finalEncrypt;
    }

    public static String caesarDecryption(String encryptedText, int key) {
        if(!isStringInBounds(encryptedText)) {
            return "The selected string is not in bounds, Try again.";
        }
        
        String plainText = "";
        int shift = key % RANGE;
        
        for(int i = 0; i < encryptedText.length(); i++) {
            char c = encryptedText.charAt(i);
            int decryptIndex = ((c - LOWER_RANGE) - shift) % RANGE;
            if (decryptIndex < 0) {
            	decryptIndex += RANGE;
            }
            
            plainText += (char)(decryptIndex + LOWER_RANGE);
        }
        
        return plainText;
    }
   }