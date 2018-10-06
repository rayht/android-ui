package com.canvas.test;

import android.graphics.LinearGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.canvas.test.shader.BitmapShaderView;
import com.canvas.test.shader.LevelBar;
import com.canvas.test.shader.RadarGradientView;
import com.canvas.test.shader.SweepGradientView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        setContentView(new BitmapShaderView(this));
//        setContentView(new LevelBar(this));
//        setContentView(new SweepGradientView(this));
        setContentView(new RadarGradientView(this));
    }
}
