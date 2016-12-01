package com.fogok.explt.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by FOGOK on 01.12.2016 11:19.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class Handler {

    private AtlasLoader atlasLoader;

    public Handler() {
        atlasLoader = new AtlasLoader(0);
    }

    public void handle(SpriteBatch batch){

    }

    public void dispose() {
        atlasLoader.dispose();
    }
}
