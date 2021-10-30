package com.example.minigames;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView((this),point.x, point.y);
        setContentView(gameView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //moveTaskToBack(true);
            gameView.isPlaying= false;
            setContentView(R.layout.pause);
            gameView.pause();
            findViewById(R.id.quitgame).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameView.close();
                    startActivity(new Intent(GameActivity.this,MainActivity2.class));
                }
            });
            findViewById(R.id.resume).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    setContentView(gameView);
                    gameView.isPlaying= true;
                    gameView.resume();
                }
            });

            return true;
        }

        return false;
    }

}