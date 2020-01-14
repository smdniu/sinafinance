package com.sinafinance.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * 配置文件加密
 *
 * @author liuruizhi
 * @since 2018/12/21
 */
public class DESUtils {

    private static final String DES = "DES";
    private static final String ENCODE = "UTF-8";
    private static final String defaultKey = "caas1q2w3e4r5tdb";  //  8字节key长度

    /**
     * 使用 默认key加密
     */
    public static String encrypt(String express) throws Exception {
        if (express == null) {
            return null;
        }
        byte[] bCiphertext = encrypt(express.getBytes(ENCODE), defaultKey.getBytes(ENCODE));
        String ciphertext = new BASE64Encoder().encode(bCiphertext);
        return ciphertext;   //  返回密文
    }

    /**
     * Description 根据键值进行加密
     * @param express
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String express, String key) throws Exception {
        if (express == null) {
            return null;
        }
        byte[] bCiphertext = encrypt(express.getBytes(ENCODE), key.getBytes(ENCODE));
        String ciphertext = new BASE64Encoder().encode(bCiphertext);
        return ciphertext;   //  返回密文
    }

    /**
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * 使用 默认key 解密
     */
    public static String decrypt(String ciphertext) throws Exception {
        return decrypt(ciphertext, defaultKey);
    }

    /**
     * Description 根据键值进行解密
     * @param ciphertext
     * @param key 加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String ciphertext, String key) throws Exception {
        if (ciphertext == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(ciphertext);
        byte[] bExpress = decrypt(buf, key.getBytes(ENCODE));
        return new String(bExpress, ENCODE);
    }

    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        String express = "xxxxx";
        String ciphertext = encrypt(express);
        System.out.println(ciphertext);
        String rexpress = decrypt(ciphertext);
        System.out.println(rexpress);
    }

}
