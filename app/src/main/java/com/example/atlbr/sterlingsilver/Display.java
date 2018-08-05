package com.example.atlbr.sterlingsilver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();

        String display = intent.getStringExtra("receipt");

        final TextView output = findViewById(R.id.display);
        output.setText(display);
    }
}
