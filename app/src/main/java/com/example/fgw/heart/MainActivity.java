package com.example.fgw.heart;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText weight, tem, sug, h;
    Button calculate;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weight = findViewById(R.id.editText);
        tem = findViewById(R.id.editText3);
//        sug = findViewById(R.id.editText);
        h = findViewById(R.id.editText4);

        calculate = findViewById(R.id.button);
        databaseReference = FirebaseDatabase.getInstance().getReference("weight").child(System.currentTimeMillis() + "");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("height").child(System.currentTimeMillis() + "");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Age").child(System.currentTimeMillis() + "");
        //databaseReference3 = FirebaseDatabase.getInstance().getReference("Sugar Levels").child(System.currentTimeMillis() + "");


//        Typeface typeface = Typeface.createFromAsset(getAssets(), "keepcalm.ttf");
//
//        weight.setTypeface(typeface);
//        calculate.setTypeface(typeface);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue(weight.getText().toString());
                databaseReference1.setValue(h.getText().toString());
                databaseReference2.setValue(tem.getText().toString());

                startActivity(new Intent(MainActivity.this, Main.class));

                //String url = "https://console.dialogflow.com/api-client/demo/embedded/42b2370d-1d00-46c7-8708-bf032657ac4a";

//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
            }
        });

    }
}
