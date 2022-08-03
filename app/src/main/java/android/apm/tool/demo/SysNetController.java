package android.apm.tool.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import android.util.Log;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SysNetController {

    private String TAG = "SysNetController";


    /**
     * NetworkCapabilities.NET_CAPABILITY_PRIVATE = 26
     * LiAuto ADD for multi-apn feature
     * AOSP Framework doesn't have the field,
     * So don't use NetworkCapabilities.NET_CAPABILITY_PRIVATE
     * for compile successfully
     */
    private final int NET_CAPABILITY_PRIVATE = 26;

    private ConnectivityManager connectivityManager;

//    private TelephonyManager telephonyManager;

    private Set<NetworkStateCallback> networkStateCallbacks = new HashSet<>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();


//    private Ping ping = new Ping(new Ping.Callback() {
//        @Override
//        public void onEnter(String msg) {
//            Log.i(TAG, "onEnter: " + msg);
//        }
//
//        @Override
//        public void onStart(String msg) {
//            Log.i(TAG, "onStart: " + msg);
//        }
//
//        @Override
//        public void onMessage(IcmpRes res) {
//            Log.i(TAG, "onMessage: " + res);
//        }
//
//        @Override
//        public void onError(String msg) {
//            Log.i(TAG, "onError: " + msg);
//        }
//
//        @Override
//        public void onStatistics(PingStatistics statistics) {
//            Log.i(TAG, "onStatistics: " + statistics);
//        }
//
//        @Override
//        public void onEnd(String msg) {
//            Log.i(TAG, "onEnd: " + msg);
//        }
//    });

    public interface NetworkStateCallback {

        void onAvailable();

        void onLost();

    }


    private static class Holder {
        private static final SysNetController sHolder = new SysNetController();
    }

    private SysNetController() {

    }

    public static SysNetController getInstance() {
        return Holder.sHolder;
    }

    public void onCreate(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        requestNetwork();

        //register of SignalStrengthListener must execute on MainThread
//        context.getMainExecutor().execute(this::registerSignalStrengthListener);
    }

    @SuppressLint("WrongConstant")
    private void requestNetwork() {

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        builder.addCapability(NET_CAPABILITY_PRIVATE);
        NetworkRequest request = builder.build();

        connectivityManager.requestNetwork(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.i(TAG, "onAvailable: " + network.toString());
            }

            @Override
            public void onLosing(Network network, int maxMsToLive) {
                Log.i(TAG, "onLosing: " + network.toString());
            }

            @Override
            public void onLost(Network network) {
                Log.i(TAG, "onLost: " + network.toString());
                networkStateCallbacks.forEach(NetworkStateCallback::onLost);
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                Log.i(TAG, "onLinkPropertiesChanged: network : " + network + " linkProperties : " + linkProperties);
                if (linkProperties.getRoutes() != null &&
                        linkProperties.getRoutes().size() > 0) {
                    connectivityManager.bindProcessToNetwork(network);
//                    executor.submit(() -> ping.startPing("-c 10 bj.bcebos.com"));
                    networkStateCallbacks.forEach(NetworkStateCallback::onAvailable);
                } else {
                    networkStateCallbacks.forEach(NetworkStateCallback::onLost);
                }
            }

            @Override
            public void onUnavailable() {
                Log.i(TAG, "onUnavailable: ");
            }
        });

    }

    @SuppressLint("WrongConstant")
    public boolean hasConnected() {
        Network[] networks = connectivityManager.getAllNetworks();
        boolean result = false;
        if (networks != null) {
            for (Network network : networks) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasCapability(NET_CAPABILITY_PRIVATE)) {
                        LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
                        if (linkProperties.getRoutes() != null &&
                                linkProperties.getRoutes().size() > 0) {
                            result = true;
                        }
                    }
                }
            }
        }

        return isReachable("8.8.8.8") || result;
    }


    private boolean isReachable(String hostname) {
        try {
            InetAddress address = InetAddress.getByName(hostname);
            NetworkInterface networkInterface = NetworkInterface.getByName("eth0.60");
            if (address.isReachable(networkInterface, 0,3000)) {
                return true;
            } else {
                Log.i(TAG, "isReachable: " + hostname + " network not reachable");
                return false;
            }
        } catch (Exception e) {
            Log.i(TAG, "isReachable: " + hostname + " network not reachable");
            return false;
        }
    }


    public void addNetworkStateCallback(NetworkStateCallback callback) {
        networkStateCallbacks.add(callback);
    }

    public void removeNetworkStateCallback(NetworkStateCallback callback) {
        networkStateCallbacks.remove(callback);
    }

}
