# AndroidNetTools

Disappointed by the lack of good ping apis in android, I developed Java Ping Tool that call ping native interface by jni for everyday android development.

* support all ping commands

## Usage
### Add as dependency
This library is released in Maven Central

then add a library dependency.**Remember** to check for latest release [here](https://github.com/PeakerBee/AndroidNetTools/releases)

```groovy
    dependencies {
        implementation 'io.github.peakerbee:ping:0.0.4'
    }
```
### Add permission
Requires internet permission (obviously...)
```xml
  <uses-permission android:name="android.permission.INTERNET" />
```

### Ping

Only Support IPV4

```java
    Ping ping = Ping.onAddress("baidu.com");
    ping.setTimes(5);
    new Thread(() -> ping.startPing(new Ping.Callback() {
        @Override
        public void onEnter(String msg) {
            Log.i(TAG, "onEnter: " + msg);
        }

        @Override
        public void onStart(String msg) {
            Log.i(TAG, "onStart: " + msg);
        }

        @Override
        public void onResult(PingResult res) {
            Log.i(TAG, "onResult: " + res);
        }

        @Override
        public void onError(String msg) {
            Log.i(TAG, "onError: " + msg);
        }

        @Override
        public void onStatistics(PingStatistics statistics) {
            Log.i(TAG, "onStatistics: " + statistics);
        }

        @Override
        public void onEnd(String msg) {
            Log.i(TAG, "onEnd: " + msg);
        }
    })).start();
```

## Building

It's a standard gradle project.

# Contributing

I welcome pull requests, issues and feedback.

- Fork it
- Create your feature branch (git checkout -b my-new-feature)
- Commit your changes (git commit -am 'Added some feature')
- Push to the branch (git push origin my-new-feature)
- Create new Pull Request

# Plan
- traceRoute Tool