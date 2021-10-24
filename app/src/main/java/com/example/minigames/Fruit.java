package com.example.minigames;

import static com.example.minigames.GameView.screenRatioX;
import static com.example.minigames.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Fruit {
    int x,y=0,width, height;

    Bitmap fruit1,fruit2,fruit3,fruit4,fruit5;
    Bitmap fruitFinal;
    Fruit(Resources res){
        fruit1 = BitmapFactory.decodeResource(res, R.drawable.cerise);
        fruit2 = BitmapFactory.decodeResource(res, R.drawable.pomme);
        fruit3 = BitmapFactory.decodeResource(res, R.drawable.pommedor);
        fruit4 = BitmapFactory.decodeResource(res, R.drawable.pommegrise);
        fruit5 = BitmapFactory.decodeResource(res, R.drawable.pineapple);

        width = fruit1.getWidth();
        height = fruit1.getHeight();

        width /= 4;
        height /=4;

        width = (int) (width*screenRatioX);
        height = (int) (height*screenRatioY);

        fruit1 = Bitmap.createScaledBitmap(fruit1,width,height,false);
        fruit2 = Bitmap.createScaledBitmap(fruit2,width,height,false);
        fruit3 = Bitmap.createScaledBitmap(fruit3,width,height,false);
        fruit4 = Bitmap.createScaledBitmap(fruit4,width,height,false);
        fruit5 = Bitmap.createScaledBitmap(fruit5,width,height,false);
        int randomFruit = (int) (Math.random()*5+1);
        switch(randomFruit){
            case 1 :
                fruitFinal=fruit1;
                break;
            case 2 :
                fruitFinal=fruit2;
                break;
            case 3 :
                fruitFinal=fruit3;
                break;
            case 4 :
                fruitFinal=fruit4;
                break;
            case 5 :
                fruitFinal=fruit5;
                break;
        }
    }
    Bitmap getFruit(){
        return fruitFinal;

    }
}
