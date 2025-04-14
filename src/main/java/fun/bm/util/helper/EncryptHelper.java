package fun.bm.util.helper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static fun.bm.util.MainEnv.LOGGER;

public class EncryptHelper {
    public static String encrypt(String data, String key) {
        try {
            // 创建 AES 密钥
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

            // 初始化 Cipher 实例
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 加密数据
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            // 返回 Base64 编码的加密结果
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            LOGGER.warning("Failed to encrypt data: " + e.getMessage());
        }
        return null;
    }

    public static String decrypt(String encryptedData, String key) {
        try {
            // 创建 AES 密钥
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

            // 初始化 Cipher 实例
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // 解密数据
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

            // 返回解密后的字符串
            return new String(decryptedBytes);
        } catch (Exception e) {
            LOGGER.warning("Failed to decrypt data: " + e.getMessage());
            return null;
        }
    }
}
