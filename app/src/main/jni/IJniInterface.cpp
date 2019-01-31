#include <jni.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include <iostream>
//#include "MD5.h"
#include "MD5.cpp"
using namespace std;






class HashNode{
public:
    string  mKey;
    string  mValue;
    HashNode *next;

    HashNode(string key, string value){
        mKey   = key;
        mValue = value;
        next = NULL;
    }
    ~HashNode(){
    }
    HashNode& operator=(const HashNode& node){
        if(this == &node) return *this;
        mKey = node.mKey;
        mValue = node.mValue;
        next = node.next;
        return *this;
    }
};

class HashMap{
public:
    HashMap(int size);
    ~HashMap();
    bool HMInsert(const string& key, const string& value);
    bool HMDelete(const string& key);
    string& HMFind(const string& key);
    string& operator[](const string& key);
private:
    int hashfunc(const string& key);
    HashNode ** mTable;
    int mSize;
    string strnull;
};

HashMap::HashMap(int size):mSize(size){
    mTable = new HashNode*[size];
    for(int i=0; i<mSize; ++i){
        mTable[i] = NULL;
    }
    strnull = "NULL";
}

HashMap::~HashMap(){
    for(int i=0; i<mSize; ++i){
        HashNode *curNode = mTable[i];
        while(curNode){
            HashNode *temp = curNode;
            curNode =curNode->next;
            delete temp;
        }
    }
    delete mTable;
}

HashMap hashmap(10);

bool HashMap::HMInsert(const string& key, const string& value)
{
    int index = hashfunc(key)%mSize;
    HashNode *node = new HashNode(key, value);
    node->next = mTable[index];
    mTable[index] = node;
    return true;
}

bool HashMap::HMDelete(const string &key)
{
    int index = hashfunc(key)%mSize;
    HashNode *node = mTable[index];
    HashNode *prev = NULL;
    while(node){
        if(key == node->mKey){
            if(NULL == prev){
                mTable[index] = node->next;
            }else{
                prev->next = node->next;
            }
            delete node;
            return true;
        }
        prev = node;
        node = node->next;
    }
    return false;
}

string& HashMap::HMFind(const string& key)
{
    int index = hashfunc(key)%mSize;
    if(NULL == mTable[index]){
        return strnull;
    }else{
        HashNode *node = mTable[index];
        while(node){
            if(key == node->mKey){
                return node->mValue;
            }
            node = node->next;
        }
    }
    return strnull;
}

string& HashMap::operator[](const string& key)
{
    return HMFind(key);
}

int HashMap::hashfunc(const string& key){
    int hash = 0;
    for(int i=0; i<key.length(); ++i){
        hash = hash << 7^key[i];
    }
    return (hash & 0x7FFFFFFF);
}


/*
 * Method:    initJNIEnv
 */
jboolean Jni_iJNIE(JNIEnv *pEnv, jclass msgFactoryClass) {
    return JNI_TRUE;
}

string jstring2str(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    std::string stemp(rtn);
    free(rtn);
    return stemp;
}

/*jstring getKey(JNIEnv *env, jstring id) {
    jclass AESencrypt;
    jmethodID mid;
    jstring result;
    AESencrypt = env->FindClass("com/safe/keyboard/jni/IJniInterface");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = env->GetStaticMethodID(AESencrypt, "getKey",
                           "(Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        env->DeleteLocalRef(AESencrypt);
        return NULL;
    }
    result = (jstring) env->CallStaticObjectMethod(AESencrypt, mid, id);
    (env)->DeleteLocalRef(AESencrypt);
    return result;
}*/

/*
 * 每添加一位密码调用该方法，使用jni方法加密保存当前输入的这一位密码
 * Method:    appendPwd
 * Signature: (Ljava/lang/String;)V
 */
void Jni_addKey(JNIEnv *env, jclass msgFactoryClass, jstring id, jstring str) {
   /* jclass AESencrypt;
    jmethodID mid;
    AESencrypt = env->FindClass("com/safe/keyboard/jni/IJniInterface");
    if (NULL == AESencrypt) {
        return;
    }
    mid = env->GetStaticMethodID(AESencrypt, "addKey",
                                 "(Ljava/lang/String;Ljava/lang/String;)V");
    if (NULL == mid) {
        env->DeleteLocalRef(AESencrypt);
        return;
    }
    env->CallStaticVoidMethod(AESencrypt, mid, id, str);
    (env)->DeleteLocalRef(AESencrypt);*/
    hashmap.HMInsert(jstring2str(env, id),hashmap.HMFind(jstring2str(env, id)).append(jstring2str(env, str)));


    /* std::string str = jstring2str(pEnv, c);            *//* 获得传入的字符串，将其转换为native Strings *//*
    input.append(str);*/
}

/*
 * 每删除一位密码调用该方法，从本地删除密码中删除
 * Method:    deleteOnePwd
 * Signature: ()V
 */
void Jni_deleteKey(JNIEnv *env, jclass msgFactoryClass, jstring id) {
   /* jclass AESencrypt;
    jmethodID mid;
    AESencrypt = env->FindClass("com/safe/keyboard/jni/IJniInterface");
    if (NULL == AESencrypt) {
        return;
    }
    mid = env->GetStaticMethodID(AESencrypt, "delKey",
                           "(Ljava/lang/String;)V");
    if (NULL == mid) {
        env->DeleteLocalRef(AESencrypt);
        return;
    }
    env->CallStaticVoidMethod(AESencrypt, mid, id);
    (env)->DeleteLocalRef(AESencrypt);*/
    //input.pop_back();
    string input = hashmap.HMFind(jstring2str(env, id));
    input.pop_back();
    hashmap.HMInsert(jstring2str(env, id),input);
}

/*
 * 清空密码
 * Method:    clearPwd
 * Signature: ()V
 */
void Jni_clearKey(JNIEnv *env, jclass msgFactoryClass, jstring id) {
   /* jclass AESencrypt;
    jmethodID mid;
    AESencrypt = env->FindClass("com/safe/keyboard/jni/IJniInterface");
    if (NULL == AESencrypt) {
        return;
    }
    mid = env->GetStaticMethodID(AESencrypt, "clearKey",
                           "(Ljava/lang/String;)V");
    if (NULL == mid) {
        env->DeleteLocalRef(AESencrypt);
        return;
    }
    env->CallStaticVoidMethod(AESencrypt, mid, id);
    (env)->DeleteLocalRef(AESencrypt);*/
    //input.clear();
    string input = hashmap.HMFind(jstring2str(env, id));
    input.clear();
    hashmap.HMInsert(jstring2str(env, id),input);
}


/*
 * 获取完整的加密密码
 * Method:    getEncryptedPin
 * Signature: ()Ljava/lang/String;
 */
jstring Jni_getEncryptKey(JNIEnv *env, jclass msgFactoryClass, jstring id, jstring timestamp) {

    //jstring jstr = getKey(env, id);

    string input = hashmap.HMFind(jstring2str(env, id));
   // jstring jstr = (env)->NewStringUTF(input.data());
    const char *originStr;
    //将jstring转化成char *类型
    originStr = input.data();
    MD5 md5 = MD5(input);
    string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());

    /*if (timestamp == NULL) {
        return NULL;
    }

    jstring result;
    jclass AESencrypt;
    jmethodID mid;

    AESencrypt = env->FindClass("com/safe/keyboard/jni/IJniInterface");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = env->GetStaticMethodID(AESencrypt, "encrypt",
                           "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        env->DeleteLocalRef(AESencrypt);
        return NULL;
    }

    jstring key = (env)->NewStringUTF("0123456789123456");
    jstring key_iv = (env)->NewStringUTF("0123456789123456");
    result = (jstring) env->CallStaticObjectMethod(AESencrypt, mid, key, key_iv, jstr);
    (env)->DeleteLocalRef(AESencrypt);
    (env)->DeleteLocalRef(jstr);
    return result;*/

    // return pEnv->NewStringUTF(input.data());
}

/*
 * 获取完整的明文密码
 * Method:    getEncryptedPin
 * Signature: ()Ljava/lang/String;
 */
jstring Jni_getDecryptKey(JNIEnv *env, jclass msgFactoryClass, jstring id, jstring timestamp) {
    /* if (timestamp == NULL) {
         return NULL;
     }

     jstring result;
     jclass AESencrypt;
     jmethodID mid;

     AESencrypt = env->FindClass("com/vigorous/safetykeyboard/jni/IJniInterface");
     if (NULL == AESencrypt) {
         return NULL;
     }
     mid = env->GetStaticMethodID(AESencrypt, "decrypt",
                                  "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
     if (NULL == mid) {
         env->DeleteLocalRef(AESencrypt);
         return NULL;
     }
     jstring jstr = (env)->NewStringUTF(input.data());
     jstring key = (env)->NewStringUTF("0123456789123456");
     jstring key_iv = (env)->NewStringUTF("0123456789123456");
     result = (jstring) env->CallStaticObjectMethod(AESencrypt, mid, key,key_iv, jstr);
     (env)->DeleteLocalRef(AESencrypt);
     (env)->DeleteLocalRef(jstr);
     return result;*/
    //return getKey(env, id);
    return (env)->NewStringUTF(hashmap.HMFind(jstring2str(env, id)).data());
}

/**
 * 方法对应表
 */
static JNINativeMethod gMethods[] =
        {
                {"iJNIE", "()Z",                                                      (void *) Jni_iJNIE},
                {"addKey",    "(Ljava/lang/String;Ljava/lang/String;)V",                  (void *) Jni_addKey},
                {"deleteKey",   "(Ljava/lang/String;)V",                                    (void *) Jni_deleteKey},
                {"clearKey",    "(Ljava/lang/String;)V",                                    (void *) Jni_clearKey},
                {"getEncryptKey",   "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) Jni_getEncryptKey},
                {"getDecryptKey",   "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) Jni_getDecryptKey},
        };

/*
 * 为某一个类注册本地方法
 */
static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * 为所有类注册本地方法
 */
static int registerNatives(JNIEnv *env) {
    const char *kClassName = "com/safe/keyboard/jni/IJniInterface"; //指定要注册的类
    return registerNativeMethods(env, kClassName, gMethods,
                                 sizeof(gMethods) / sizeof(gMethods[0]));
}

/*
 * System.loadLibrary("lib")时调用
 * 如果成功返回JNI版本, 失败返回-1
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);

    if (!registerNatives(env)) {
        return -1;
    }
    result = JNI_VERSION_1_4;

    return result;
}













