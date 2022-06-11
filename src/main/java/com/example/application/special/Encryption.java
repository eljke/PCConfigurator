package com.example.application.special;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    static int randomize;
    static String encodedKey;
    static String encryptedPassword;
    static SecretKey originalKey;
    static SecretKeySpec key;

    public static void encryptPass(String passwordToEncrypt) throws Exception {

        String password = System.getProperty("password");

        if (password == null) {
            throw new IllegalArgumentException("Run with -Dpassword=<password>");
        }

        byte[] salt = String.valueOf(getRandom()).getBytes();
        int iterationCount = 40000;
        int keyLength = 128;

        key = createSecretKey(password.toCharArray(), salt, iterationCount, keyLength);
        encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

        originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        encryptedPassword = encrypt(passwordToEncrypt, key);
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public static String getEncodedSecretKey(){
        return encodedKey;
    }

    public static SecretKeySpec getSecretKey(){
        return key;
    }

    public static String getEncryptedPassword(){
        return encryptedPassword;
    }

    public static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
    }

    public static int getRandom(){
        Random random = new Random();
        randomize = random.nextInt(9999999, 999999999);
        return randomize;
    }

    private static byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }
}
