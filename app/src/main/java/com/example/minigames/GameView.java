package com.example.minigames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.DisplayMetrics;
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
    private int fruitDelayCnt=0;
    private int Combo = 0;
    private int fruitPopDelay=7;
    private double caughtFruitsCnt = 0;
    private double fallenFruitsCnt = 0;
    private double totalFruitsGenerated=0;
    private double accuracy= 100;

    private int score=0;
    private int GameCounter = 60;
    public static MediaPlayer mediaPlayer;

    SoundPool soundPool= new SoundPool.Builder().setMaxStreams(7).build();
    private int OhNo,Pause,hit1,hit2,hit3,hit4,hit5;

    public GameView(Context context, int screenX,int screenY) {
        super(context);
        GameView.screenX = (int) (screenX);
        GameView.screenY = (int) (screenY);
        GameView.mediaPlayer = MediaPlayer.create(context,R.raw.dokidoki);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        screenRatioX = (float) 1920f/screenX;
        screenRatioY = (float) 1080f/screenY;

        OhNo= soundPool.load(context,R.raw.oh_no,1);
        Pause = soundPool.load(context,R.raw.pause,1);
        hit1= soundPool.load(context,R.raw.drum_hit_clap,1);
        hit2= soundPool.load(context,R.raw.drum_hitfinish,1);
        hit3= soundPool.load(context,R.raw.soft_slidertick,1);
        hit4= soundPool.load(context,R.raw.soft_hitwhistle,1);
        hit5= soundPool.load(context,R.raw.normal_hitwhistle,1);

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
            mediaPlayer.start();
            update();
            draw();
            sleep();
        }
    }

    public void close(){
        isPlaying = false;
    }
    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
    private void update(){
        totalFruitsGenerated = fallenFruitsCnt+caughtFruitsCnt;
        if(totalFruitsGenerated !=0){
            accuracy = 100-(fallenFruitsCnt/totalFruitsGenerated)*100;
        }
        if(character.x > screenX-character.width){
            character.x = screenX-character.width;
        }
        if(character.x < 0){
            character.x = 0;
        }

        if( fruits.size() < 10){

            fruitDelayCnt++;
            if(fruitDelayCnt >= fruitPopDelay) {
                newFruit();
                fruitDelayCnt=0;
            }
        }
        List<Fruit> trash = new ArrayList<>();
        Rect Hitbox = new Rect();
        for (Fruit fruit : fruits){
            if(Hitbox.intersects(fruit.getRect(),character.getRect())){
                caughtFruitsCnt +=1;
                trash.add(fruit);
                switch(fruit.indexFruit){
                    case 1:
                        soundPool.play(hit1,1,1,1,0,1);
                        break;
                    case 2:
                        soundPool.play(hit2,1,1,1,0,1);
                        break;
                    case 3:
                        soundPool.play(hit3,1,1,1,0,1);
                        break;
                    case 4:
                        soundPool.play(hit4,1,1,1,0,1);
                        break;
                    case 5:
                        soundPool.play(hit5,1,1,1,0,1);
                        break;
                }
                Combo +=1;
                score+= 50*Combo;

            }
            if(fruit.y > character.y+character.height){
                trash.add(fruit);
                Combo =0;
                fallenFruitsCnt+=1;
                soundPool.play(OhNo,1,1,1,0,1);
            }
            //Cheat mode
            /*if(fruit.y > character.y-character.height){
                character.x = fruit.x;
            }*/
            if(Combo < 20) {
                fruit.y += (10 + Combo/2) * screenRatioY;
                fruitPopDelay = 20;
            }
            else {
                fruit.y += 20 * screenRatioY;
                fruitPopDelay = 10;
            }
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
            canvas.drawBitmap(background2.background,background2.x, background2.y,paint);
            if(Combo < 10){
                canvas.drawBitmap(character.getCharacter(1),character.x,character.y,paint);
            }
            else if(Combo >= 10 && Combo <20){
                canvas.drawBitmap(character.getCharacter(2),character.x,character.y,paint);
            }
            else{
                canvas.drawBitmap(character.getCharacter(3),character.x,character.y,paint);
            }

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(60*screenRatioX);
            String TextCombo = Integer.toString(Combo);

            canvas.drawText(TextCombo,(character.x+character.width/2),(character.y-character.height/3),paint);

            String textAccuracy = String.format("%.2f %%",accuracy);
            String textScore = String.format("%09d",score);
            paint.setColor(Color.BLUE);
            canvas.drawText("Accuracy : "+ textAccuracy,screenX-(700*screenRatioX),screenRatioY*200,paint);
            paint.setColor(Color.GREEN);
            canvas.drawText("Score :" + textScore,screenX-(700*screenRatioX),screenRatioY*100,paint);

            for (Fruit fruit : fruits){
                canvas.drawBitmap(fruit.getFruit(), fruit.x, fruit.y, paint);
            }
            getHolder().unlockCanvasAndPost(canvas);

        }
    }
    private void sleep(){
        try {
            Thread.sleep(10);       //1/17 = ~60 fps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public void pause(){
        try {
            thread.join();
            isPlaying=false;
            mediaPlayer.pause();
            soundPool.play(Pause,1,1,2,0,1);
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
