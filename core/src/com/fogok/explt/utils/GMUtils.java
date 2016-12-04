package com.fogok.explt.utils;

/**
 * Created by FOGOK on 28.08.2016 17:14.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class GMUtils {

    public static float getDeg(float x1, float y1, float x2, float y2){
        return (float) (Math.atan2(y1 - y2, x1 - x2) / Math.PI * 180d) + 180f;
    }

    public static float getNextX(float dist, int degr){
        return (float) Math.cos(Math.toRadians((double) degr)) * dist;
    }

    public static float getNextY(float dist, int degr){
        return (float) Math.sin(Math.toRadians((double) degr)) * dist;
    }

    public static float getDist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public static float normalizeOneZero(float alpha){
        if (alpha < 0f)
            alpha = 0f;
        else if (alpha > 1f)
            alpha = 1f;
        return alpha;
    }
}
