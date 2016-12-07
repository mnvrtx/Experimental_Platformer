package com.fogok.explt.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fogok.explt.Main;
import com.fogok.explt.core.AtlasLoader;
import com.fogok.explt.core.Controller;
import com.fogok.explt.core.SoundCore;
import com.fogok.explt.core.TiledMapDrawer;
import com.fogok.explt.utils.CollisionDetector;
import com.fogok.explt.utils.GMUtils;
import com.fogok.explt.utils.Prefers;

import java.util.Random;

/**
 * Created by FOGOK on 01.12.2016 14:00.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class Player {

    private Sprite player, blick;
    private Body body;
    private BodyDef bodyDef;
    private FixtureDef[] fixtureDef = new FixtureDef[2];


    private int POWERS;


    private Rectangle[] deadRectangles;
    private Rectangle[] powerRectangles;
    private Rectangle[] savesRectangles;
    private Rectangle[] storyRectangles;
    private Boolean[] powersCollected;
    private World world;

    private Sprite whiteScreen;

    private AtlasLoader atlasLoader;
    public Player(AtlasLoader atlasLoader, World world, TiledMapDrawer tiledMapDrawer) {
        deadRectangles = tiledMapDrawer.getDeadRectangles();
        powerRectangles = tiledMapDrawer.getPowerRectangles();
        savesRectangles = tiledMapDrawer.getSavesRectangles();
        powersCollected = tiledMapDrawer.getPowersCollected();
        storyRectangles = tiledMapDrawer.getStoryRectangles();

        this.atlasLoader = atlasLoader;

        this.world = world;



        player = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_MAIN_CUBE));
        player.setBounds(TiledMapDrawer.SPAWNPOINT.getX(), TiledMapDrawer.SPAWNPOINT.getY(), TiledMapDrawer.ONECUBESIZE, TiledMapDrawer.ONECUBESIZE);

        blick = new Sprite(atlasLoader.getTG(AtlasLoader.OBJ_BLICK));
        blick.setSize(TiledMapDrawer.ONECUBESIZE * 4f, TiledMapDrawer.ONECUBESIZE * 4f);
        blick.setOriginCenter();


        createBox2D(world);
        POWERS = Prefers.getInt(Prefers.KeyStateCube);

        for (int i = 0; i < 3; i++) {
            powersCollected[i] = (POWERS >= (i + 1));
        }
        completeDraw();
        createWhiteScreen();
    }

    private void createWhiteScreen(){
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        whiteScreen = new Sprite(new Texture(pixmap));
        pixmap.dispose();
    }

    private float bodyDensity;
    private boolean playSoundCollision = true;
    private void createBox2D(World world){
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getX(), player.getY());
        body = world.createBody(bodyDef);

        PolygonShape[] pShapes = new PolygonShape[2];

        final float[] verts1 = new float[] {0f, 0f, 0f, player.getHeight(), player.getWidth(), 0f};
        final float[] verts2 = new float[] {0f, player.getHeight(), player.getWidth(), player.getHeight(), player.getWidth(), 0f};
        for (int i = 0; i < 2; i++) {
            fixtureDef[i] = new FixtureDef();

            pShapes[i] = new PolygonShape();
            pShapes[i].set(i == 0 ? verts1 : verts2);

            fixtureDef[i].shape = pShapes[i];              //3m^2
            bodyDensity = 4f / (float) Math.pow(getSize(), 2d);


            fixtureDef[i].density = bodyDensity / 2f;
            fixtureDef[i].restitution = 0.5f;
            fixtureDef[i].friction = 0.2f;
        }


        
        body.createFixture(fixtureDef[0]);
        body.createFixture(fixtureDef[1]);
        body.setUserData(1);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (body.getLinearVelocity().y < 0f || body.getLinearVelocity().y == 0f)
                    Controller.setIsTrueJump();

                playSoundCollision = true;
            }

            @Override
            public void endContact(Contact contact) {
                if (playSoundCollision) {
                    playSoundCollision = false;
//                    SoundCore.playSound(SoundCore.Sounds.Stone, false);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        iterssDead = iterssMax;
//        float densFill = body.getFixtureList().get(0).getDensity() + body.getFixtureList().get(1).getDensity();
//        Main.DEBUG_VALUE2 = "" + getSize() + " " + densFill;
    }

    private Random rnd = new Random();
    private float gravIt, gravMax = (float) rnd.nextInt(8) + 2f;
    private boolean isNormalGrav = true;

    private float iterssDead, iterssMax = 0.7f;

    private float changControlIt, changControlMax = (float) rnd.nextInt(8) + 2f;
    private boolean isNormalControl = true;

    private float lockJumpIt, lockJumpMax = (float) rnd.nextInt(8) + 2f;
    private boolean isLockJump = false;


    private Vector2 gravityDown = new Vector2(0f, -9.81f);
    private Vector2 gravityUp = new Vector2(0f, 9.81f);
    private void changeState(){
        if (POWERS > 0){
            gravIt += Gdx.graphics.getDeltaTime();
            if (isNormalGrav){
                if (gravIt > gravMax){
                    isNormalGrav = false;
                    gravMax = (float) rnd.nextInt(8) + 2f;
                    gravIt = 0f;
                }
            }else {
                if (gravIt > 0.5f){
                    isNormalGrav = true;
                    gravIt = 0f;
                }
            }

            world.setGravity(isNormalGrav ? gravityDown : gravityUp);
        }

        if (POWERS > 1){
            changControlIt += Gdx.graphics.getDeltaTime();
            if (isNormalControl){
                if (changControlIt > changControlMax){
                    isNormalControl = false;
                    changControlMax = (float) rnd.nextInt(8) + 2f;
                    changControlIt = 0f;
                }
            }else {
                if (changControlIt > 1.4f){
                    isNormalControl = true;
                    changControlIt = 0f;
                }
            }
        }

        if (POWERS > 2){
            
            lockJumpIt += Gdx.graphics.getDeltaTime();
            if (!isLockJump){
                if (lockJumpIt > lockJumpMax){
                    isLockJump = true;
                    lockJumpMax = (float) rnd.nextInt(8) + 2f;
                    lockJumpIt = 0f;
                }
            }else {
                if (lockJumpIt > 3f){
                    isLockJump = false;
                    lockJumpIt = 0f;
                }
            }
        }
    }

    public void draw(SpriteBatch batch, SpriteBatch whiteScreenBatch){
        if (isDead)
            refresh();
        else{
            handleDeadRectangles();
            handleSavePoints();
            handlePowers();
        }

        changeState();

        player.setPosition(body.getPosition().x, body.getPosition().y);
        player.setOrigin(0f, 0f);
        player.setRotation((int) Math.toDegrees((double) body.getAngle()));
        player.draw(batch);


        if (!isDead){
            drawBlicks(batch);
            calculateDrawingControl();
            calculateStoryNarr();
            handleControl();
        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.J))
//            isPutWhiteScreen = true;

        if (isPutWhiteScreen){
            batch.end();
            whiteScreenBatch.begin();
            drawWhiteScreen(whiteScreenBatch);
            whiteScreenBatch.end();
            batch.begin();
        }
    }

    private void calculateStoryNarr(){
        float getX =  player.getX() + GMUtils.getNextX(getSize() / 2f, 45 + (int) player.getRotation()),
                getY =  player.getY() + GMUtils.getNextY(getSize() / 2f, 45 + (int) player.getRotation());
        for (int i = 0; i < storyRectangles.length; i++) {
            if (CollisionDetector.isTouchDot(getX, getY, storyRectangles[i])){
                switch (i){
                    case 0:
                        StoryNarrator.setStory(2);
                        break;
                    case 1:
                        StoryNarrator.setStory(4);
                        break;
                    case 2:
                        StoryNarrator.setStory(8);
                        break;
                }
                storyRectangles[i].setY(4590f);
            }
        }
    }

    private void calculateDrawingControl(){
        final int currentStory = StoryNarrator.getCurrentStory();
        isDrawLeftRight = (currentStory > 15 && currentStory < 28) || (currentStory > 30 && currentStory < 40);
        isDrawUp = (currentStory > 19 && currentStory < 28) || (currentStory > 30 && currentStory < 40);
    }
    private boolean isDrawLeftRight;
    private boolean isDrawUp;

    public boolean getIsDrawLeftRight(){
        return isDrawLeftRight;
    }

    public boolean getIsDrawUp(){
        return isDrawUp;
    }




    private boolean isPutWhiteScreen;
    private float itersWhiteScreen = 1f;
    private void drawWhiteScreen(SpriteBatch batch){
        float alpha = GMUtils.normalizeOneZero(itersWhiteScreen);
        whiteScreen.setAlpha(alpha);
        whiteScreen.draw(batch);

        itersWhiteScreen -= Gdx.graphics.getDeltaTime();
        if (itersWhiteScreen < 0f){
            isPutWhiteScreen = false;
            itersWhiteScreen = 1f;
        }
    }


    private float rotation1 = rnd.nextInt(360), rotation2 = rnd.nextInt(360), rotation3 = rnd.nextInt(360);
    private void drawBlicks(SpriteBatch batch){
        final float rotationSpeed = 0.2f;
        if (POWERS > 0 && !isNormalGrav){
            final float blcX = player.getX() + GMUtils.getNextX(getSize() * 0.258f, 43 + (int) player.getRotation()),
                    blcY = player.getY() + GMUtils.getNextY(getSize() * 0.258f, 43 + (int) player.getRotation());


//            blick.setAlpha(1f);
            blick.setColor(Color.GREEN);
//            blick.setAlpha(0.1f);
            blick.setPosition(blcX - blick.getWidth() / 2f, blcY - blick.getHeight() / 2f);
            rotation1 += rotationSpeed * Main.mdT;
            blick.setRotation(rotation1);
            blick.setScale(0.12f);
            blick.setAlpha(0.6f);
            blick.draw(batch);


        }
        if (POWERS > 1 && !isNormalControl){
            final float blcX = player.getX() + GMUtils.getNextX(getSize() * 1.11f, 46 + (int) player.getRotation()),
                    blcY = player.getY() + GMUtils.getNextY(getSize() * 1.11f, 46 + (int) player.getRotation());

//            blick.setAlpha(1f);
            blick.setColor(Color.RED);
//            blick.setAlpha(0.1f);
            rotation2 -= rotationSpeed * Main.mdT;
            blick.setRotation(rotation2);
            blick.setPosition(blcX - blick.getWidth() / 2f, blcY - blick.getHeight() / 2f);
            blick.setScale(0.35f);
            blick.setAlpha(0.6f);
            blick.draw(batch);
        }
        if (POWERS > 2 && isLockJump){
            final float blcX = player.getX() + GMUtils.getNextX(getSize() * 0.688f, 62 + (int) player.getRotation()),
                    blcY = player.getY() + GMUtils.getNextY(getSize() * 0.688f, 62 + (int) player.getRotation());

//            blick.setAlpha(1f);
            blick.setColor(Color.BLUE);
//            blick.setAlpha(0.1f);
            rotation3 += rotationSpeed * Main.mdT * 2;
            blick.setRotation(rotation3);
            blick.setPosition(blcX - blick.getWidth() / 2f, blcY - blick.getHeight() / 2f);
            blick.setScale(0.7f);
            blick.setAlpha(0.6f);
            blick.draw(batch);
        }

    }

    private float itersDead = 0f, itersDeadMax = 0.5f;
    private void refresh(){
        itersDead += Gdx.graphics.getDeltaTime();
        if (itersDead > itersDeadMax){
            isDead = false;
            itersDead = 0f;
            player.setScale(1f);
            body.setLinearVelocity(0f, 0f);
            body.setTransform(TiledMapDrawer.SPAWNPOINT.getX(), TiledMapDrawer.SPAWNPOINT.getY(), 0f);

            player.setPosition(body.getPosition().x, body.getPosition().y);
            final float posX = player.getX() + GMUtils.getNextX(getSize() / 2f, 45 + getRotation()),
                    posY = player.getY() + GMUtils.getNextY(getSize() / 2f, 45 + getRotation());

            Main.getPhysicCamera().position.set(posX, posY, 0);

            Controller.clearJump();

            iterssDead = iterssMax;

            StoryNarrator.setStory(3);

            SoundCore.playSound(SoundCore.Sounds.Restart);

            isPutWhiteScreen = true;
        }else{
            player.setScale(1f - itersDead / itersDeadMax);
            body.setLinearVelocity(0f, 0f);
        }
    }

    private void completeDraw(){
        switch (POWERS){
            case 1:
                player.setRegion(atlasLoader.getTG(AtlasLoader.OBJ_MAIN_CUBE1));
                break;
            case 2:
                player.setRegion(atlasLoader.getTG(AtlasLoader.OBJ_MAIN_CUBE2));
                break;
            case 3:
                player.setRegion(atlasLoader.getTG(AtlasLoader.OBJ_MAIN_CUBE3));
                break;
            default:

                break;
        }
    }

    private boolean isDead;
    private void handleDeadRectangles(){
        float getX =  player.getX() + GMUtils.getNextX(getSize() / 2f, 45 + (int) player.getRotation()),
                getY =  player.getY() + GMUtils.getNextY(getSize() / 2f, 45 + (int) player.getRotation());
        for (int i = 0; i < deadRectangles.length; i++) {
            if (CollisionDetector.isTouchDot(getX, getY, deadRectangles[i])){
                isDead = true;
            }
        }
    }

    private void handleSavePoints(){
        float getX =  player.getX() + GMUtils.getNextX(getSize() / 2f, 45 + (int) player.getRotation()),
                getY =  player.getY() + GMUtils.getNextY(getSize() / 2f, 45 + (int) player.getRotation());
        for (int i = 1; i < savesRectangles.length + 1; i++) {
            if (CollisionDetector.isTouchDot(getX, getY, savesRectangles[i - 1])){
                if (i != 6){
                    if (TiledMapDrawer.SPAWNPNT != i) {
                        TiledMapDrawer.SPAWNPNT = i;
                        Prefers.putInt(Prefers.KeySavePoint, i);
                        Prefers.putInt(Prefers.KeyStateCube, POWERS);
                        isPutWhiteScreen = true;
                        TiledMapDrawer.refreshSpawnPoint();
                        SoundCore.playSound(SoundCore.Sounds.Restart);
                    }
                }else{
                    StoryNarrator.setStory(9);
                }
            }
        }
    }

    private void handlePowers(){
        float getX =  player.getX() + GMUtils.getNextX(getSize() / 2f, 45 + (int) player.getRotation()),
                getY =  player.getY() + GMUtils.getNextY(getSize() / 2f, 45 + (int) player.getRotation());

        for (int i = 1; i < powerRectangles.length + 1; i++) {
            if (CollisionDetector.isTouchDot(getX, getY, powerRectangles[i - 1])){
                if (POWERS != i){
                    POWERS = i;
                    powersCollected[i - 1] = true;
                    completeDraw();
                    switch (i - 1){
                        case 0:
                            StoryNarrator.setStory(5);
                            break;
                        case 1:
                            StoryNarrator.setStory(6);
                            break;
                        case 2:
                            StoryNarrator.setStory(7);
                            break;
                    }
                }
            }
        }
    }



    private float velX;
    private final float velVelSpeed = 0.3f;
    private void handleControl(){
        if (iterssDead == 0f){

            if (Controller.getIsMove()){

                if (isNormalControl != Controller.getLeft()){
                    if (velX < 10f * (1f / Math.sqrt(bodyDensity)))
                        velX += velVelSpeed;
                }
                else{
                    if (velX > -10f * (1f / Math.sqrt(bodyDensity)))
                        velX -= velVelSpeed;
                }

                body.setLinearVelocity(velX, body.getLinearVelocity().y);
            }else{
                velX = body.getLinearVelocity().x;
            }

            if (StoryNarrator.getCurrentStory() == 20)
                Controller.testJump();

            if ((Controller.getJump()) && !isLockJump){
                Controller.clearJump();
//            body.setAngularVelocity(-20f);
                float velYBody = body.getLinearVelocity().y;
                if (velYBody < 0f)
                    velYBody = 0f;
                body.applyLinearImpulse(0f,
                        12 - velYBody,
                        body.getPosition().x + player.getWidth() / 2f, body.getPosition().y + player.getWidth() / 2f, true);
            }

            if (isLockJump)
                Controller.clearJump();

            if (body.getAngularVelocity() > 10f * getSize())
                body.setAngularVelocity(10f * getSize());
        }else{
            iterssDead -= Gdx.graphics.getDeltaTime();
            iterssDead = GMUtils.normalizeOneZero(iterssDead);
        }

    }

    public float getX(){
        return player.getX();
    }

    public float getY(){
        return player.getY();
    }

    public float getSize(){
        return player.getWidth();
    }


    public int getRotation(){
        return (int) player.getRotation();
    }


    public void dispose() {
        whiteScreen.getTexture().dispose();
    }
}
