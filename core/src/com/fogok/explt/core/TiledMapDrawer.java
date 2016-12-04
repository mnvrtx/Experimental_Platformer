package com.fogok.explt.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fogok.explt.Main;
import com.fogok.explt.utils.Pos;
import com.fogok.explt.utils.Prefers;

/**
 * Created by FOGOK on 01.12.2016 11:59.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class TiledMapDrawer {

    private OrthogonalTiledMapRenderer tiledMapRenderer;
//    private ShapeRenderer shapeRenderer;
    private Rectangle mapObjects[];
    private Rectangle deadRectangles[];
    private Rectangle savesRectangles[];
    private Rectangle powerRectangles[];

    private Boolean powersCollected[] = new Boolean[]{false, false, false};

    public static Pos SPAWNPOINT;
    public static float ONECUBESIZE;

    private Body[] barrierBodies;
    private Polygon[] barriers;
    private float invFinCff;

    private static float finCff;
    public TiledMapDrawer(World world) {

        TmxMapLoader loader = new TmxMapLoader();


        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
//        params.textureMinFilter = Texture.TextureFilter.Linear;
//        params.textureMagFilter = Texture.TextureFilter.Linear;



        TiledMap tiledMap = loader.load("map.tmx");

//        shapeRenderer = new ShapeRenderer();
//        final float finCff = 20f / Gdx.graphics.getWidth() / 2f;
        finCff = 20f / Gdx.graphics.getWidth() / 2f;
        invFinCff = 1f / finCff;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 0.5f);
        tiledMapRenderer.getBatch().disableBlending();

        MapLayer collisionObjectLayer = tiledMap.getLayers().get("l2");
        MapObjects objects = collisionObjectLayer.getObjects();
        int maxMapObjects = 0, maxDeadRectangleObjects = 0, maxSavesRectangles = 0, maxPowerRectangles = 0;
        for (int i = 0; i < objects.getCount(); i++) {
            if (objects.get(i).getName() == null)
                maxMapObjects++;
            else if (objects.get(i).getName().equals("d"))
                maxDeadRectangleObjects++;
            else if (objects.get(i).getName().equals("s"))
                maxSavesRectangles++;
            else if (objects.get(i).getName().equals("q"))
                maxPowerRectangles++;
        }
        mapObjects = new Rectangle[maxMapObjects];
        deadRectangles = new Rectangle[maxDeadRectangleObjects];
        savesRectangles = new Rectangle[maxSavesRectangles];
        powerRectangles = new Rectangle[maxPowerRectangles];
        int itersMap = 0, itersDead = 0, itersSaves = 0, itersPowers = 0;
        for (int i = 0; i < objects.getCount(); i++) {
            if (objects.get(i).getName() == null){
                mapObjects[itersMap] = ((RectangleMapObject) objects.get(i)).getRectangle();
                mapObjects[itersMap].set(mapObjects[itersMap].x * finCff, mapObjects[itersMap].y * finCff, mapObjects[itersMap].width * finCff, mapObjects[itersMap].height * finCff);
                itersMap++;
            }else if (objects.get(i).getName().equals("d")){
                deadRectangles[itersDead] = ((RectangleMapObject) objects.get(i)).getRectangle();
                deadRectangles[itersDead].set(deadRectangles[itersDead].x * finCff, deadRectangles[itersDead].y * finCff, deadRectangles[itersDead].width * finCff, deadRectangles[itersDead].height * finCff);
                itersDead++;
            }else if (objects.get(i).getName().equals("s")){
                savesRectangles[itersSaves] = ((RectangleMapObject) objects.get(i)).getRectangle();
                savesRectangles[itersSaves].set(savesRectangles[itersSaves].x * finCff, savesRectangles[itersSaves].y * finCff, savesRectangles[itersSaves].width * finCff, savesRectangles[itersSaves].height * finCff);
                itersSaves++;
            }else if (objects.get(i).getName().equals("q")){
                powerRectangles[itersPowers] = ((RectangleMapObject) objects.get(i)).getRectangle();
                powerRectangles[itersPowers].set(powerRectangles[itersPowers].x * finCff, powerRectangles[itersPowers].y * finCff, powerRectangles[itersPowers].width * finCff, powerRectangles[itersPowers].height * finCff);
                itersPowers++;
            }
        }


        barriers = new Polygon[mapObjects.length];
        for (int i = 0; i < mapObjects.length; i++) {
            barriers[i] = new Polygon(new float[] { this.mapObjects[i].width, 0f,
                    this.mapObjects[i].width, this.mapObjects[i].height, 0f, this.mapObjects[i].height, 0f, 0f } );
            barriers[i].setPosition(this.mapObjects[i].x, this.mapObjects[i].y);
        }

        createBarriersBox2D(world);

//        mapTiles = (TiledMapTileLayer) tiledMap.getLayers().get("l1");
//        item1 = new TiledMapTileLayer.Cell();
//        item2 = new TiledMapTileLayer.Cell();
//        item3 = new TiledMapTileLayer.Cell();
//        noItem = new TiledMapTileLayer.Cell();

//        item1.setTile(tiledMap.getTileSets().getTile(16));
//        item2.setTile(tiledMap.getTileSets().getTile(17));
//        item3.setTile(tiledMap.getTileSets().getTile(18));
//        noItem.setTile(tiledMap.getTileSets().getTile(7));

        SPAWNPNT = Prefers.getInt(Prefers.KeySavePoint);
        refreshSpawnPoint();
        ONECUBESIZE = 90 * finCff;
    }

    private static final Pos spawnPonints[] = new Pos[] {new Pos(450, 4230), new Pos(25020, 3420), new Pos(540, 2430), new Pos(25020, 1350), new Pos(25290, 630)};

    public static int SPAWNPNT;
    public static void refreshSpawnPoint(){
        SPAWNPOINT = new Pos(spawnPonints[SPAWNPNT].getX() * finCff, spawnPonints[SPAWNPNT].getY() * finCff);
    }

    private void createBarriersBox2D(final World world){
        //box2d
        BodyDef barrierDef = new BodyDef();
        barrierDef.type = BodyDef.BodyType.StaticBody;

        barrierBodies = new Body[barriers.length + 2];

        for (int i = 0; i < barriers.length; i++) {
            barrierBodies[i] = world.createBody(barrierDef);

            FixtureDef fDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            float[] g;

                g = new float[barriers[i].getTransformedVertices().length];
                for (int qi2 = 0; qi2 < g.length; qi2++)
                    g[qi2] = barriers[i].getTransformedVertices()[qi2];



            shape.set(g);

            fDef.shape = shape;
            fDef.friction = 0.2f;
            barrierBodies[i].createFixture(fDef);
        }

    }



    public void draw(SpriteBatch batch){
        batch.end();
//        correctCollectedItems();
        Main.getCamera().position.set((int)(Main.getPhysicCamera().position.x * invFinCff * 0.5f), (int)(Main.getPhysicCamera().position.y * invFinCff * 0.5f), 0);
        Main.getCamera().update();
        tiledMapRenderer.setView(Main.getCamera());
        tiledMapRenderer.render();
//        drawRects();
        batch.begin();
    }


//    private TiledMapTileLayer.Cell item1, item2, item3, noItem;
//    private TiledMapTileLayer mapTiles;
//    private void correctCollectedItems(){
//        for (int i = 0; i < 3; i++) {
//            switch (i){
//                case 0:
//                    mapTiles.setCell(32, 32, powersCollected[0] ? item1 : noItem);
//                    break;
//                case 1:
//                    mapTiles.setCell(272, 19, powersCollected[1] ? item2 : noItem);
//                    break;
//                case 2:
//                    mapTiles.setCell(279, 6, powersCollected[2] ? item3 : noItem);
//                    break;
//            }
//        }
//    }

    public Boolean[] getPowersCollected() {
        return powersCollected;
    }

    public Rectangle[] getDeadRectangles() {
        return deadRectangles;
    }

    public Rectangle[] getPowerRectangles() {
        return powerRectangles;
    }

    public Rectangle[] getSavesRectangles() {
        return savesRectangles;
    }

    //    private void drawRects(){
//        shapeRenderer.setProjectionMatrix(Main.getCamera().combined);
//
////            Gdx.gl.glLineWidth(3f);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        for (int i = 0; i < mapObjects.length; i++) {
//            shapeRenderer.rect(mapObjects[i].x, mapObjects[i].y, mapObjects[i].width, mapObjects[i].height);
//        }
//        Main.DEBUG_VALUE2 = mapObjects[0].x + " " + mapObjects[0].y + " " +  mapObjects[0].width + " " +  mapObjects[0].height;
//        shapeRenderer.end();
//    }

    public void dispose() {
        SPAWNPOINT = null;
        tiledMapRenderer.getMap().dispose();
        tiledMapRenderer.dispose();
//        shapeRenderer.dispose();

    }
}
