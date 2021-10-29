package com.example.minigames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    public boolean isPlaying;
    private final Background background1;
    private final Background background2;
    private static int screenX, screenY;
    private final Paint paint;
    private final Character character;
    private List<Fruit> fruits;
    public static float screenRatioX, screenRatioY;
    int fruitDelayCnt=0;
    int score = 0;
    public GameView(Context context, int screenX,int screenY) {
        super(context);
        GameView.screenX = screenX;
        GameView.screenY = screenY;

        screenRatioX = 1920f/2340;
        screenRatioY = 1080f/1080;

        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        character = new Character(screenX,screenY,getResources());
        background2.x = screenX;

        paint = new Paint();

        fruits = new ArrayList<>();
    }

    @Override
    public void run() {
        while(isPlaying){
            update();
            draw();
            sleep();
        }

    }
    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
    private void update(){
        if(character.x > screenX-character.width){
            character.x = screenX-character.width;
        }
        if(character.x < 0){
            character.x = 0;
        }

        if( fruits.size() < 5){

            fruitDelayCnt++;
            if(fruitDelayCnt >= 10) {
                newFruit();
                fruitDelayCnt=0;
            }
        }
        List<Fruit> trash = new ArrayList<>();
        Rect Hitbox = new Rect();
        for (Fruit fruit : fruits){
            if(Hitbox.intersects(fruit.getRect(),character.getRect())){
                trash.add(fruit);
                score+=1;

            }
            if(fruit.y > character.y+character.height){
                trash.add(fruit);
            }
            //Cheat mode
            /*if(fruit.y > character.y-character.height){
                character.x = fruit.x;
            }*/
            fruit.y += 20*screenRatioY;

        }
        for (Fruit fruit : trash){
            fruits.remove(fruit);

            if(trash.size()>100){
                trash = new ArrayList<>();
            }
        }
    }
    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background,background1.x, background1.y,paint);
            //canvas.drawBitmap(background2.background,background2.x, background2.y,paint);

            canvas.drawBitmap(character.getCharacter(),character.x,character.y,paint);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(100*screenRatioX);
            String textScore = Integer.toString(score);
            canvas.drawText(textScore,(character.x+character.width/2),(character.y-character.height/3),paint);
            if(score > 10){
                canvas.drawText("SUPA HOT!",(character.width/2),(character.y-character.height),paint);
            }

            for (Fruit fruit : fruits){
                canvas.drawBitmap(fruit.getFruit(), fruit.x, fruit.y, paint);
            }
            getHolder().unlockCanvasAndPost(canvas);

        }
    }
    private void sleep(){
        try {
            Thread.sleep(17);       //1/17 = ~60 fps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public void pause(){
        try {
            thread.join();
            isPlaying=false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(event.getX() < screenX-character.width && event.getX()>0)
                    character.x = (int) event.getX();
        }
        return true;
    }
    public void newFruit(){
        Fruit fruit = new Fruit(getResources());
        double randomNumber = Math.random();
        int xMin = (int) (screenRatioX*150);
        int xMax =(int) ((screenX-150)*screenRatioX);
        int randomPosition = (int) (randomNumber*xMax+xMin);
        fruit.x = randomPosition;
        fruit.y = -(int) (fruit.height*screenRatioY);
        fruits.add(fruit);
    }
}
