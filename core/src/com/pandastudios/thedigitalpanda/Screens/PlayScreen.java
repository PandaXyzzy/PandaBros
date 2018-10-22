package com.pandastudios.thedigitalpanda.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pandastudios.thedigitalpanda.Scenes.Controller;
import com.pandastudios.thedigitalpanda.Scenes.Hud;
import com.pandastudios.thedigitalpanda.Sprites.Enemies.Enemy;
import com.pandastudios.thedigitalpanda.Sprites.Items.Item;
import com.pandastudios.thedigitalpanda.Sprites.Items.ItemDef;
import com.pandastudios.thedigitalpanda.Sprites.Items.Mushroom;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Tools.B2WorldCreator;
import com.pandastudios.thedigitalpanda.Tools.Manager;
import com.pandastudios.thedigitalpanda.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen{
    //ref to our game, used to set screens
    private PandaBros game;
    private Manager manager;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private Hud hud;
    private Controller controller;
    private Music music;

    //tiledmap variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Panda playerPanda;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;




///////////////////////////////////////////////////////////////////////////////////////////////////
//                                        constructor                                            //
///////////////////////////////////////////////////////////////////////////////////////////////////

    public PlayScreen(PandaBros game){

        atlas = new TextureAtlas("pandaPanda.atlas");

        this.manager = game.manager;

        this.game = game;
        gamecam = new OrthographicCamera();

        //create a fitviewport to maintain aspect ratio
        gameport = new FitViewport(PandaBros.V_WIDTH / PandaBros.PPM, PandaBros.V_HEIGHT / PandaBros.PPM, gamecam);
        //load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PandaBros.PPM);

        //initially set our gamecam to be centered at the start of game
        gamecam.position.set(gameport.getWorldWidth()/2, gameport.getWorldHeight()/2, 0);

        //create world , set no gravity in x and -10 gravity in y, and allow bodies to sleep
        world = new World(new Vector2(0, -10),true);
        //show debug lines
        b2dr = new Box2DDebugRenderer();


        creator = new B2WorldCreator(this, manager);
        //create mario, controller, hud, ect
        controller = new Controller();
        playerPanda = new Panda(this, manager);
        hud = new Hud(game.batch);

        world.setContactListener(new WorldContactListener());

        music = manager.aManager.get(manager.music);
        music.setLooping(true);
        //music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();






    }

///////////////////////////////////////////////////////////////////////////////////////////////////
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }
    public void handleSpawningItems(){
        if (!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y, manager));
            }
        }

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleinput(float dt){

        if(controller.isUpPressed() || controller.isbPressed()) {
            playerPanda.b2Body.applyLinearImpulse(new Vector2(0, 0.7f), playerPanda.b2Body.getWorldCenter(), true);
        }
        if(controller.isRightPressed() && playerPanda.b2Body.getLinearVelocity().x <=2) {
            playerPanda.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), playerPanda.b2Body.getWorldCenter(), true);
        }
        if(controller.isLeftPressed() && playerPanda.b2Body.getLinearVelocity().x >= -2) {
            playerPanda.b2Body.applyLinearImpulse(new Vector2( -0.1f, 0), playerPanda.b2Body.getWorldCenter(), true);
        }
    }


    public void update(float dt){
        handleinput(dt);
        handleSpawningItems();

        world.step(1/60f, 6,2);

        playerPanda.update(dt);
        for (Enemy enemy: creator.getGoombas()){
            enemy.update(dt);
                if(enemy.getX() < playerPanda.getX() +224/PandaBros.PPM)
                    enemy.b2Body.setActive(true);
        }

        for (Item item : items){item.update(dt);}

        hud.update(dt);

        gamecam.position.x = playerPanda.b2Body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {

        //separate our update logic from render
        update(delta);

        //clear the game screen with black
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our box2dDebuglines
        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin(); //start drawing

        playerPanda.draw(game.batch);
        for (Enemy enemy: creator.getGoombas()){enemy.draw(game.batch);}
        for (Item item : items){item.draw(game.batch);}

        game.batch.end();//stop drawing

        //Set batch to draw what Hud cam sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();

    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
        controller.resize(width, height);
    }
    public TiledMap getMap() {
        return map;
    }
    public World getWorld() {
        return world;
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
