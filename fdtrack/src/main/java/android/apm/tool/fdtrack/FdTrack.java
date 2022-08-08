package android.apm.tool.fdtrack;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FdTrack {

    private static final String TAG = "FdTrack";

    private static final int FDTRACK_ENABLE_THRESHOLD = 30;

    private static final int FDTRACK_ABORT_THRESHOLD = 60;

    private static final int FDTRACK_INTERVAL = 2;

    private int fdTrackEnableThreshold = FDTRACK_ENABLE_THRESHOLD;

    private int fdTrackAbortThreshold = FDTRACK_ABORT_THRESHOLD;

    private int fdTrackInterval = FDTRACK_INTERVAL;

    private boolean enabled = false;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // Used to load the 'fdtrack' library on application startup.
    static {
        System.loadLibrary("fdtrack_run");
    }


    public FdTrack() {

    }

    public FdTrack(int trackEnableThreshold, int trackAbortThreshold, int trackInterval) {
        fdTrackEnableThreshold = trackEnableThreshold > 0 ? trackEnableThreshold : FDTRACK_ENABLE_THRESHOLD;
        fdTrackAbortThreshold = trackAbortThreshold > 0 ? trackAbortThreshold : FDTRACK_ABORT_THRESHOLD;
        fdTrackInterval = trackInterval > 0 ? trackInterval : FDTRACK_INTERVAL;
     }

    public void spawnFdLeakCheckThread() {
        executor.submit(this::fdLeakCheck);
    }


    private void fdLeakCheck() {
        int maxFd = getMaxFd();
        Log.i(TAG, "fdLeakCheck: maxfd = " + maxFd);
        if (maxFd > fdTrackEnableThreshold) {
            // Do a manual GC to clean up fds that are hanging around as garbage.
            System.gc();
            System.runFinalization();
            maxFd = getMaxFd();
        }

        if (maxFd > fdTrackEnableThreshold && !enabled) {
            Log.i(TAG, "fdtrack enable threshold reached, enabling maxfd = "+ maxFd);
            System.loadLibrary("fdtrack");
            enabled = true;
        } else if (maxFd > fdTrackAbortThreshold) {
            Log.i(TAG, "fdtrack abort threshold reached, dumping and aborting maxFd = "+ maxFd);
            fdTrackAbort();
            return;
        }

        executor.schedule(this::fdLeakCheck, fdTrackInterval, TimeUnit.SECONDS);
    }



    private native int getMaxFd();



    private native void fdTrackAbort();
}