# AndroidNetTools

Disappointed by the lack of good ping apis in android, I developed Java Ping Tool that call ping native interface by jni for everyday android development.

* support all ping commands

## Usage
### Add as dependency
This library is not yet released in Maven Central, until then you can add as a library module or use JitPack.io

add remote maven url

```groovy

    repositories {
        maven {
            url "https://jitpack.io"
        }
    }
```

then add a library dependency. **Remember** to check for latest release [here](https://github.com/PeakerBee/AndroidNetTools/releases)

```groovy
    dependencies {
        implementation 'com.github.PeakerBee:AndroidNetTools:0.0.1'
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
    Ping ping = new Ping(new Ping.Callback() {
        @Override
        public void onEnter(String msg) {
            Log.i(TAG, "onEnter: " + msg);
        }
        
        @Override
        public void onStart(String msg) {
            Log.i(TAG, "onStart: " + msg);
        }
        
        @Override
        public void onMessage(IcmpRes res) {
            Log.i(TAG, "onMessage: " + res);
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
    });

     new Thread(() -> ping.startPing("-c 3 baidu.com")).start();
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