package com.fogok.explt.objects.uiwidgets.basewidgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fogok.explt.objects.UI;

/**
 * Created by FOGOK on 03.11.2016 14:39.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class TextBlock extends BaseObject {

    private float customCff;
    private String text;
    private boolean isBlackColor;
    public TextBlock(float x, float y, String text) {
        super(x, y, 0, 0);
        customCff = -1f;
        setText(text);
        isBlackColor = true;
    }

    public void setText(String text) {
        this.text = text;
        refreshBounds();
    }

    public void setBlackColor(boolean blackColor) {
        isBlackColor = blackColor;
    }

    public void setCustomCff(float customCff) {
        this.customCff = customCff;
        refreshBounds();
    }

    public void setDefaultCff(){
        this.customCff = -1f;
        refreshBounds();
    }

    private void refreshBounds(){
        if (customCff != -1f) UI.setCff(customCff); else UI.setDefaultCff();
        bounds.setWidth(UI.getSize(true, text));
        bounds.setHeight(UI.getSize(false, text));
        if (customCff != -1f) UI.setDefaultCff();
    }

    @Override
    public void draw(final SpriteBatch batch) {
        if (customCff != -1f) UI.setCff(customCff);

        UI.setColor(isBlackColor ? Color.BLACK : Color.WHITE);
        UI.drawText(batch, text, bounds.x, bounds.y + bounds.height);

        if (customCff != -1f) UI.setDefaultCff();
    }
}
