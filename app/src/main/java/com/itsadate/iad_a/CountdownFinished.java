package com.itsadate.iad_a;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class CountdownFinished extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown_finished);

        ImageView image=(ImageView)findViewById(R.id.imageView1);

        // Step1 : create the  RotateAnimation object
        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        // Step 2:  Set the Animation properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);

        // Step 3: Start animating the image
        image.startAnimation(anim);

        // Later. if you want to  stop the animation
        // image.setAnimation(null);
    }
}
