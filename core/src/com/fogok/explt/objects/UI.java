package com.fogok.explt.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by FOGOK on 12.10.2016 16:46.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */

public class UI {

    public static final int SIZE_CHAR_FONT = 100;
    private static float CONVERTED_TO_ABSTRACT;

    private static BitmapFont bitmapFont;
    private static GlyphLayout glyphLayout;

    public static void initializate(){

//        float convertToAE = (Main.HEIGHT / (float) Gdx.graphics.getHeight());
        float cffMax = (float) Gdx.graphics.getHeight() / (float) SIZE_CHAR_FONT;

        CONVERTED_TO_ABSTRACT = cffMax * 0.15f;

        final String pathToFont = "font/font.fnt";
        bitmapFont = new BitmapFont(Gdx.files.internal(pathToFont));
        bitmapFont.setUseIntegerPositions(false);
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        bitmapFont.getData().setScale(CONVERTED_TO_ABSTRACT);

        glyphLayout = new GlyphLayout();
    }

    public static float getSize(boolean wh, String text){
        glyphLayout.setText(bitmapFont, text);
        return wh ? glyphLayout.width : glyphLayout.height;
    }

    public static void setCff(float cff){
        bitmapFont.getData().setScale(cff * CONVERTED_TO_ABSTRACT);
    }

    public static void setDefaultCff(){
        bitmapFont.getData().setScale(CONVERTED_TO_ABSTRACT);
    }

    public static void setColor(final Color color){
        bitmapFont.setColor(color);
    }

    public static void drawText(final SpriteBatch batch, final String text, final float x, final float y){
        bitmapFont.draw(batch, text, x, y);
    }

    public static BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public static void dispose(){
        bitmapFont.dispose();
        bitmapFont = null;
    }

}
