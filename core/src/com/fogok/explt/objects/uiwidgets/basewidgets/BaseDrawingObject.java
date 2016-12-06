package com.fogok.explt.objects.uiwidgets.basewidgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by FOGOK on 03.11.2016 13:41.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public interface BaseDrawingObject {
    void draw(final SpriteBatch batch);
    void setBounds(final float x, final float y, final float w, final float h);
    void setPosition(final float x, final float y);
    void setPositionToCenter();
    void setPositionXToCenter();
    Rectangle getBounds();
}
