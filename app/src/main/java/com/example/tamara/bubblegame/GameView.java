package com.example.tamara.bubblegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by tamara on 8/27/16.
 */
public class GameView extends SurfaceView implements SensorEventListener {
    public static final int WIDTH=600;
    public static final int HEIGHT =800;
    private static final float A = 0.9F;
    private Paint drawPaint, canvasPaint;
    private float oldx;
    private float oldy;
    private long lastTime;
    Bubble bubble;
//    Bubble bubble2;
    Needle needle;

    public GameView(Context context, AttributeSet attrs) {
        super(context,attrs);
        drawPaint =new Paint();
        drawPaint.setColor(0xFF00FFFF);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.BEVEL);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        lastTime = System.currentTimeMillis();
        bubble =new Bubble(1.5f, 80f, 70f, 80, drawPaint);
//        bubble2 =new Bubble(1.5f, 200f, 300f, 70, drawPaint);
        needle = new Needle(1.6f, 200f, 400f, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFFFFF);
        canvas.drawRect(new Rect(0,0,WIDTH, HEIGHT),drawPaint);
        bubble.draw(canvas);
//        bubble2.draw(canvas);
        needle.draw(canvas);
    }

    private float lowFrequencyFilter(float last, float newv){
        return last*(1-A)+A*newv;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long timeStamp=System.currentTimeMillis();
        float axisX = sensorEvent.values[0];
        float axisY = sensorEvent.values[1];
        axisX=lowFrequencyFilter(oldx,axisX);
        axisY=lowFrequencyFilter(oldx,axisY);
        oldx=axisX;
        oldy=axisY;
        float deltaT=(lastTime-timeStamp)*0.001f;
        axisX *=deltaT;
        axisY *=deltaT;
        bubble.move(axisX,axisY,deltaT);
//        bubble2.move(axisX,axisY,deltaT);
//        bubble.colided(bubble2);
//        bubble2.colided(bubble);
        needle.move(axisX, axisY,deltaT);
//        needle.colided(bubble2);
        needle.colided(bubble);
        invalidate();
        lastTime =timeStamp;
    }

    public void setRecordDBG(){
        needle.record_dbg = !needle.record_dbg;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
