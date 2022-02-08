package com.example.cnctracking_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.cnctracking_2.ui.login.Login;

public class Splash extends AppCompatActivity {

    View logo1;
    View logo2;
    Animation topAnimation, sideAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        topAnimation =  AnimationUtils.loadAnimation(this, R.anim.top_animation);
        sideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_animation);
        logo1 =  findViewById(R.id.logo1);
        logo2 = findViewById(R.id.logo2);
        logo1.setAnimation(topAnimation);
        logo2.setAnimation(sideAnimation);
        new CountDownTimer(2000, 500) {

            public void onTick(long millisUntilFinished) {
                //nothing
            }

            public void onFinish() {
                Intent i=new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();
            }
        }.start();
    }

}