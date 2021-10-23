package com.example.minigames;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private Background background1,background2;
    private static int screenX, screenY;
    private Paint paint;
    private Character character;
    public static float screenRatioX, screenRatioY;

    public GameView(Context context, int screenX,int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1920f/2340;
        screenRatioY = 1080f/1080;

        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        character = new Character(screenX,screenY,getResources());
        background2.x = screenX;

        paint = new Paint();
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
        background1.x -= 10*screenRatioX;
        background2.x -= 10*screenRatioX;
        if(background1.x + background1.background.getWidth()< 0){
            background1.x = screenX;
        }
        if(background2.x + background2.background.getWidth()<0){
            background2.x = screenX;
        }
    }
    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background,background1.x, background1.y,paint);
            canvas.drawBitmap(background2.background,background2.x, background2.y,paint);

            canvas.drawBitmap(character.getCharacter(),character.x,character.y,paint);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
