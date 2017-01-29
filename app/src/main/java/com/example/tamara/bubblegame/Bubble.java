package com.example.tamara.bubblegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by tamara on 8/27/16.
 */
public class Bubble extends MovingObject {
    private static final String TAG = "BUBBLE";
    private float radius;

    public Bubble(float speed, float x, float y, float radius, Paint paint) {
        super(speed/4, x, y, paint);
        this.radius = radius;
        displacementUp=displacementDown=displacementLeft=displacementRight=radius;
    }

    @Override
    public void draw(Canvas c) {
        c.drawCircle(x, y, radius , drawPaint);
    }
//// TODO: 29.1.17. resiti lepljenje 
    @Override
    public void colided(Bubble bubble) {
        if(bubble==this)return;
        float distx=Math.abs(x-bubble.x);
        float disty=Math.abs(y-bubble.y);
        float dist=(float)Math.sqrt(distx*distx+disty*disty);
        float prefdist=radius+bubble.radius;
        if(dist<prefdist){
            float angle = (float)Math.atan(distx/disty);
            float fixx = (float) Math.sin(angle)*(prefdist-dist);
            float fixy = (float) Math.cos(angle)*(prefdist-dist);
            Bubble right= x+radius>bubble.x+bubble.radius? this:bubble;
            Bubble left= x-radius<bubble.x-bubble.radius? this:bubble;
            Bubble lower= y+radius>bubble.y+bubble.radius? this:bubble;
            Bubble higher= y-radius<bubble.y-bubble.radius? this:bubble;
            //fixing position
//            Log.d(TAG, "colided: "+fixx+" "+fixy+" "+dist+" "+prefdist);
            float rectX=left.x-left.radius;
            float rectY=higher.y- higher.radius;
            float width =distx+prefdist+fixx;
            float height =disty+prefdist+fixy;
            if(rectX+width>GameView.WIDTH)rectX=GameView.WIDTH-width;
            if(rectY+height>GameView.HEIGHT)rectY=GameView.HEIGHT-height;
            rectX=Math.max(0f, rectX-fixx*left.radius/prefdist);
            rectY=Math.max(0f, rectY-fixy*left.radius/prefdist);
            left.x=rectX+left.radius;
            right.x=left.x+distx+fixx;
            higher.y=rectY+higher.radius;
            lower.y=higher.y+disty+fixy;
            //fixing speed
            speedX = -speedX*bubble.radius/prefdist;
            speedY = -speedY*bubble.radius/prefdist;
            bubble.speedX = -bubble.speedX*radius/prefdist;
            bubble.speedY = -bubble.speedY*radius/prefdist;
//            Log.d(TAG, "colided: "+speedX+" "+speedY+" "+bubble.speedX+" "+bubble.speedY);
        }
    }

    public float getRadius() {
        return radius;
    }
}
