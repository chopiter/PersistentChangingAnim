package com.chopiter.persistentchanginganim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.chopiter.persistentchanginganimation.PersistentChangingAnimManager;

public class MainActivity extends AppCompatActivity {
    ImageView mImageView;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.img1);
        mImageView2 = (ImageView) findViewById(R.id.img2);
        mImageView3 = (ImageView) findViewById(R.id.img3);
        mImageView4 = (ImageView) findViewById(R.id.img4);

        final Animation animation = new RotateAnimation(0, 355,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        final PersistentChangingAnimManager manager = new PersistentChangingAnimManager(interpolation1);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.startAnim(v, animation, 2000);
            }
        });


        final PersistentChangingAnimManager manager2 = new PersistentChangingAnimManager(interpolation2);
        final Animation animation2 = new RotateAnimation(0, 359,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setRepeatCount(-1);
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager2.startAnim(v, animation2, 2000);
            }
        });

        final PersistentChangingAnimManager manager3 = new PersistentChangingAnimManager(interpolation3);
        final Animation animation3 = new RotateAnimation(0, 359,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation3.setRepeatCount(-1);
        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager3.startAnim(v, animation3, 2000);
            }
        });


        final PersistentChangingAnimManager manager4 = new PersistentChangingAnimManager(interpolation3);
        manager4.setInterpolator(new CycleInterpolator(0.5f));
        final Animation animation4 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_PARENT,0.5f,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        animation4.setRepeatCount(-1);
        mImageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager4.startAnim(v,animation4,2000);
            }
        });

    }


    /**
     * 无限加速
     */
    PersistentChangingAnimManager.AnimInterpolator interpolation1 = new PersistentChangingAnimManager.AnimInterpolator() {
        @Override
        public float getYfromX(float x) {
            float result;
            result = x * x;
            return result;
        }

        @Override
        public float getXfromY(float y) {
            float result;
            result = (float) Math.sqrt(y);
            return result;
        }
    };

    /**
     * 加速后恒定
     */
    PersistentChangingAnimManager.AnimInterpolator interpolation2 = new PersistentChangingAnimManager.AnimInterpolator() {

        @Override
        public float getYfromX(float x) {
            float result;
            if (x < 3) {
                result = x * x;
            } else {
                result = 6 * x - 9;
            }
            return result;
        }

        @Override
        public float getXfromY(float y) {
            float result;
            if (y < 9) {
                result = (float) Math.sqrt(y);
            } else {
                result = (y + 9) / 6;
            }
            return result;
        }
    };

    /**
     * 加速-匀速-减速
     */
    PersistentChangingAnimManager.AnimInterpolator interpolation3 = new PersistentChangingAnimManager.AnimInterpolator() {

        @Override
        public float getYfromX(float x) {
            // y = x^2 (0<x<3)
            // y = 6x-9 (3<=x<6)
            // y = -(x-9)(x-9)+36 (6<=x<9)
            // y = 36(x>=9)
            float result;
            if (x < 3) {
                result = x * x;
            } else if (x < 6) {
                result = 6 * x - 9;
            } else if (x < 9) {
                result = -(x - 9) * (x - 9) + 36;
            } else {
                result = 36;
            }
            return result;
        }

        @Override
        public float getXfromY(float y) {
            float result;
//          x = sqrt(y) (0<y<9)
//          x = (y + 9) / 6 (9<=y<27)
//          x = 9 - sqrt(36 - y) (27<=y<36)
//          x = 9 (y>=36)
            if (y < 9) {
                result = (float) Math.sqrt(y);
            } else if (y < 27) {
                result = (y + 9) / 6;
            } else if (y < 36) {
                result = (float) (9 - Math.sqrt(36 - y));
            } else {
                result = 9;
            }
            return result;
        }
    };
}
