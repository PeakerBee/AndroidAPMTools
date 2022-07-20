//
// Created by Peaker.Chen on 2022/7/18.
//

#ifndef PING_PING_IMPL_H
#define PING_PING_IMPL_H

#include <android/log.h>
#include "ping_common.h"

#ifdef  __cplusplus
extern "C" {
#endif

int ping(int argc, char** arg, ping_ops *ops);
void stop_ping();

#ifdef  __cplusplus
}
#endif
#endif

