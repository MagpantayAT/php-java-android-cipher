package com.example.gsmreview.ui;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class JavaPHPCompatibleEncryption {

    private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
    private static int CIPHER_KEY_LEN = 16; //128 bits

    // 128 bit key
    public static final String ENCRYPTION_KEY = "magpantayabraham"; // change the value, make sure same as PHP

    // 16 bytes IV
    public static final String ENCRYPTION_IV = "abrahammagpantay"; // change the value, make sure same as PHP


    public static String encryptToHex(String key, String iv, String data) {
        try {
            if (key.length() < JavaPHPCompatibleEncryption.CIPHER_KEY_LEN) {
                int numPad = JavaPHPCompatibleEncryption.CIPHER_KEY_LEN - key.length();

                for(int i = 0; i < numPad; i++){
                    key += "0"; //0 pad to len 16 bytes
                }

            } else if (key.length() > JavaPHPCompatibleEncryption.CIPHER_KEY_LEN) {
                key = key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
            }

            IvParameterSpec initVector = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(JavaPHPCompatibleEncryption.CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String base64_EncryptedData = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                base64_EncryptedData = Base64.getEncoder().encodeToString(encryptedData);
            }
            String base64_IV = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                base64_IV = Base64.getEncoder().encodeToString(iv.getBytes("UTF-8"));
            }

            return asciiToHex(base64_EncryptedData + ":" + base64_IV);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static String decryptFromHex(String key, String hexdata) {
        try {
            if (key.length() < JavaPHPCompatibleEncryption.CIPHER_KEY_LEN) {
                int numPad = JavaPHPCompatibleEncryption.CIPHER_KEY_LEN - key.length();

                for(int i = 0; i < numPad; i++){
                    key += "0"; //0 pad to len 16 bytes
                }

            } else if (key.length() > JavaPHPCompatibleEncryption.CIPHER_KEY_LEN) {
                key = key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
            }

            String data = hexToAscii(hexdata);

            String[] parts = data.split(":");

            IvParameterSpec iv = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                iv = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(JavaPHPCompatibleEncryption.CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] decodedEncryptedData = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                decodedEncryptedData = Base64.getDecoder().decode(parts[0]);
            }

            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
    /*
    public static void main(String[] args) {

        String input = "Hi, I am using AES Enc-Dec Algorithm";
        String enc = encryptToHex(ENCRYPTION_KEY, ENCRYPTION_IV, input);
        String dec = decryptFromHex(ENCRYPTION_KEY,enc);
        System.out.println("Input Text     : " + input);
        System.out.println("Encrypted Text : " + enc);
        System.out.println("Decrypted Text : " + dec);
    }
    */
}