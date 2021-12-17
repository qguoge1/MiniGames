package com.example.minigames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    public boolean isPlaying;
    private Background background1;
    private Background background2;
    private static int screenX, screenY;
    private Paint paint;
    private Character character;
    private List<Fruit> fruits;
    public static float screenRatioX, screenRatioY;
    // Variables pour les fruits
    private int fruitDelayCnt=0;
    private int Combo = 0;
    private int fruitPopDelay=7;
    private double caughtFruitsCnt = 0;
    private double fallenFruitsCnt = 0;
    private double totalFruitsGenerated=0;
    private double accuracy= 100;
    private int score=0;
    // Variables
    public static MediaPlayer mediaPlayer;

    private boolean finished = false;
    private int gameDuration = 30000;

    CountDownTimer timer = new CountDownTimer(gameDuration, 1000) {

        public void onTick(long millisUntilFinished) {
            gameDuration -= 1000;
        }

        public void onFinish() {    // La fin du compteur, je mets le flag à 0 pour afficher le score
            finished = true;
            draw();
            sleep();
            close();
            gameIsFinished();
        }
    };

    SoundPool soundPool= new SoundPool.Builder().setMaxStreams(7).build();
    private int OhNo,Pause,hit1,hit2,hit3,hit4,hit5;
    private int endMusic,badEnding,averageEnding,goodEnding;

    private ChangeListener listener;        // Ecouteur pour détecter la fin du jeu


    public GameView(Context context, int screenX,int screenY) {
        super(context);

        GameView.screenX = (int) (screenX);
        GameView.screenY = (int) (screenY);
        // MediaPlayer pour jouer la musique de fond
        GameView.mediaPlayer = MediaPlayer.create(context,R.raw.dokidoki);

        // Ratio de l'écran qui permet de gérer les différentes tailles d'écrans (ex : 300* screenRatioX ou Y)
        screenRatioX = (float) 1920f/screenX;
        screenRatioY = (float) 1080f/screenY;
        // Variables sons (à durée courte
        OhNo= soundPool.load(context,R.raw.oh_no,1);
        Pause = soundPool.load(context,R.raw.pause,1);
        hit1= soundPool.load(context,R.raw.drum_hit_clap,1);
        hit2= soundPool.load(context,R.raw.drum_hitfinish,1);
        hit3= soundPool.load(context,R.raw.soft_slidertick,1);
        hit4= soundPool.load(context,R.raw.soft_hitwhistle,1);
        hit5= soundPool.load(context,R.raw.normal_hitwhistle,1);

        endMusic = soundPool.load(context,R.raw.awakenpillarmentheme,1);
        badEnding =soundPool.load(context,R.raw.mancryingsoundeffect,1);
        averageEnding =soundPool.load(context,R.raw.heheboiii,1);
        goodEnding =soundPool.load(context,R.raw.happyyeaaahboiiiii,1);
        // Variables de
        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        character = new Character(screenX,screenY,getResources());
        background2.x = screenX;

        paint = new Paint();

        fruits = new ArrayList<>();
    }
    //boucle principale du jeu (Mis à jour des données -> Dessin -> Temporisation
    @Override
    public void run() {
        while(isPlaying){
            mediaPlayer.start();
            update();
            draw();
            sleep();
        }
    }
    // Méthode flag à 0
    public void close(){
        isPlaying = false;

    }
    // Méthode reprise de jeu (Après une pause par exemple)
    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
        timer.start();
    }
    // Méthode mis à jour du jeu, positions des perso, fruits ou encore le score, précisions,
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
    // Méthode dessin utilisé pour le canvas
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
            String textTimer = String.format("%02ds",gameDuration/1000);

            paint.setColor(Color.CYAN);
            canvas.drawText("Time : " + textTimer, screenX*screenRatioX/2, screenRatioY*60,paint);
            paint.setColor(Color.BLUE);
            canvas.drawText("Accuracy : "+ textAccuracy,screenX-(700*screenRatioX),screenRatioY*200,paint);
            paint.setColor(Color.GREEN);
            canvas.drawText("Score :" + textScore,screenX-(700*screenRatioX),screenRatioY*100,paint);

            for (Fruit fruit : fruits){
                canvas.drawBitmap(fruit.getFruit(), fruit.x, fruit.y, paint);
            }
            if(finished == true ){
                canvas.drawBitmap(background1.background,background1.x, background1.y,paint);
                soundPool.play(endMusic,1,1,1,0,1);
                paint.setColor(Color.GREEN);
                paint.setTextSize(150*screenRatioX);
                canvas.drawText("Score :" + textScore,screenX/3-(700*screenRatioX),screenY/2*screenRatioY,paint);
                if(accuracy < 50){
                    canvas.drawBitmap(character.getCharacter(1),(screenX/2+300)*screenRatioX,screenY/4*screenRatioY,paint);
                    soundPool.play(badEnding,1,1,1,0,1);
                }
                else if (accuracy>= 50 && accuracy <=85){
                    canvas.drawBitmap(character.getCharacter(2),(screenX/2+300)*screenRatioX,screenY/4*screenRatioY,paint);
                    soundPool.play(averageEnding,1,1,1,0,1);
                }
                else{
                    canvas.drawBitmap(character.getCharacter(3),(screenX/2+300)*screenRatioX,screenY/4*screenRatioY,paint);
                    soundPool.play(goodEnding,1,1,1,0,1);
                }
            }
            getHolder().unlockCanvasAndPost(canvas);


        }
    }
    private void sleep(){
        try {
            Thread.sleep(10);       //1/17 = ~60 fps    1/10 => 100 fps
            if(finished){
                Thread.sleep(2000);     // Pause pour afficher le score de fin
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    // Lorsque l'on clique sur le bouton back, on arrête le jeu
    public void pause(){
        try {
            thread.join();      // Mets en pause le thread
            isPlaying=false;
            timer.cancel();     // Stoppe le timer
            mediaPlayer.pause();        //Stoppe la musique
            soundPool.play(Pause,1,1,2,0,1);        //Joue la musique de pause
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Méthode qui capture l'action de l'utilisateur (doigts)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(event.getX() < screenX-character.width && event.getX()>0)        // Si l'endroit appuyé de dépasse pas l'écran
                    character.x = (int) event.getX();           // Le personnage se déplace là où l'utilisateur à appuyé
        }
        return true;
    }
    // Méthode création de fruit (position X aléatoire)
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

// Detection du changement de variable finished afin de changer d'activity dans gameActivity
    public void gameIsFinished(){

        if(listener != null) listener.onChange();
    }
    public ChangeListener getListener(){
        return listener;
    }
    public void setListener(ChangeListener listener){
        this.listener = listener;
    }

    public interface ChangeListener{
        void onChange();
    }
}
