package com.example.minigames;

import static com.example.minigames.GameView.screenRatioX;
import static com.example.minigames.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Character {

    int x, y, width, height, moveCounter = 0;
    Bitmap character1, character2,character3;

    Character(int screenX, int screenY, Resources res) {
        character1 = BitmapFactory.decodeResource(res, R.drawable.fruitcatcherkiai);
        character2 = BitmapFactory.decodeResource(res, R.drawable.fruitcatcherfail);

        character1 = BitmapFactory.decodeResource(res,R.drawable.qqpleure);
        character2 = BitmapFactory.decodeResource(res,R.drawable.qqcontent);
        character3 = BitmapFactory.decodeResource(res,R.drawable.qqchaud);

        width = character1.getWidth();
        height = character1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        character1 = Bitmap.createScaledBitmap(character1, width, height, false);
        character2 = Bitmap.createScaledBitmap(character2, width, height, false);
        character3 = Bitmap.createScaledBitmap(character3, width, height, false);
        x = (int) (screenX / 2 - screenRatioX * 100);
        y = (int) (screenY /2 + screenRatioY* 200);
    }
    //Getter du personnage en fonction de l'index (Varie en fonction du combo)
    Bitmap getCharacter(int index) {
        switch(index){
            case 1:
                return character1;
            case 2:
                return character2;
            default:
                return character3;
        }
    }
    // Getter de la hitbox du personnage
    Rect getRect(){
        return new Rect(this.x,this.y,this.x+this.width,this.y+this.height/30);
    }
}