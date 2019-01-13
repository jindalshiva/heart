package com.example.fgw.heart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class body extends AppCompatActivity {
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        b = findViewById(R.id.huj);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(body.this, heart.class));
            }
        });
        Toast.makeText(getApplicationContext(),"Backend, done on python",Toast.LENGTH_SHORT).show();
    }
}
