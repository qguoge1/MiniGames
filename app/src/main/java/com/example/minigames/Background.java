package com.example.minigames;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x,y;
    Bitmap background;
    Background (int screenX, int screenY, Resources res){
        // Charge l'image de fond
        background = BitmapFactory.decodeResource(res, R.drawable.background7);
        background = Bitmap.createScaledBitmap(background,screenX,screenY,false);

    }
}
