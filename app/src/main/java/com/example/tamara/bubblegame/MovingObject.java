package com.example.tamara.bubblegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by tamara on 8/27/16.
 */
abstract public class MovingObject {
    private static final String TAG = "MOVOBJ";
    protected float x;
    protected float y;
    protected float speedX;
    protected float speedY;
    protected float speed;
    protected float displacementUp;
    protected float displacementDown;
    protected float displacementLeft;
    protected float displacementRight;
    protected Paint drawPaint;
    protected float collisionCoefficient = 0.5f;

    public MovingObject(float speed, float x, float y, Paint paint) {
        this.speed = speed;
        this.x = x;
        this.y = y;
        drawPaint = paint;
    }

    abstract public void draw(Canvas c);

    public boolean move(float axisX, float axisY, float deltaT){
        boolean res=false;
        speedX -= axisX;
        speedY += axisY;
        x+=speedX*deltaT*speed;
        y+=speedY*deltaT*speed;
        if(displacementLeft+x>GameView.WIDTH){
            x=GameView.WIDTH-displacementLeft;
            speedX=-speedX*collisionCoefficient;
            res=true;
        }
        if(displacementDown+y>GameView.HEIGHT){
            y=GameView.HEIGHT-displacementDown;
            speedY=-speedY*collisionCoefficient;
            res=true;
        }
        if(-displacementRight+x<0){
            x=0+displacementRight;
            speedX=-speedX*collisionCoefficient;
            res=true;
        }
        if(-displacementUp+y<0){
            y=0+displacementUp;
            speedY=-speedY*collisionCoefficient;
            res=true;
        }
        //Log.d(TAG, "move: "+x+" "+y);
        return res;
    }

    abstract public void colided(Bubble bubble);

}
