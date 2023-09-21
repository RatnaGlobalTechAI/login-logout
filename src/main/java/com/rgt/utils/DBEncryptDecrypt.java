package com.rgt.utils;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

public class DBEncryptDecrypt {
	
	private static Logger logger = LoggerFactory.getLogger(DBEncryptDecrypt.class);
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private byte[] arrayBytes;
    private String myEncryptionKey = "ThisIsSpartaThisIsSparta";
    private String myEncryptionScheme = "DESede";
    SecretKey key;

    public DBEncryptDecrypt() {
        try {
            this.arrayBytes = this.myEncryptionKey.getBytes(StandardCharsets.UTF_8);
            this.ks = new DESedeKeySpec(this.arrayBytes);
            this.skf = SecretKeyFactory.getInstance(this.myEncryptionScheme);
            this.cipher = Cipher.getInstance(this.myEncryptionScheme);
            this.key = this.skf.generateSecret(this.ks);
        } catch (Exception ex) {
            logger.error(Constant.EXCEPTION_KEY, ex.getMessage());
        }
    }

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            this.cipher.init(1, this.key);
            byte[] plainText = unencryptedString.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedText = this.cipher.doFinal(plainText);
            encryptedString = Base64Utils.encodeToString(encryptedText);
        } catch (Exception exe) {
            logger.error(Constant.EXCEPTION_KEY, exe.getMessage());
        }

        return encryptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedText = null;

        try {
            this.cipher.init(2, this.key);
            byte[] encryptedText = Base64Utils.decode(encryptedString.getBytes());
            byte[] plainText = this.cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception var5) {
            logger.error("Exception Occured======>{}", var5.getMessage());
        }

        return decryptedText;
    }
    
    public static void main(String args[]) {
    	DBEncryptDecrypt dbEncryptDecrypt = new DBEncryptDecrypt();
		System.out.println(dbEncryptDecrypt.encrypt("root"));
		System.out.println(dbEncryptDecrypt.decrypt("rIxgX9EfsxeJJSpQl9fUJg=="));
	}


}
