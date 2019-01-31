package com.safe.keyboard.jni;




/**
 * @author Administrator
 */
public class IJniInterface {

   // private static HashMap<String, String> keys = new HashMap<>();


    public static final native boolean iJNIE();

    public static final native void addKey(String id, String text);

    public static final native void deleteKey(String id);

    public static final native void clearKey(String id);

    public static final native String getEncryptKey(String id, String timestamp);

    public static final native String getDecryptKey(String id, String timestamp);

   /* private static String encrypt(String sKey, String sKey2, String text) throws Exception {
        if (sKey == null) {
            throw new Exception("Key为null");
        }
        if (text == null) {
            return "";
        }
        // 判断Key是否为16位
        if (sKey.length() != 16 || sKey2.length() != 16) {
            throw new Exception("Key长度不是16位");

        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(sKey2.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        //cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        //String data = String.valueOf(arr);
        //return new String(Base64.decode(encrypted,Base64.DEFAULT));
        return new String(Base64.encodeToString(encrypted, Base64.DEFAULT));
    }*/

  /*  private static String decrypt(String sKey, String sKey2, String text) {
        try {
            // 判断Key是否正确
            if (sKey == null || sKey2 == null) {
                throw new Exception("Key为null");

            }
            // 判断Key是否为16位
            if (sKey.length() != 16 || sKey2.length() != 16) {
                throw new Exception("Key长度不是16位");

            }
            byte[] raw = sKey.getBytes("UTF-8");
            byte[] bytes = new byte[16];
            for (int i = 0; i < raw.length; i++) {
                bytes[i] = raw[i];
            }
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(sKey2.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(text.getBytes(), Base64.DEFAULT);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
*/
/*

    private static void addKey(String id, String text) {
        if (keys.containsKey(id)) {
            keys.put(id, keys.get(id).concat(text));
        } else {
            keys.put(id, text);
        }
    }

    private static void delKey(String id) {
        if (keys.containsKey(id)) {
            if (keys.get(id).length() >= 1) {
                keys.put(id, keys.get(id).substring(0, keys.get(id).length() - 1));
            } else {
                keys.put(id, "");
            }

        }
    }

    private static void clearKey(String id) {
        if (keys.containsKey(id)) {
            keys.put(id, "");
        }
    }

    private static String getKey(String id) {
        if (keys.containsKey(id)) {
            return keys.get(id);
        } else {
            return "";
        }
    }
*/


}
