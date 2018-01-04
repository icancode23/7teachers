package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class splash extends Activity {
    Thread splashTread;
    TextView tvanim;

    class C02511 extends Thread {
        C02511() {
        }

        public void run() {
            int waited = 0;
            while (waited < 1500) {
                try {
                    C02511.sleep(180);
                    waited += 100;
                } catch (InterruptedException e) {
                    splash.this.finish();
                    return;
                } catch (Throwable th) {
                    splash.this.finish();
                }
            }
            splash.this.startActivity(new Intent(splash.this, front.class));
            splash.this.finish();
            splash.this.finish();
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setFormat(1);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0231R.layout.activity_splash);
        this.tvanim = (TextView) findViewById(C0231R.id.tvanim);
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, C0231R.anim.alpha);
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(C0231R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, C0231R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(C0231R.id.abv);
        Animation animnew = AnimationUtils.loadAnimation(this, C0231R.anim.alpha_anim);
        Animation anim2 = AnimationUtils.loadAnimation(this, C0231R.anim.rotate_anim3);
        Animation anim3 = AnimationUtils.loadAnimation(this, C0231R.anim.scale_anim);
        AnimationSet s = new AnimationSet(false);
        s.addAnimation(animnew);
        s.addAnimation(anim2);
        this.tvanim.startAnimation(s);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float a = (float) (((double) displaymetrics.widthPixels) / 1.4d);
        iv.getLayoutParams().height = (int) ((float) (((double) displaymetrics.heightPixels) / 1.8d));
        iv.getLayoutParams().width = (int) a;
        iv.clearAnimation();
        iv.startAnimation(anim);
        anim.setFillAfter(true);
        this.splashTread = new C02511();
        this.splashTread.start();
    }
}
