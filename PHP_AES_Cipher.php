<?php

 
    class JavaPHPCompatibleEncryption {
     
        private static $OPENSSL_CIPHER_NAME = "aes-128-cbc"; //Name of OpenSSL Cipher 
        private static $CIPHER_KEY_LEN = 16; //128 bits
     
        static function encrypt($key, $iv, $data) {
         
            if (strlen($key) < JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN) {
                $key = str_pad("$key", JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN, "0"); //0 pad to len 16
            } else if (strlen($key) > JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN) {
                $key = substr($str, 0, JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN); //truncate to 16 bytes
            }
             
            $encodedEncryptedData = base64_encode(openssl_encrypt($data, JavaPHPCompatibleEncryption::$OPENSSL_CIPHER_NAME, $key, OPENSSL_RAW_DATA, $iv));
            $encodedIV = base64_encode($iv);
            $encryptedPayload = $encodedEncryptedData.":".$encodedIV;
     
            return bin2hex($encryptedPayload);
             
        }
     
         
        static function decrypt($key, $data) {
            if (strlen($key) < JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN) {
                $key = str_pad("$key", JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN, "0"); //0 pad to len 16
            } else if (strlen($key) > JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN) {
                $key = substr($str, 0, JavaPHPCompatibleEncryption::$CIPHER_KEY_LEN); //truncate to 16 bytes
            }
            $dataStr = hex2bin($data);
             
            $parts = explode(':', $dataStr); //Separate Encrypted data from iv.
            $decryptedData = openssl_decrypt(base64_decode($parts[0]), JavaPHPCompatibleEncryption::$OPENSSL_CIPHER_NAME, $key, OPENSSL_RAW_DATA, base64_decode($parts[1]));
             
            return $decryptedData;
        }
     
    }
     
     
    $iv = 'abrahammagpantay'; // change this one, make sure same as Java
    $key = 'magpantayabraham'; // change this one, make sure same as Java
     
    $inputData = "Hi, I am using AES Enc-Dec Algorithm";
     
    echo "Input Text     : $inputData <br><br>";
     
    $encrypted = JavaPHPCompatibleEncryption::encrypt($key, $iv, $inputData);
     
    echo "Encrypted Text: $encrypted <br><br>";
     
    $decryptedPayload = JavaPHPCompatibleEncryption::decrypt($key, $encrypted);
     
    echo "Decrypted Text: $decryptedPayload <br><br>";
 
?>