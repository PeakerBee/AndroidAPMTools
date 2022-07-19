#include <jni.h>
#include <malloc.h>
#include <string.h>
#include "pingimpl.h"


typedef struct {
    JNIEnv* jniEnv;
    jobject jObject;
    jmethodID onStartMethodId;
    jmethodID onMessageMethodId;
    jmethodID onErrorMethodId;
    jmethodID onEndMethodId;
} Callback2Java;

Callback2Java *callback2Java;

void on_start(const char * msg) {

}

void on_message(const char * msg) {

}

void on_error(const char * error_msg, int error_code) {

}

void on_end(const char * msg) {

}

extern "C" JNIEXPORT void JNICALL Java_com_chehejia_iot_ping_Ping_nativeStartPing(JNIEnv * env, jobject thiz, jobjectArray stringArray) {
    LOGD("nativeStartPing enter.");
    callback2Java = static_cast<Callback2Java *>(malloc(sizeof(Callback2Java)));
    callback2Java->jniEnv = env;
    callback2Java->jObject = thiz;

    jclass jClass = env->GetObjectClass(thiz);

    callback2Java->onStartMethodId =  env->GetMethodID(jClass, "nativeOnStart", "(Ljava/lang/String;)V");
    callback2Java->onMessageMethodId =  env->GetMethodID(jClass, "nativeOnMessage", "(Ljava/lang/String;)V");
    callback2Java->onErrorMethodId =  env->GetMethodID(jClass, "nativeOnError", "(Ljava/lang/String;I)V");
    callback2Java->onEndMethodId =  env->GetMethodID(jClass, "nativeOnEnd", "(Ljava/lang/String;)V");

    jsize stringArrayLength = env->GetArrayLength(stringArray);

    char* args[stringArrayLength + 1];
    args[0] = strdup("ping");

    for (int i = 1; i < stringArrayLength + 1; i++) {
        jobject string_object = env->GetObjectArrayElement(stringArray, i - 1);
        jstring string_java = static_cast<jstring>(string_object);
        const char * param = env->GetStringUTFChars(string_java, NULL);
        args[i] = strdup(param);
        env->ReleaseStringUTFChars(string_java, param);
    }

    ping_ops ops = {
            .ping_start = on_start,
            .ping_message = on_message,
            .ping_error = on_error,
            .ping_end = on_end,
    };

    LOGD("nativeStartPing start.");

    ping(stringArrayLength + 1, args, &ops);

    for (int i = 0; i < stringArrayLength + 1; i++) {
        free(args[i]);
    }
    free(callback2Java);

    LOGD("nativeStartPing end.");

}



JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGD("JNI_OnLoad");
    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    LOGD("JNI_OnUnload");
}

