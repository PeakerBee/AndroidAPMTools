#include <jni.h>
#include <string>
#include <unistd.h>
#include <csignal>
#include <fcntl.h>
#include <cerrno>
#include <android/log.h>

#define TAG "Ping"
#define LOG(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__)

#define BIONIC_SIGNAL_FDTRACK (__SIGRTMIN + 7)

int get_current_max_fd() {
    // Not actually guaranteed to be the max, but close enough for our purposes.
    int fd = open("/dev/null", O_RDONLY | O_CLOEXEC);
    if (fd == -1) {
        LOG("failed to open /dev/null: %s", strerror(errno));
    }
    close(fd);
    return fd;
}

extern "C" JNIEXPORT void JNICALL
Java_android_apm_tool_fdtrack_FdTrack_fdTrackAbort(JNIEnv* env, jobject /* this */) {
    sigval val{};
    val.sival_int = 1;
    sigqueue(getpid(), BIONIC_SIGNAL_FDTRACK, val);
}

extern "C" JNIEXPORT int JNICALL
Java_android_apm_tool_fdtrack_FdTrack_getMaxFd(JNIEnv* env, jobject /* this */) {
    return get_current_max_fd();
}




