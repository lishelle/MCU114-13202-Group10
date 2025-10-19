package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtShow;
    private Button btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnStar, btnClear, btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 連結 XML 元件
        txtShow = findViewById(R.id.textView);
        btnZero = findViewById(R.id.btnZero);
        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
        btnFive = findViewById(R.id.btnFive);
        btnSix = findViewById(R.id.btnSix);
        btnSeven = findViewById(R.id.btnSeven);
        btnEight = findViewById(R.id.btnEight);
        btnNine = findViewById(R.id.btnNine);
        btnStar = findViewById(R.id.btnStar);
        btnClear = findViewById(R.id.btnClear);
        btnCall = findViewById(R.id.btnCall);   // 新增 CALL 按鈕

        // 所有按鈕共用監聽器
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = txtShow.getText().toString();
                int id = v.getId();

                if (id == R.id.btnZero) {
                    txtShow.setText(s + "0");
                } else if (id == R.id.btnOne) {
                    txtShow.setText(s + "1");
                } else if (id == R.id.btnTwo) {
                    txtShow.setText(s + "2");
                } else if (id == R.id.btnThree) {
                    txtShow.setText(s + "3");
                } else if (id == R.id.btnFour) {
                    txtShow.setText(s + "4");
                } else if (id == R.id.btnFive) {
                    txtShow.setText(s + "5");
                } else if (id == R.id.btnSix) {
                    txtShow.setText(s + "6");
                } else if (id == R.id.btnSeven) {
                    txtShow.setText(s + "7");
                } else if (id == R.id.btnEight) {
                    txtShow.setText(s + "8");
                } else if (id == R.id.btnNine) {
                    txtShow.setText(s + "9");
                } else if (id == R.id.btnStar) {
                    txtShow.setText(s + "*");
                } else if (id == R.id.btnClear) {
                    txtShow.setText("電話號碼:");
                } else if (id == R.id.btnCall) {
                    // 取得輸入的號碼
                    String number = txtShow.getText().toString();
                    if (!number.isEmpty()) {
                        // 建立撥號 Intent
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                }
            }
        };


        btnZero.setOnClickListener(myListener);
        btnOne.setOnClickListener(myListener);
        btnTwo.setOnClickListener(myListener);
        btnThree.setOnClickListener(myListener);
        btnFour.setOnClickListener(myListener);
        btnFive.setOnClickListener(myListener);
        btnSix.setOnClickListener(myListener);
        btnSeven.setOnClickListener(myListener);
        btnEight.setOnClickListener(myListener);
        btnNine.setOnClickListener(myListener);
        btnStar.setOnClickListener(myListener);
        btnClear.setOnClickListener(myListener);
        btnCall.setOnClickListener(myListener);
    }
}

