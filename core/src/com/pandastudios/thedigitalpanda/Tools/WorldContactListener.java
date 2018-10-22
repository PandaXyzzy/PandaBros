package com.pandastudios.thedigitalpanda.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pandastudios.thedigitalpanda.Sprites.Items.Item;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Sprites.Enemies.Enemy;
import com.pandastudios.thedigitalpanda.Sprites.TileObjects.InteractiveTileObject;
import com.pandastudios.thedigitalpanda.PandaBros;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch (cDef){
            case PandaBros.PANDA_HEAD_BIT | PandaBros.BRICK_BIT:
            case PandaBros.PANDA_HEAD_BIT | PandaBros.COIN_BIT:
                if (fixA.getFilterData().categoryBits == PandaBros.PANDA_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Panda) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Panda) fixB.getUserData());
                break;
            case PandaBros.ENEMY_HEAD_BIT | PandaBros.PANDA_BIT:
                if (fixA.getFilterData().categoryBits == PandaBros.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case PandaBros.ENEMY_BIT | PandaBros.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == PandaBros.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case PandaBros.PANDA_BIT | PandaBros.ENEMY_BIT://<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (fixA.getFilterData().categoryBits == PandaBros.PANDA_BIT)
                    ((Panda) fixA.getUserData()).hit();
                else
                    ((Panda) fixB.getUserData()).hit();
                break;
            case PandaBros.ENEMY_BIT | PandaBros.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PandaBros.ITEM_BIT | PandaBros.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == PandaBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else ((Item)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case PandaBros.ITEM_BIT | PandaBros.PANDA_BIT:
                if (fixA.getFilterData().categoryBits == PandaBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Panda) fixB.getUserData());
                else ((Item)fixB.getUserData()).use((Panda) fixA.getUserData());
                break;
            case PandaBros.ITEM_BIT | PandaBros.PANDA_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == PandaBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Panda) fixB.getUserData());
                else ((Item)fixB.getUserData()).use((Panda) fixA.getUserData());
                break;




        }

    }







    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
