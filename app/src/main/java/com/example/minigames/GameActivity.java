package com.example.minigames;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Plein écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        // Création de la vue du jeu en fonction de l'écran
        gameView = new GameView((this),point.x, point.y);
        // Affichage de la vue de jeu (extension de surfaceView
        setContentView(gameView);

        // Ecouteur sur la méthode de "fin" du jeu dans gameView
        gameView.setListener(new GameView.ChangeListener() {
            @Override
            public void onChange() {
                startActivity(new Intent(GameActivity.this,MainActivity.class));
                finish();
            }
        });
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
        // En cas d'évvènement sur le bouton retour met le jeu en pause et affiche le layout pause
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //moveTaskToBack(true);
            gameView.isPlaying= false;
            setContentView(R.layout.pause);
            gameView.pause();
            // Passe à l'activity précédent si l'utilisateur choisi "Quit"
            findViewById(R.id.quitgame).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameView.close();
                    finish();
                    startActivity(new Intent(GameActivity.this,MainActivity.class));
                }
            });
            // Reprends le jeu si l'utilisateur clique sur Resume
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