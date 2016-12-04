package com.fogok.explt.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.fogok.explt.Main;
import com.fogok.explt.utils.CollisionDetector;

/**
 * Created by FOGOK on 01.12.2016 14:40.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class Controller {

    private static boolean left, jump, move, isTrueJump;
    private static boolean j1, j2, j3;
    private static Rectangle jT1, jT2, jT3;
    private static Sprite jt, jtAct;


    public Controller() {
        isTrueJump = true;
        left = false;
        jump = false;
        move = false;
        j1 = false;
        j2 = false;
        j3 = false;
    }

    public static void init(AtlasLoader atlasLoader){
        jt = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_J));
        jtAct = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_JACT));

        float size = (int)(Gdx.graphics.getPpcX() * 1.18f);
        float otst = size * 0.2f;

        jT1 = new Rectangle(otst, otst, size, size);
        jT2 = new Rectangle(otst * 4f + size, otst, size, size);
        jT3 = new Rectangle(Gdx.graphics.getWidth() - otst - size, otst, size, size);

//        jT1.setBounds();
    }



    private static boolean lastJ3;
    public static void handlerControls(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || j1){
            left = true;
            move = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || j2){
            left = false;
            move = true;
        } else
            move = false;

        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || (j3 && !lastJ3)) && isTrueJump){
            jump = true;
            isTrueJump = false;
        }
        lastJ3 = j3;
    }

    public static void drawControl(SpriteBatch batch){
        hhandleControl();

        batch.begin();
        Sprite sprite = jt;
        for (int i = 0; i < 3; i++) {
            switch (i){
                case 0:
                    sprite = j1 ? jtAct : jt;
                    sprite.setBounds(jT1.getX(), jT1.getY(), jT1.getWidth(), jT1.getHeight());
                    sprite.setOriginCenter();
                    sprite.setRotation(90);
                    break;
                case 1:
                    sprite = j2 ? jtAct : jt;
                    sprite.setBounds(jT2.getX(), jT2.getY(), jT2.getWidth(), jT2.getHeight());
                    sprite.setOriginCenter();
                    sprite.setRotation(-90);
                    break;
                case 2:
                    sprite = j3 ? jtAct : jt;
                    sprite.setBounds(jT3.getX(), jT3.getY(), jT3.getWidth(), jT3.getHeight());
                    sprite.setOriginCenter();
                    sprite.setRotation(0);
                    break;
            }
            sprite.draw(batch);
        }
        batch.end();

    }

    private static boolean lockH1, lockH2, lockH3;
    private static void hhandleControl(){
        j1 = j2 = j3 = lockH1 = lockH2 = lockH3 = false;


        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)){
                final int x = Gdx.input.getX(i), y = Gdx.graphics.getHeight() - Gdx.input.getY(i);

                j1 = CollisionDetector.isTouchDot(x, y, jT1) || lockH1;
                if (j1) lockH1 = true;
                j2 = CollisionDetector.isTouchDot(x, y, jT2) || lockH2;
                if (j2) lockH2 = true;
                j3 = CollisionDetector.isTouchDot(x, y, jT3) || lockH3;
                if (j3) lockH3 = true;
            }
        }

        Main.DEBUG_VALUE1 = j1 + " " + j2 + " " + j3 + " " + lockH1 + " " + lockH2 + " " + lockH3;

    }

    public static boolean getLeft(){
        return left;
    }

    public static boolean getJump(){
        return jump;
    }

    public static boolean getIsMove(){
        return move;
    }

    public static void setIsTrueJump(){
        isTrueJump = true;
    }

    public static void clearJump(){
        jump = false;
    }

    public static void dispose() {
        jt = null;
        jtAct = null;
    }
}
