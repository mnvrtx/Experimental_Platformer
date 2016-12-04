package com.fogok.explt.utils;


/**
 * Created by FOGOK on 17.07.2016 0:54.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class Pos {

    private float x, y;


    public Pos(final float x, final float y) {
        set(x, y);
    }

    public Pos(final Pos pos){
        set(pos.x, pos.y);
    }



    public void set(final float x, final float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
