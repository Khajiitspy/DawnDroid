package com.example.dawn.utils;


import android.widget.Toast;

import com.example.dawn.application.HomeApplication;

public class MyLogger {
    public static void toast(String text) {
        Toast.makeText(HomeApplication.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }
}
