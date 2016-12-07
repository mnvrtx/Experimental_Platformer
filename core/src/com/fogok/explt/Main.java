package com.fogok.explt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fogok.explt.core.Controller;
import com.fogok.explt.core.Handler;
import com.fogok.explt.core.SoundCore;
import com.fogok.explt.core.StartScreen;
import com.fogok.explt.objects.StoryNarrator;
import com.fogok.explt.objects.UI;
import com.fogok.explt.utils.Prefers;
import com.fogok.explt.utils.ResHelper;

public class Main extends ApplicationAdapter {

    public static boolean ISPC;

    public static float WIDTH, HEIGHT, mdT, dgnlCff;

    private BitmapFont bf;
    private SpriteBatch batch, backBatch, controllerBatch;
    private static OrthographicCamera camera, physicCamera;

    public static String DEBUG_VALUE1 = ""
                       , DEBUG_VALUE2 = "";

    private Handler handler;
    private StartScreen startScreen;
    private Sprite back;


	@Override
	public void create () {
        //initNatives
		batch = new SpriteBatch();
        backBatch = new SpriteBatch(1);
        backBatch.disableBlending();
        bf = new BitmapFont();
		controllerBatch = new SpriteBatch();
        back = new Sprite(new Texture("back.png"));
        float sizeWhitePartsInTexture = 128;
        ResHelper.setParams(2f, 0.5f, sizeWhitePartsInTexture);
        back.setBounds(ResHelper.getX(), ResHelper.getY(), ResHelper.getW(), ResHelper.getH());
        initCamera();
        Prefers.initPrefs();
        SoundCore.init();
        UI.initializate();

        Prefers.putInt(Prefers.KeySavePoint, 0);
        Prefers.putInt(Prefers.KeyStateCube, 0);
        Prefers.putInt(Prefers.KeyStateStory, 0);

        StoryNarrator.init();
        ///

        //initCore
        handler = new Handler();
        startScreen = new StartScreen(handler.getAtlasLoader());
        ///
	}

	@Override
	public void render () {
        //natives
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




        if (startScreen.getStart()){
            if (StoryNarrator.getCurrentStory() < 40){
                backBatch.begin();
                back.draw(backBatch);
                backBatch.end();
                ///
                physicCamera.update();
                batch.setProjectionMatrix(physicCamera.combined);
                batch.begin();
                handler.handle(batch, controllerBatch);
                batch.end();
                ///
                controllerBatch.begin();
                Controller.drawControl(controllerBatch, handler.getIsDrawLeftRight(), handler.getIsDrawUp());
                if (!startScreen.isEndedH())
                    startScreen.draw(controllerBatch);
            }else{

                controllerBatch.begin();
            }
        }
        else{
            controllerBatch.begin();
            startScreen.draw(controllerBatch);
        }
        StoryNarrator.drawAndNarrate(controllerBatch);
        controllerBatch.end();


        mdT = Math.min(Gdx.graphics.getDeltaTime() / 0.016f, 2.5f);
//        debugMethod1();
        ///
	}

//    @Override
//    public void resize(int width, int height) {
////        if (ISPC){
//            super.resize(width, height);
//            initCamera();
////        }
//    }


    float aspectR;
    private void initCamera(){
        aspectR =  (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
//        camera = new OrthographicCamera(20f, 20f * aspectR);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        camera.setToOrtho(false);
//        camera.update();
        physicCamera = new OrthographicCamera(20, 20f * aspectR);

//        dgnlCff = 981f / (float) (Math.sqrt(Math.pow(Gdx.graphics.getWidth(), 2) + Math.pow(Gdx.graphics.getHeight(), 2))); ///981

        WIDTH = physicCamera.viewportWidth;
        HEIGHT = physicCamera.viewportHeight;
        dgnlCff = 1f;
    }

    public static OrthographicCamera getCamera(){
        return camera;
    }

    public static OrthographicCamera getPhysicCamera(){
        return physicCamera;
    }


//    private float fpsIt, fpsMax = 10f;
//    private int fps;
//    private void debugMethod1(){
//        controllerBatch.begin();
//
//        fpsIt += 1f * mdT;
//        if (fpsIt > fpsMax){
//            fpsIt = 0f;
//            fps = (int) (1f / Gdx.graphics.getDeltaTime());
//        }
//        bf.draw(controllerBatch, "Reporting\n-------------\n".concat("\n-------------\n").concat(
//                "FPS:").concat(String.valueOf(fps)).concat("\n").concat(
//                DEBUG_VALUE1).concat("\n").concat(
//                DEBUG_VALUE2),
//
//                60, 250);
//
//        controllerBatch.end();
////        debugValueChanger.draw();
//    }

    @Override
    public void dispose() {
        Controller.dispose();
        SoundCore.disposeAll();
        StoryNarrator.dispose();
        startScreen.dispose();
        batch.dispose();
        backBatch.dispose();
        controllerBatch.dispose();
        handler.dispose();
        back.getTexture().dispose();
    }
}
