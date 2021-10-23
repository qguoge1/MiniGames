package com.example.minigames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private final Background background1;
    private final Background background2;
    private static int screenX, screenY;
    private final Paint paint;
    private final Character character;
    public static float screenRatioX, screenRatioY;

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
}
