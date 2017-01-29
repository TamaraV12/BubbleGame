package com.example.tamara.bubblegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by tamara on 8/27/16.
 * Needle is defined by a moving object point, length and the angle
 * between needle and point's y-coordinate
 */
// TODO: 29.1.17. resiti  drhtanje!!! 
public class Needle extends MovingObject{
    private static final String TAG = "NEEDLE";
    private float angle;
    private float length;
    private static Paint paint =new Paint();
    public boolean record_dbg;

    static{
        paint.setColor(0xFFAAAAAA);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }

    public Needle(float speed, float x, float y, float length) {
        super(speed/4, x, y, paint);
        this.length =length;
        displacementDown = length;
        angle=0f;
    }

    @Override
    public boolean move(float axisX, float axisY, float deltaT) {
        boolean  head = super.move(axisX, axisY, deltaT);
        float deltaAngle = (float)Math.atan(speedX/speedY);
        angle = angle*0.95f +deltaAngle*0.05f;
        float delX =(float) Math.sin(angle)*length;
        float delY =(float) Math.cos(angle)*length;
        if(delY>0){
            displacementDown=delY;
            displacementUp =0;
        }
        else {
            displacementUp=-delY;
            displacementDown=0;
        }
        if(delX>0){
            displacementLeft=delX;
            displacementRight=0;
        }
        else {
            displacementRight=-delX;
            displacementLeft=0;
        }
        return head;
    }

    @Override
    public void colided(Bubble bubble) {
        float dist = (float)Math.sqrt((x-bubble.x)*(x-bubble.x)+(y-bubble.y)*(y-bubble.y));
        float sina=(float)Math.sin(angle);
        float cosa=(float)Math.cos(angle);
        float x2=x+sina*length;// other point of the needle
        float y2=y+cosa*length;
        float dist2=(float)Math.sqrt((x2-bubble.x)*(x2-bubble.x)+(y2-bubble.y)*(y2-bubble.y));
        if(dist<bubble.getRadius()){
            //// TODO: 29.1.17. Probusi balon 
            if(record_dbg) Log.d(TAG, "colided: vrh");
            x=bubble.x+(x-bubble.x)*bubble.getRadius()/dist;
            y=bubble.y+(y-bubble.y)*bubble.getRadius()/dist;
            angle = (float)Math.atan((x2-x)/(y2-y));
        }
        if (dist2<bubble.getRadius()){
            if (record_dbg) Log.d(TAG, "colided: kraj 1 "+angle*180/Math.PI);
            x2=bubble.x+(x2-bubble.x)*bubble.getRadius()/dist2;
            y2=bubble.y+(y2-bubble.y)*bubble.getRadius()/dist2;
            angle = (float)Math.atan((x2-x)/(y2-y));
            if (record_dbg) Log.d(TAG, "colided: kraj 2 "+angle*180/Math.PI);
        }
        float xp=sina*sina*bubble.x+cosa*cosa*x+cosa*sina*(bubble.y-y); //nearest point to the bubble
        float yp=cosa*cosa*bubble.y+sina*sina*y+cosa*sina*(bubble.x-x);
        if(record_dbg)Log.d(TAG, "colided: xp"+xp+" yp "+yp+" x "+x+" y "+y+" x2 "+x2+" y2 "+y2);
        if((((xp>=x-5)&&(xp<=x2+5))||((xp>=x2-5)&&(xp<=x+5)))&&
                (((yp>=y-5)&&(yp<=y2+5))||((yp>=y2-5)&&(yp<=y+5)))){
            float distn=(float) Math.sqrt((xp-bubble.x)*(xp-bubble.x)+(yp-bubble.y)*(yp-bubble.y));
            if(distn<bubble.getRadius()){
                if(record_dbg) Log.d(TAG, "colided: x "+x+" y "+y+" x2 "+x2+" y2 "+y2);
                if(record_dbg) Log.d(TAG, "colided: xp "+xp+" yp "+yp+" xc "+bubble.x+" yc "+bubble.y+" distn "+distn+" radius"+bubble.getRadius());
                xp=bubble.x+(xp-bubble.x)*bubble.getRadius()/distn;
                yp=bubble.y+(yp-bubble.y)*bubble.getRadius()/distn;
                if(record_dbg) Log.d(TAG, "colided: 1 "+angle*180/Math.PI+" xp "+xp+" yp "+yp);
                float distt = (float) Math.sqrt((xp-x)*(xp-x)+(yp-y)*(yp-y));
                if(distt<length/2){
                    angle = (float)Math.atan((x2-xp)/(y2-yp));
                    x= (float) (x2-Math.sin(angle)*length);
                    y= (float) (y2-Math.cos(angle)*length);
                }
                else angle = (float)Math.atan((xp-x)/(yp-y));
                if(record_dbg) Log.d(TAG, "colided: 2 "+angle*180/Math.PI);
            }
        }
    }


    @Override
    public void draw(Canvas c) {
        float delX =(float) Math.sin(angle)*length;
        float delY =(float) Math.cos(angle)*length;
        c.drawLine(x,y, x+delX, y+delY, drawPaint);
        c.drawCircle(x+delX,y+delY,6,drawPaint);
    }


}
