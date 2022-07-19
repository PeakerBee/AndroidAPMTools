//
// Created by Peaker.Chen on 2022/7/18.
//

#ifndef PING_PINGIMPL_H
#define PING_PINGIMPL_H

#include <android/log.h>
#include "ping_common.h"

#define TAG "Ping"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__)
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__)


#ifdef  __cplusplus
extern "C" {
#endif

int ping(int argc, char** arg, ping_ops *ops);

#ifdef  __cplusplus
}
#endif
#endif

