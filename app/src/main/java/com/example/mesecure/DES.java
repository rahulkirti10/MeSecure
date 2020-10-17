package com.example.mesecure;

import android.util.Base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
    private static final String unicode_format= "UTF8";
    private static final String des_encryption_scheme = "DES";
    private KeySpec myspec;
    private SecretKeyFactory mysecretkeyfactory;
    private Cipher cipher;
    byte[] keyAsBytes;
    private String myEncriptionKey,myEncriptionScheme;
    SecretKey key;

    public DES(String myEncKey) throws Exception{

        myEncriptionKey = myEncKey;
        myEncriptionScheme =  des_encryption_scheme;
        keyAsBytes = myEncriptionKey.getBytes(unicode_format);
        myspec = new DESKeySpec(keyAsBytes);
        mysecretkeyfactory = SecretKeyFactory.getInstance(myEncriptionScheme);
        cipher = Cipher.getInstance(myEncriptionScheme);
        key = mysecretkeyfactory.generateSecret(myspec);
    }

    public String encrypt(String unencryptedString)
    {
        String encryptedString =null;
        try
        {
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] plainText=unencryptedString.getBytes(unicode_format);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = Base64.encodeToString(encryptedText,Base64.DEFAULT);
        }
        catch(Exception e) {

        }

        return encryptedString;

    }

    public String Decrypt (String encryptedString)
    {
        String decryptText=null;
        try
        {
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] plainText=Base64.decode(encryptedString,Base64.DEFAULT);
            byte[] encryptedText = cipher.doFinal(plainText);
            decryptText = new String(encryptedText,"UTF-8");
        }
        catch(Exception e) {

        }

        return decryptText;
    }
}
