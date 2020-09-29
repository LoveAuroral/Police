package com.dark_yx.policemaincommon.Util;

/**
 * Created by Ligh on 2017/1/7 17:24
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密介绍
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 * Created by Lmp on 2016/8/17.
 */
public class DES {
    public static String PASSWORD = "vickn_desdes";

    /**
     * @param data 待加密的字符串
     * @return 加密的字符串Base64表示形式
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        int padding = 8 - data.length() % 8 == 8 ? 0 : 8 - data.length() % 8;
        byte[] strByte = data.getBytes();
        byte[] paddingByte = new byte[padding];
        byte[] resultByte = new byte[strByte.length + paddingByte.length];
        System.arraycopy(strByte, 0, resultByte, 0, strByte.length);
        System.arraycopy(paddingByte, 0, resultByte, strByte.length, paddingByte.length);

        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(PASSWORD.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return encode(cipher.doFinal(resultByte)).replace("\n", "");
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(String src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        byte[] b = android.util.Base64.decode(src, android.util.Base64.DEFAULT);
        return cipher.doFinal(b);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(final byte[] bytes) {
        String s = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
        return s;
    }

}
