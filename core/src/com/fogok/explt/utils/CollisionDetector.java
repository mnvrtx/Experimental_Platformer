package com.fogok.explt.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by FOGOK on 17.07.2016 1:02.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class CollisionDetector {


    public static boolean isTouch(final Polygon p1, final Polygon p2){
        return Intersector.overlapConvexPolygons(p1, p2);
    }

//    public static boolean isDotTouch(final float x, final float y, final Polygon polygon){
//        return Intersector.overl
//    }

    public static boolean isTouchBound(final Rectangle b1, final Rectangle b2){
       return b1.overlaps(b2);
    }

    public static boolean isTouchDot(float x, float y, Rectangle rectangle){
        return rectangle.contains(x, y);
    }
}
