package com.fogok.explt.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.fogok.explt.Main;
import com.fogok.explt.objects.Player;
import com.fogok.explt.utils.GMUtils;

import java.math.BigDecimal;

/**
 * Created by FOGOK on 01.12.2016 11:19.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class Handler {

    private AtlasLoader atlasLoader;
    private TiledMapDrawer tiledMapDrawer;
    private Player player;

    public static boolean PAUSE;

    private World world;
    private Box2DDebugRenderer dDebugRenderer;
    private boolean REFRESH;

    public Handler() {
        atlasLoader = new AtlasLoader(0);
        Controller.init(atlasLoader);
        world = new World(new Vector2(0f, -9.81f), false);
        tiledMapDrawer = new TiledMapDrawer(world);
        player = new Player(atlasLoader, world, tiledMapDrawer);
        dDebugRenderer = new Box2DDebugRenderer();
        REFRESH = true;
        PAUSE = false;
    }

    public void handle(SpriteBatch batch, SpriteBatch whiteScreenBatch){
        if (REFRESH){
            REFRESH = false;
            Main.getPhysicCamera().position.set(TiledMapDrawer.SPAWNPOINT.getX(), TiledMapDrawer.SPAWNPOINT.getY(), 0);
            Main.getPhysicCamera().update();
        }

        tiledMapDrawer.draw(batch);
        world.step(Gdx.graphics.getDeltaTime(), 8, 4);
        player.draw(batch, whiteScreenBatch);
        Controller.handlerControls();

//        batch.end();
//        dDebugRenderer.render(world, Main.getPhysicCamera().combined);
//        batch.begin();
        setPositionCamera();
//        Main.DEBUG_VALUE1 = world.getBodyCount() + "";
    }

//    private BigDecimal bigDecimal;
    public void setPositionCamera(){
//        Main.getCamera().position.set(
//                (bigDecimal = new BigDecimal(player.getX() + GMUtils.getNextX(player.getSize() / 2f, 45 + player.getRotation())).setScale(2, BigDecimal.ROUND_CEILING)).floatValue(),
//                (bigDecimal = new BigDecimal(player.getY() + GMUtils.getNextY(player.getSize() / 2f, 45 + player.getRotation())).setScale(2, BigDecimal.ROUND_CEILING)).floatValue(),
//                0);


        final float posX = player.getX() + GMUtils.getNextX(player.getSize() / 2f, 45 + player.getRotation()),
                posY = player.getY() + GMUtils.getNextY(player.getSize() / 2f, 45 + player.getRotation());

        final float lerp = 2f;
        Vector3 position = Main.getPhysicCamera().position;
        position.x += (posX - position.x) * lerp * Gdx.graphics.getDeltaTime();
        position.y += (posY - position.y) * lerp * Gdx.graphics.getDeltaTime();
    }

    public void dispose() {
        atlasLoader.dispose();
        tiledMapDrawer.dispose();
        player.dispose();
        world.dispose();
    }
}
