package com.noveogroup.example;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    // CR Code Review

    // TODO To Do

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new Thread() {
            @Override
            public void run() {
                try {
                    Object object = "";
                    Double value = (Double) object;
                } catch (Exception e) {

                }

                "".length();
            }
        }.start();
    }

}
