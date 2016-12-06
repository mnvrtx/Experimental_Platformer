package com.fogok.explt.objects.uiwidgets.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fogok.explt.core.AtlasLoader;
import com.fogok.explt.core.SoundCore;

/**
 * Created by FOGOK on 06.12.2016 1:30.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class Button extends BaseButton {

    public Button(AtlasLoader atlasLoader, ButtonActions.All action, float x, float y, float w, float h) {
        super(action, x, y, w, h);
        Sprite first;
        switch (action){
            case MUSIC:
                first = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_MUSIC));
                break;
            case SOUND:
                first = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_SOUND));
                break;

            default:
                first = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_SOUND));
                break;
        }

        first.setColor(Color.BLACK);

        setFirstTexture(first);
        Sprite second = new Sprite(first);
        second.setOriginCenter();
        second.setScale(1.2f);
        setSecondTexture(second);
    }


    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        boolean setRed = false;
        switch (action) {
            case MUSIC:
                setRed = SoundCore.MUSIC_ENABLED;
                break;
            case SOUND:
                setRed = SoundCore.SOUND_ENABLED;
                break;
        }

        normalTex.setColor(setRed ? Color.BLACK : Color.RED);
        touchedTex.setColor(setRed ? Color.BLACK : Color.RED);
    }

    public void dispose() {

    }
}
