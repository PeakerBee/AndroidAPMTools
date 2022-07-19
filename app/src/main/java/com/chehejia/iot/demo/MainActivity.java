package com.chehejia.iot.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.chehejia.iot.demo.databinding.ActivityMainBinding;
import com.chehejia.iot.ping.Ping;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private Ping ping = new Ping();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText("ping test");

        new Thread(() -> {
            Log.i("Ping", "thread name " + Thread.currentThread().getName());
            ping.nativeStartPing((String[]) Arrays.asList("ping -c baidu.ddcom").toArray());
        }).start();
    }
}