package com.fogok.explt.objects.uiwidgets.buttons;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

abstract class BaseButton {

    /**
     * Класс чистой кнопки с двумя текстурами - нажатой и ненажатой, а так же при нажатии выполняется действияе
     * */

    private boolean isTouched;
    protected ButtonActions.All action;

    //must
    protected Sprite normalTex;       ///ненажатая кнопка
    protected Sprite touchedTex;      //нажатая кнопка

    private Rectangle bounds;
    ///


    //proporties
    private boolean isEnabled;
    ///

    public BaseButton(final ButtonActions.All action, float x, float y, float w, float h){
        this.action = action;
        bounds = new Rectangle(x, y, w, h);
        isEnabled = true;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    protected void setFirstTexture(Sprite sprite){
        normalTex = sprite;
        normalTex.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }
    protected void setSecondTexture(Sprite sprite){
        touchedTex = sprite;
        touchedTex.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setPosition(float posX, float posY){
        normalTex.setPosition(posX, posY);
        touchedTex.setPosition(posX, posY);
        bounds.setPosition(posX, posY);
    }

    public void setAlpha(float alpha){
        normalTex.setAlpha(alpha);
        touchedTex.setAlpha(alpha);
    }

    public void setScale(float scaleXY){
        normalTex.setScale(scaleXY);
        touchedTex.setScale(scaleXY);
    }

    public void setOrigin(float x, float y){
        normalTex.setOrigin(x, y);
        touchedTex.setOrigin(x, y);
    }

    public void setRotation(float rotation){
        normalTex.setRotation(rotation);
        touchedTex.setRotation(rotation);
    }

    public float getX(){
        return bounds.getX();
    }

    public float getY(){
        return bounds.getY();
    }


    public void draw(SpriteBatch batch){
        if (isEnabled) calcM();
        drawButtonTexture(batch);
    }

    private void drawButtonTexture(SpriteBatch batch){      //отрисовываем тесктуру кнопки, в зависимости от того, нажата ли она или нет
        Sprite drawingTexture = isTouched ? touchedTex : normalTex;
        drawingTexture.draw(batch);
    }

//    private void drawButtonText(SpriteBatch batch){
//        UI.getTitleFont().setColor(Color.DARK_GRAY);
//        UI.drawText(batch, true, text, bounds.x + bounds.width / 2f, bounds.y + bounds.height / 2f);
//    }

    private void calcM(){
        if (Gdx.input.isTouched()){ //если на экран нажимает палец
            isTouched = bounds.contains(
                     Gdx.input.getX(),
                    (Gdx.graphics.getHeight() - Gdx.input.getY())
            );  ///определяем,  касается ли палец кнопки или нет

        }else{              //при отпускании кнопки
            if (isTouched)      ///если при отпускании кнопки палец находился на кнопке, то выполняем действие
                ButtonActions.activateAction(action);

            isTouched = false;  //делаем так, чтобы действие не выполнилось ещё раз
        }
    }


    public void dispose(){

    }
}
