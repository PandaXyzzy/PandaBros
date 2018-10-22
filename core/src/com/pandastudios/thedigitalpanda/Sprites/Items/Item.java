package com.pandastudios.thedigitalpanda.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Tools.Manager;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean isDestroyed;
    protected Body body;
    protected Manager manager;


    public Item(PlayScreen screen, float x, float y, Manager manager){
        this.screen = screen;
        this.manager = manager;
        this.world = screen.getWorld();
        setPosition(x,y);
        setBounds(getX(), getY(), 16/PandaBros.PPM, 16/PandaBros.PPM);
        defineItem();
        toDestroy=false;
        isDestroyed = false;
    }

    public abstract void defineItem();
    public abstract void use(Panda panda);
    public void destroy(){toDestroy = true;}

    public void update(float dt){
        if (toDestroy && !isDestroyed){
            world.destroyBody(body);
            isDestroyed=true;
        }
    }
    public void reverseVelocity(boolean x, boolean y){
            if (x)
                velocity.x=-velocity.x;
            if (y)
                velocity.y=-velocity.y;
    }

    public void draw(Batch batch){if (!isDestroyed)super.draw(batch);}



}
