package com.tenghe.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class EncryptUtil {
  private static final int ITERATION_CLOUT = 30;
  private static final int SALT_SIZE = 16;
  private static final int KEY_LENGTH = 16;

  public static String generatePassword(String password) {
    char[] chars = password.toCharArray();
    byte[] salt = getSalt();
    return hex(salt) + hex(generateSecret(chars, salt));
  }

  public static boolean matchedPassword(String originalPassword, String storedPassword) {
    byte[] storedPwdBytes = decodeHex(storedPassword);
    if(storedPwdBytes.length != SALT_SIZE + KEY_LENGTH) {
      return false;
    }
    byte[] salt = new byte[SALT_SIZE];
    byte[] hash = new byte[KEY_LENGTH];
    System.arraycopy(storedPwdBytes, 0, salt, 0, salt.length);
    System.arraycopy(storedPwdBytes, salt.length, hash, 0, hash.length);
    byte[] testHash = generateSecret(originalPassword.toCharArray(), salt);

    if(hash.length != testHash.length) {
      return false;
    }
    int diff = 0;
    for (int i = 0; i < hash.length; i++) {
      diff |= hash[i] ^ testHash[i];
    }
    return diff == 0;
  }

  private static byte[] generateSecret(char[] password, byte[] salt) {
    try {
      PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION_CLOUT, KEY_LENGTH * 8);
      return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
          .generateSecret(spec)
          .getEncoded();
    } catch(NoSuchAlgorithmException | InvalidKeySpecException ex) {
      throw new RuntimeException("generate secret error", ex);
    }
  }

  private static byte[] getSalt() {
    try {
      byte[] salt = new byte[SALT_SIZE];
      SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
      return salt;
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException("NoSuch Algorithm 'SHA1PRNG'", ex);
    }
  }

  private static String hex(byte[] arr) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arr.length; ++i) {
      sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
    }
    return sb.toString();
  }

  private static byte[] decodeHex(String hex) {
    return decodeHex(hex.toCharArray());
  }

  private static byte[] decodeHex(char[] hex) {
    final int len = hex.length;
    final byte[] out = new byte[len >> 1];
    for (int i = 0, j = 0; j < len; i++) {
      int f =  Character.digit(hex[j++], 16) << 4;
      f = f |  Character.digit(hex[j++], 16);
      out[i] = (byte) (f & 0xFF);
    }
    return out;
  }

}
