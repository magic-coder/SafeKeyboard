package com.safe.keyboard.jni;




/**
 * @author Administrator
 */
public class IJniInterface {




    public static final native boolean iJNIE();

    public static final native void addKey(String id, String text);

    public static final native void deleteKey(String id);

    public static final native void clearKey(String id);

    public static final native String getEncryptKey(String id, String timestamp);

    public static final native String getDecryptKey(String id, String timestamp);



}
