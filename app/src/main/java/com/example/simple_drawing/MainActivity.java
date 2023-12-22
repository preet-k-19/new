package com.example.simple_drawing;

import static com.example.simple_drawing.R.id.hey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        draw drawingView = findViewById(R.id.hey);
        Button undoButton = findViewById(R.id.undo_button);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button eraseButton = findViewById(R.id.erase_button);

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.undo();
            }
        });
        Log.d("on111","yess");

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraserMode(true);// Activate eraser mode
                Log.d("on","yess");
            }
        });

        Log.d("on222","yess");


        eraseButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                drawingView.setEraserMode(false);
                Log.d("off123","no");
                return false;
            }
        });



    }

}