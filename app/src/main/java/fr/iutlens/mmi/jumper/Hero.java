package fr.iutlens.mmi.jumper;

import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;

import fr.iutlens.mmi.jumper.utils.SpriteSheet;

/**
 * Created by dubois on 30/12/2017.
 */

public class Hero {

    public static final int SAME_FRAME = 3;
    private final float BASELINE = 0.93f;


    public static final float MAX_STRENGTH = 2f;
    private final float G = 0.2f;
    private final float IMPULSE = 2.5f;

    private SpriteSheet sprite;

    private float y;
    private float vy;
    private float vx;

    private float jump;

    private int frame;
    private int cpt;
    public boolean perdu;


    public Hero(int sprite_id, float vx){
        sprite = SpriteSheet.get(sprite_id);
        y = 0;
        vy = 0;
        jump = 0;
        frame =0;
        cpt = 0;
        this.vx = vx;
        perdu = false;
    }


    public float getY(){
        return y;
    }

    public void update(float floor, float slope, float vx, int tuile){
        Log.d("update","tuile:"+tuile);
        this.vx = vx;
        y += vy; // inertie
        float altitude = y-floor;
        if(altitude<-10){
            perdu = true;
        } //game over
        if (altitude <0 && altitude>-1){ // On est dans le sol : atterrissage
            vy = 0; //floor-y;
            y = floor;
            altitude = 0;
        }

        if (altitude == 0){ // en contact avec le sol
            if(tuile < 6) {
                if (jump != 0) {
                    vy = jump * IMPULSE * GameView.SPEED; // On saute ?
                    frame = 3;
                } else {
                    //                vy = -G*vx;
                    if (slope*vx<0) vy = slope * vx - G * GameView.SPEED; // On suit le sol...
                    if (vx != 0) {
                        cpt = (cpt + 1) % SAME_FRAME;
                        if (cpt == 0) frame = (frame + 1) % 8;
                    }
                }
            }else{
                perdu = true;
            }
        } else { // actuellement en vol
            vy -= G*GameView.SPEED; // effet de la gravité
            frame = (vy>0) ? 3 : 5;
//            if (y < floor+slope*vx) y = floor+slope*vx; // atterrissage ?
        }





        jump = 0;
    }

    public void paint(Canvas canvas, float x, float y, float vx){

        canvas.save();
        canvas.translate(x,0);

        if(vx < 0) {
            canvas.scale(-1,1);
        }

        sprite.paint(canvas,frame,-sprite.w/2,y-sprite.h*BASELINE);


        canvas.restore();
    }

    public void jump(float strength) {
        if (strength>MAX_STRENGTH) strength = MAX_STRENGTH;
        if (strength> jump) jump = strength;
    }
}
