package com.example.zr.payedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PsdEditText psdEditText = (PsdEditText) findViewById(R.id.ppe_pwd);
        psdEditText.initStyle(R.drawable.edit_bg, 6, 0.33f, R.color.color999999, R.color.color999999, 20);
        psdEditText.setOnTextFinishListener(new PsdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                Toast.makeText(MainActivity.this,"哈哈哈",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
