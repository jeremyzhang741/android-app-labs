package com.example.jeremy.count;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clickMeBtn = (Button) findViewById(R.id.button);
        clickMeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myClick(v); /* my method to call new intent or
activity */
            }
        });
    }

    public void myClick(View v) {
        TextView text = (TextView) findViewById(R.id.textView);
        count++;
        text.setText("Button clicked"+count+"times.");

    }
}
