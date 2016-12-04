package com.fogok.explt.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by Oleg on 08.03.2016.
 */
public class ResHelper {

    private static float  x, y, w, h;

    public static void setParams(float cff1, float cff2, float w2Plsk){
        float qw, qh;
        qh = Gdx.graphics.getHeight();
        qw = qh * cff1;

        if (qw - w2Plsk < Gdx.graphics.getWidth()){
            qw = Gdx.graphics.getWidth() + w2Plsk;
            qh = qw * cff2;
        }

        w = qw;
        h = qh;
        x = (Gdx.graphics.getWidth() - w) / 2f;
        y = (Gdx.graphics.getHeight() - h) / 2f;
    }

    public static float getW(){
        return w;
    }

    public static float getH(){
        return h;
    }

    public static float getX(){
        return x;
    }

    public static float getY(){
        return y;
    }


}
