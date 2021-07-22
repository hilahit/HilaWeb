package com.example.parkinson.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.parkinson.R;


/** Custom view for loading screen animation
 * has show() and hide() functions for starting and cancelling animation
 */
public class LoadingScreen extends FrameLayout {

    LottieAnimationView animationView;

    public LoadingScreen(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.setBackgroundColor(ContextCompat.getColor(context, R.color.white_70));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_loading_screen, this, true);

        animationView = findViewById(R.id.animationView);

        //We set empty click listener because we want to disable on click events when loading screen in showing
        this.setOnClickListener(v -> {
            //Do nothing
        });
    }

    public void show(){
        this.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    public void hide(){
        this.setVisibility(View.GONE);
        animationView.cancelAnimation();
    }


}
