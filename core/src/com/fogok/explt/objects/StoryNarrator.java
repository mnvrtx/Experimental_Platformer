package com.fogok.explt.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fogok.explt.core.SoundCore;
import com.fogok.explt.objects.uiwidgets.basewidgets.TextBlock;
import com.fogok.explt.utils.Localization;
import com.fogok.explt.utils.Prefers;

/**
 * Created by FOGOK on 05.12.2016 1:00.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class StoryNarrator {

    private static int startText, endText, currentText, lastCurrentText;
    private static float showTime, showMax;
    private static boolean isTimedTicked;
    private static Sprite blackCover;

    private static boolean isShow;

    private static TextBlock upGameText;


    public static void init(){

        upGameText = new TextBlock(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.95f, Localization.getText(0));
        setStory(0);
        upGameText.setCustomCff(0.23f);
        upGameText.setPositionToCenter();
        lastCurrentText = -1;
        isShow = true;
        createBlackCover();
    }

    private static void createBlackCover(){
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        blackCover = new Sprite(new Texture(pixmap));
        pixmap.dispose();
    }

    public static void setStory(int k){
        isShow = true;
        switch (k){
            case 0:
                startText = endText = 0;
                break;
            case 1:
                startText = 1;
                endText = 21;
                break;
            case 2:
                startText = 22;
                endText = 26;
                break;
            case 3:
                startText = endText = 27;
                break;
            case 4:
                startText = 28;
                endText = 31;
                break;
            case 5:
                startText = 32;
                endText = 35;
                break;
            case 6:
                startText = endText = 36;
                break;
            case 7:
                startText = endText = 37;
                break;
            case 8:
                startText = 38;
                endText = 39;
                break;
            case 9:
                startText = 40;
                endText = 47;
                break;
        }
        currentText = startText;
        isTimedTicked = false;
        showTime = 0f;
    }

    public static void setCurrentText(int currentText) {
        StoryNarrator.currentText = currentText;
    }

    public static void setIsShow(boolean isShow) {
        StoryNarrator.isShow = isShow;
    }

    public static void drawAndNarrate(SpriteBatch batch){
        if (isShow){
            calcTime();
            if (!upGameText.isBlackColor())
                blackCover.draw(batch);

            upGameText.draw(batch);
        }
    }

    private static void calcTime(){
        if (currentText != 0 && currentText != 47){
            if (!isTimedTicked){
                isTimedTicked = true;
                showMax = Localization.getText(currentText).length() * 0.08f;
                showMax = Math.max(showMax, 2f);
            }
            if (showTime < showMax) {
                showTime += Gdx.graphics.getDeltaTime();
            }else{
                if (currentText < endText)
                    currentText++;
                else
                    isShow = false;


                showTime = 0f;
                isTimedTicked = false;
            }


        }
        if (lastCurrentText != currentText){
            upGameText.setText(Localization.getText(currentText));
            upGameText.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.95f);

            if (currentText < 9 || currentText > 39)
                upGameText.setBlackColor(true);
            else
                upGameText.setBlackColor(false);

            if (currentText == 21)
                Prefers.putInt(Prefers.KeyStateStory, 2);

            if (currentText == 47){
                Prefers.putInt(Prefers.KeySavePoint, 0);
                Prefers.putInt(Prefers.KeyStateCube, 0);
                Prefers.putInt(Prefers.KeyStateStory, 0);
            }

            upGameText.setPositionToCenter();
            final float otst = 10;
            blackCover.setBounds(upGameText.getBounds().getX() - otst, upGameText.getBounds().getY() - otst, upGameText.getBounds().getWidth() + otst * 2, upGameText.getBounds().getHeight() + otst * 2);

            SoundCore.playSound(SoundCore.Sounds.Notify);
        }
        lastCurrentText = currentText;
    }

    public static int getCurrentStory(){
        return currentText;
    }


    public static void dispose(){
        blackCover.getTexture().dispose();
    }
}
