package com.pandastudios.thedigitalpanda.Sprites.Enemies;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.pandastudios.thedigitalpanda.Scenes.Hud;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Sprites.Enemies.Enemy;
import com.pandastudios.thedigitalpanda.Tools.Manager;


public class Goomba extends Enemy {
    private float stateTime;
    private Animation <TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Manager manager;
    float angle;




    public  Goomba(PlayScreen screen, float x, float y, Manager manager){
        super(screen, x, y);
        this.manager = manager;


        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 12 / PandaBros.PPM, 12 / PandaBros.PPM);

        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2Body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32,0,16,16));
            stateTime = 0;
        }
        else if (!destroyed) {
            b2Body.setLinearVelocity(velocity);
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(),getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        //create goomba hitbox
        PolygonShape box = new PolygonShape();
        box.setAsBox(4/PandaBros.PPM,4/PandaBros.PPM);


        FixtureDef fDef = new FixtureDef();
        fDef.filter.categoryBits = PandaBros.ENEMY_BIT;
        fDef.filter.maskBits = PandaBros.GROUND_BIT |
                PandaBros.BRICK_BIT |
                PandaBros.COIN_BIT |
                PandaBros.ENEMY_BIT |
                PandaBros.OBJECT_BIT |
                PandaBros.PANDA_BIT;

        fDef.shape = box;
        b2Body.createFixture(fDef).setUserData(this);

        //create head sensor
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2((float)-2.5, 8).scl(1/PandaBros.PPM);
        vertice[1] = new Vector2(3, 8).scl(1/PandaBros.PPM);
        vertice[2] = new Vector2(2, 3).scl(1/PandaBros.PPM);
        vertice[3] = new Vector2(2, 3).scl(1/PandaBros.PPM);
        head.set(vertice);

        fDef.shape = head;
        fDef.restitution = 3f;
        fDef.filter.categoryBits = PandaBros.ENEMY_HEAD_BIT;
        b2Body.createFixture(fDef).setUserData(this);

    }

    public void draw(Batch batch){
        if (!destroyed || stateTime <1)
            super.draw(batch);
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        manager.aManager.get(manager.stomp).play();
        Hud.addScore(1000);
    }
}
