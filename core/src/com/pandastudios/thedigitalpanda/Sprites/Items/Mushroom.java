package com.pandastudios.thedigitalpanda.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Tools.Manager;

public class Mushroom extends Item{



    public Mushroom(PlayScreen screen, float x, float y, Manager manager) {
        super(screen, x, y, manager);
        setRegion(screen.getAtlas().findRegion("Panda_Mushroom"), 0, 0, 200,178);
        velocity = new Vector2(0.7f,0);
    }

    @Override
    public void defineItem() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(),getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        //create shrrom hitbox
        PolygonShape box = new PolygonShape();
        box.setAsBox(4/PandaBros.PPM,4/PandaBros.PPM);
        FixtureDef fDef = new FixtureDef();
        fDef.filter.categoryBits = PandaBros.ITEM_BIT;
        fDef.filter.maskBits = PandaBros.PANDA_BIT|
                PandaBros.OBJECT_BIT|
                PandaBros.GROUND_BIT|
                PandaBros.COIN_BIT|
                PandaBros.BRICK_BIT;

        fDef.shape = box;
        body.createFixture(fDef).setUserData(this);

    }

    @Override
    public void use(Panda panda) {
        destroy();
        manager.aManager.get(manager.powerup).play();
        panda.grow();


    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
