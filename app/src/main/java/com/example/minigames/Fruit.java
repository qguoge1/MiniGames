package com.example.minigames;

import static com.example.minigames.GameView.screenRatioX;
import static com.example.minigames.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Fruit {
    int x, y = 0, width, height;

    Bitmap fruit1, fruit2, fruit3, fruit4, fruit5;
    Bitmap fruitFinal;
    public int indexFruit;
    Fruit(Resources res) {
        //Avec des fruits
        fruit1 = BitmapFactory.decodeResource(res, R.drawable.cerise);
        fruit2 = BitmapFactory.decodeResource(res, R.drawable.pomme);
        fruit3 = BitmapFactory.decodeResource(res, R.drawable.pommedor);
        fruit4 = BitmapFactory.decodeResource(res, R.drawable.pommegrise);
        fruit5 = BitmapFactory.decodeResource(res, R.drawable.pineapple);
        //Avec des visages
        fruit1 = BitmapFactory.decodeResource(res, R.drawable.hug);
        fruit2 = BitmapFactory.decodeResource(res, R.drawable.qq);
        fruit3 = BitmapFactory.decodeResource(res, R.drawable.rems);
        fruit4 = BitmapFactory.decodeResource(res, R.drawable.ash);
        fruit5 = BitmapFactory.decodeResource(res, R.drawable.val);
        width = fruit1.getWidth();
        height = fruit1.getHeight();
        //Réduction de la taille de l'image (fruit)
        width /= 2;
        height /= 2;
        // Multiplication par le ratio de l'écran pour avoir une taille quasiment proportionnelle par rapport à toutes les tailles d'écrans
        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);
        // Mise à l'échelle de l'image
        fruit1 = Bitmap.createScaledBitmap(fruit1, width, height, false);
        fruit2 = Bitmap.createScaledBitmap(fruit2, width, height, false);
        fruit3 = Bitmap.createScaledBitmap(fruit3, width, height, false);
        fruit4 = Bitmap.createScaledBitmap(fruit4, width, height, false);
        fruit5 = Bitmap.createScaledBitmap(fruit5, width, height, false);


        int randomFruit = (int) (Math.random() * 5 + 1);
        //Sélection de fruit aléatoire
        switch (randomFruit) {
            case 1:
                fruitFinal = fruit1;
                indexFruit=1;
                break;
            case 2:
                fruitFinal = fruit2;
                indexFruit=2;
                break;
            case 3:
                fruitFinal = fruit3;
                indexFruit=3;
                break;
            case 4:
                fruitFinal = fruit4;
                indexFruit=4;
                break;
            case 5:
                fruitFinal = fruit5;
                indexFruit=5;
                break;
        }
    }
    // Getter de l'image du fruit généré
    Bitmap getFruit() {
        return fruitFinal;
    }
    // Getter de la hitbox du fruit
    Rect getRect() {
        return new Rect(this.x, this.y, this.x + this.width, this.y + this.height);
    }
}

