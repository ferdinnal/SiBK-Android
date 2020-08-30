package com.sibk.tasik.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sibk.tasik.MainActivityGuru.MainActivity;
import com.sibk.tasik.R;
import com.sibk.tasik.Utility.ScreenSize;

import static maes.tech.intentanim.CustomIntent.customType;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TextView tvName;
    private TextView tvDesc;
    private ImageView imageView2;
    private RelativeLayout rlButton;

    private String fullname;
    private int usertypeid;
    private ScreenSize ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent i = this.getIntent();
        fullname = i.getStringExtra("fullname");
        usertypeid = i.getIntExtra("usertypeid", 0);
        ss = new ScreenSize(WelcomeActivity.this);

        initView();
        setClick();
    }

    private void initView() {
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        rlButton = (RelativeLayout) findViewById(R.id.rlButton);
        tvName.setText(fullname);
    }

    public void setClick() {

        rlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i;
                if (usertypeid == 2) {
                    i = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else if (usertypeid == 3) {
                    i = new Intent(WelcomeActivity.this, com.sibk.tasik.MainActivitySiswa.MainActivity.class);
                    startActivity(i);
                    finish();
                }
                customType(WelcomeActivity.this, "bottom-to-up");
            }
        });
    }


}