package security;

import java.math.BigInteger;

public class Encryptor {

    private static final String keyEncryption = "";
    private static final String blurCharacter = "17c38";
    /**
     * attribute for RSA encryption and decryption of password
     */
    private static BigInteger publicKey = new BigInteger("65537");
    private static BigInteger modulus = new BigInteger("715824427497041");
    private static BigInteger privateKey = new BigInteger("560266891166993");

    /**
     *  attribute for RSA encryption and decryption of password
     */
    private static BigInteger modulusLazy = new BigInteger("589423");
    private static BigInteger privateKeyLazy = new BigInteger("86993");
    /**
     * @param message
     * @return
     */

    public static String passwordEncryption(String message) {
        if (message == null) throw new IllegalArgumentException();
        StringBuilder result = new StringBuilder();
        BigInteger[] tableBigInteger = Encryptor.StringToArrayBigInteger(message);
        for (BigInteger bi : tableBigInteger) {
            result.append(bi.modPow(Encryptor.publicKey, Encryptor.modulus));
            result.append(Encryptor.blurCharacter);
        }
        result.subSequence(0, result.length() - Encryptor.blurCharacter.length() + 1);
        return result.toString();
    }

    public static String smallEncryption(String message) {
        if (message == null) throw new IllegalArgumentException();
        StringBuilder result = new StringBuilder();
        BigInteger[] tableBigInteger = Encryptor.StringToArrayBigInteger(message);
        for (BigInteger bi : tableBigInteger) {
            result.append(bi.modPow(Encryptor.publicKey, Encryptor.modulusLazy));
            result.append(Encryptor.blurCharacter);
        }
        result.subSequence(0, result.length() - Encryptor.blurCharacter.length() + 1);
        return result.toString();
    }

    public static String passwordDecryption(String message) {
        StringBuilder result = new StringBuilder();
        BigInteger bigIntegerTemp;
        char tempChar;
        for (String s : message.split(Encryptor.blurCharacter)) {
            bigIntegerTemp = new BigInteger(s);
            tempChar = (char) bigIntegerTemp.modPow(Encryptor.privateKey, Encryptor.modulus).intValue();
            result.append(tempChar);
        }
        return result.toString();
    }

    public static String smallDecryption(String message) {
        StringBuilder result = new StringBuilder();
        BigInteger bigIntegerTemp;
        char tempChar;
        for (String s : message.split(Encryptor.blurCharacter)) {
            bigIntegerTemp = new BigInteger(s);
            tempChar = (char) bigIntegerTemp.modPow(Encryptor.privateKeyLazy, Encryptor.modulusLazy).intValue();
            result.append(tempChar);
        }
        return result.toString();
    }

    public static BigInteger[] StringToArrayBigInteger(String message) {
        BigInteger[] result = new BigInteger[message.length()];
        String temp;
        for (int index = 0; index < message.length(); index++) {
            temp = String.valueOf((int) message.charAt(index));
            result[index] = new BigInteger(temp);
        }
        return result;
    }

    public static void main(String[] args) {
        String text = "dflkjlsdf";
        String encrypted = Encryptor.smallEncryption(text);
        String decrypted = Encryptor.smallDecryption(encrypted);
        System.out.println(text.equals(decrypted));
    }
}
