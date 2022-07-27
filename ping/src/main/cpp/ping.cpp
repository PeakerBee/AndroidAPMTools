#include <jni.h>
#include <malloc.h>
#include <string.h>
#include "ping_run.h"


typedef struct {
    JNIEnv* jniEnv;
    jobject jObject;
    jmethodID onEnterMethodId;
    jmethodID onStartMethodId;
    jmethodID onMessageMethodId;
    jmethodID onErrorMethodId;
    jmethodID onStatisticsId;
    jmethodID onEndMethodId;
} Callback2Java;

Callback2Java *callback2Java;

void on_enter(const char* fmt, ...) {
    if (fmt == NULL) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping enter");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onEnterMethodId, value);
        return;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;
    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF(buffer);
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onEnterMethodId, value);
    } else {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping enter");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onEnterMethodId, value);
    }
}

void on_start(const char* fmt, ...) {
    if (fmt == NULL) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping start");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onStartMethodId, value);
        return;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;
    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF(buffer);
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onStartMethodId, value);
    } else {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping start");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onStartMethodId, value);
    }
}


void on_result(const char* fmt, ...) {
    if (fmt == NULL) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onMessageMethodId, value);
        return;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;
    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF(buffer);
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onMessageMethodId, value);
    } else {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onMessageMethodId, value);
    }
}



void on_error(const char* fmt, ...) {
    if (fmt == NULL) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping error");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onErrorMethodId, value);
        return;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;
    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF(buffer);
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onErrorMethodId, value);
    } else {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping error");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onErrorMethodId, value);
    }
}


void on_statistics(const char* fmt, ...) {
    if (fmt == NULL) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onStatisticsId, value);
        return;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;
    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF(buffer);
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onStatisticsId, value);
    } else {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onStatisticsId, value);
    }
}


void on_end(const char* fmt, ...) {
    if (fmt == NULL) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping end");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onEndMethodId, value);
        return;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;
    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (callback2Java->jniEnv)->NewStringUTF(buffer);
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onEndMethodId, value);
    } else {
        jstring value = (callback2Java->jniEnv)->NewStringUTF("ping end");
        (callback2Java->jniEnv)->CallVoidMethod(callback2Java->jObject, callback2Java->onEndMethodId, value);
    }
}


extern "C" JNIEXPORT void JNICALL Java_android_net_tool_ping_Ping_nativeStartPing(JNIEnv * env, jobject thiz, jobjectArray stringArray) {

    callback2Java = static_cast<Callback2Java *>(malloc(sizeof(Callback2Java)));
    callback2Java->jniEnv = env;
    callback2Java->jObject = thiz;

    jclass jClass = env->GetObjectClass(thiz);

    callback2Java->onEnterMethodId =  env->GetMethodID(jClass, "nativeOnEnter", "(Ljava/lang/String;)V");
    callback2Java->onStartMethodId =  env->GetMethodID(jClass, "nativeOnStart", "(Ljava/lang/String;)V");
    callback2Java->onMessageMethodId =  env->GetMethodID(jClass, "nativeOnMessage", "(Ljava/lang/String;)V");
    callback2Java->onErrorMethodId =  env->GetMethodID(jClass, "nativeOnError", "(Ljava/lang/String;)V");
    callback2Java->onStatisticsId = env->GetMethodID(jClass, "nativeOnStatistics", "(Ljava/lang/String;)V");
    callback2Java->onEndMethodId =  env->GetMethodID(jClass, "nativeOnEnd", "(Ljava/lang/String;)V");

    jsize stringArrayLength = env->GetArrayLength(stringArray);

    char* args[stringArrayLength];

    for (int i = 0; i < stringArrayLength; i++) {
        jobject string_object = env->GetObjectArrayElement(stringArray, i);
        jstring string_java = static_cast<jstring>(string_object);
        const char * param = env->GetStringUTFChars(string_java, NULL);
        args[i] = strdup(param);
        env->ReleaseStringUTFChars(string_java, param);
    }

    ping_ops ops = {
            .ping_enter = on_enter,
            .ping_start = on_start,
            .ping_result = on_result,
            .ping_error = on_error,
            .ping_statistics = on_statistics,
            .ping_end = on_end,
    };
    ops.ping_enter("Ping enter");
    ping(stringArrayLength, args, &ops);

    for (int i = 0; i < stringArrayLength; i++) {
        free(args[i]);
    }
    free(callback2Java);
}


extern "C" JNIEXPORT void JNICALL Java_android_net_tool_ping_Ping_nativeStopPing(JNIEnv * env, jobject thiz) {
    stop_ping();
}




JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
}

