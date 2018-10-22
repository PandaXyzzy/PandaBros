package com.pandastudios.thedigitalpanda.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.Sprites.TileObjects.Brick;
import com.pandastudios.thedigitalpanda.Sprites.TileObjects.Coin;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Sprites.Enemies.Goomba;

public class B2WorldCreator {
    private Array<Goomba> goombas;


    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public B2WorldCreator(PlayScreen screen, Manager manager){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2) / PandaBros.PPM, (rect.getY() + rect.getHeight() /2) / PandaBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() /2 / PandaBros.PPM, rect.getHeight() /2 / PandaBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() /2) / PandaBros.PPM, (rect.getY() + rect.getHeight() /2) / PandaBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() /2 / PandaBros.PPM, rect.getHeight() /2 / PandaBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PandaBros.OBJECT_BIT;
            body.createFixture(fdef);
        }

        //create brick bodies
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen, object, manager);
        }

        //create coin bodies
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object, manager);
        }

        //create all goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen, rect.getX() / PandaBros.PPM, rect.getY() / PandaBros.PPM, manager));
        }
    }
}
