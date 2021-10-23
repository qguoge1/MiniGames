package com.example.minigames;

import static com.example.minigames.GameView.screenRatioX;
import static com.example.minigames.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Character {

    int x,y, width, height, moveCounter = 0;
    Bitmap character1,character2;
    Character(int screenX, int screenY, Resources res){
        character1 = BitmapFactory.decodeResource(res, R.drawable.fruitcatcherkiai);
        character2 = BitmapFactory.decodeResource(res, R.drawable.fruitcatcherfail);

        width = character1.getWidth();
        height = character1.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (width*screenRatioX);
        height = (int) (height*screenRatioY);

        character1 = Bitmap.createScaledBitmap(character1,width,height,false);
        character2 = Bitmap.createScaledBitmap(character2,width,height,false);

        x = screenX/2 - (int) screenRatioX * 100;
        y = screenY - (int) screenRatioY* 700;
    }
    Bitmap getCharacter(){
        if (moveCounter == 0){
            moveCounter ++;
            return character1;
        }
        moveCounter--;

        return character2;
    }
}
