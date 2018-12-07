package com.pandastudios.thedigitalpanda.Sprites;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Sprites.Enemies.Enemy;
import com.pandastudios.thedigitalpanda.Sprites.Enemies.Turtle;
import com.pandastudios.thedigitalpanda.Tools.Manager;

public class Panda extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, RIP, WIN }
    public State currentState;
    private State previousState;
    public World world;
    public Body b2Body;
    public Manager manager;
    private float stateTime;
    private Animation<TextureRegion> pandaStand;
    private Animation<TextureRegion> pandaRun;
    private Animation<TextureRegion> pandaJump;
    private Animation<TextureRegion> pandaRIP;
    private TextureRegion bigPandaStand;
    private Animation<TextureRegion> bigPandaJump;
    private Animation<TextureRegion> bigPandaRun;
    private Animation<TextureRegion> growPanda;
    private float stateTimer;
    private boolean isRunningRight;
    public boolean isBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigPanda;
    private boolean timeToRedefinePanda;
    private boolean pandaIsRIP;
    private boolean pandaWins;
    private boolean timeToDefineGodMode;


////////////////////////////////////////////////////////////////////////////////////////////////

    public Panda(PlayScreen screen, Manager manager){
        this.world = screen.getWorld();
        this.manager = manager;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;

        //create texture array for animations
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //run animation
        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("walk"), i * 17, 0, 17, 22));
        pandaRun = new Animation(.1f, frames);
        frames.clear(); //clear for big run
        //bigrun
        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bigWalk"), i * 17, 0, 17, 34));
        bigPandaRun = new Animation(.1f, frames);
        frames.clear();//clear for jump
        //jump animation
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jump"), i * 17, 0, 17, 22));
        pandaJump = new Animation(0.2f, frames);
        frames.clear(); //clear for big jump
        //big jump
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jump"), i * 17, 0, 17, 22));
        bigPandaJump = new Animation(0.2f, frames);
        frames.clear(); //clear for grow
        //grow mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("grow"), 17, 0, 17, 34));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("grow"), 0, 0, 17, 34));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("grow"), 17, 0, 17, 34));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("grow"), 0, 0, 17, 34));
        growPanda = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();//clear for stand


        //create panda texture from atlas and tell where in the image he is
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("stand"), i * 17, 0, 17, 22));
        pandaStand = new Animation(.6f, frames);
        bigPandaStand = new TextureRegion(screen.getAtlas().findRegion("bigPandaStand"), 0, 0, 17, 34);
        frames.clear();//clear for RIP

        for (int i = 0; i < 7; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("RIP"), 0, 0, 17, 22));
        pandaRIP = new Animation(1f, frames);


        //give panda size and location stuff
        definePanda();
        setBounds(0,0 , 16 /PandaBros.PPM, 16 / PandaBros.PPM);
        setRegion(pandaStand.getKeyFrame(stateTimer,true));

    }

////////////////////////////////////////////////////////////////////////////////////////////////



    private State getState(){
        if (pandaIsRIP || b2Body.getPosition().y < 0)
            return State.RIP;
        else if (runGrowAnimation)
            return State.GROWING;
        else if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2Body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2Body.getLinearVelocity().x !=0)
            return State.RUNNING;
        else if (pandaWins)
            return State.WIN;
        else
            return State.STANDING;
    }

    private TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case RIP:
                region = pandaRIP.getKeyFrame(stateTimer);
                break;
            case GROWING:
                region = growPanda.getKeyFrame(stateTimer);
                if (growPanda.isAnimationFinished(stateTimer))
                    runGrowAnimation=false;
                break;
            case JUMPING:
                region = pandaJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = isBig? bigPandaRun.getKeyFrame(stateTimer, true) : pandaRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = isBig ? bigPandaStand : pandaStand.getKeyFrame(stateTimer, true);
                    break;
        }

        if((b2Body.getLinearVelocity().x <0 || !isRunningRight) && !region.isFlipX()){
            region.flip(true, false);
            isRunningRight = false;
        }

        else if ((b2Body.getLinearVelocity().x>0|| isRunningRight) && region.isFlipX()){
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////


    public void grow(){
        runGrowAnimation = true;
        isBig = true;
        timeToDefineBigPanda = true;
        setBounds(getX(), getY(), getWidth(), getHeight() + 12/PandaBros.PPM);
    }

    public void setPandaWins(){
        pandaWins = true;
    }

    public void hit(Enemy enemy){
        if (enemy instanceof Turtle && ((Turtle)enemy).currentState == Turtle.State.STANDING_SHELL){
            ((Turtle)enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        }else{
        if (isBig){ isBig = false;
        timeToDefineGodMode = true;
        manager.aManager.get(Manager.powerdown).play();
        setBounds(getX(), getY(), getWidth(), getHeight() - 12/PandaBros.PPM);
            System.out.println("panda got small");
        }
        //if panda is small
        else {die();}
        setRegion(pandaStand.getKeyFrame(stateTimer,true));
    }}

    public boolean isRIP(){return pandaIsRIP;}

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean isWon(){return pandaWins;}


    private void die() {
        if (!isRIP()) {
            manager.aManager.get(manager.music).stop();
            manager.aManager.get(Manager.RIP).play();
            pandaIsRIP = true;
            System.out.println("panda is RIP");

            //make panda stop collision
            Filter filter = new Filter();
            filter.maskBits = PandaBros.NOTHING_BIT;
            for (Fixture fixture : b2Body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            //make panda bump up
            b2Body.applyLinearImpulse(new Vector2(0, 2f), b2Body.getWorldCenter(), true);
        }
    }

    /////////////////////// stuff////////////////////////////////////////////////////////////////




    public void update(float dt) {
        stateTime += dt;
        setPosition(b2Body.getPosition().x - getWidth() / (float) 2, b2Body.getPosition().y - getHeight() / ((float) 2.7));
        setRegion(getFrame(dt));

        if (timeToDefineBigPanda) {defineBigPanda();}
        if (timeToDefineGodMode){stateTime = 0; godModePanda(); }
        if (timeToRedefinePanda){
            while (stateTime < 3){return; }

            {redefinePanda();}
        }
    }

    ///////////////////// Define methods //////////////////////////////////////////////////////////

    private void definePanda(){
    BodyDef bDef = new BodyDef();
    bDef.position.set(32 / PandaBros.PPM,32/PandaBros.PPM);
    bDef.type = BodyDef.BodyType.DynamicBody;
    b2Body = world.createBody(bDef);

    FixtureDef fDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(4/PandaBros.PPM, 4/PandaBros.PPM);
    fDef.filter.categoryBits = PandaBros.PANDA_BIT;
    fDef.filter.maskBits = PandaBros.GROUND_BIT |
            PandaBros.BRICK_BIT |
            PandaBros.COIN_BIT |
            PandaBros.ENEMY_BIT |
            PandaBros.OBJECT_BIT |
            PandaBros.WIN_BIT |
            PandaBros.ENEMY_HEAD_BIT|
            PandaBros.WIN_BIT |
            PandaBros.ITEM_BIT;

    fDef.shape = shape;
    b2Body.createFixture(fDef).setUserData(this);


    EdgeShape head = new EdgeShape(); //// change 6 to other number to figure out scale
    head.set(new Vector2(-2/PandaBros.PPM,4/ PandaBros.PPM), new Vector2(2/PandaBros.PPM, 4/ PandaBros.PPM));
    fDef.shape = head;
    fDef.filter.categoryBits = PandaBros.PANDA_HEAD_BIT;
    fDef.isSensor = true;

    b2Body.createFixture(fDef).setUserData(this);
    }

    private void defineBigPanda(){
        Vector2 currentPosition = b2Body.getPosition();
        world.destroyBody(b2Body);

        BodyDef bDef = new BodyDef();
        bDef.position.set(currentPosition);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4/PandaBros.PPM, 6/PandaBros.PPM);
        fDef.filter.categoryBits = PandaBros.PANDA_BIT;
        fDef.filter.maskBits = PandaBros.GROUND_BIT |
                PandaBros.BRICK_BIT |
                PandaBros.COIN_BIT |
                PandaBros.ENEMY_BIT |
                PandaBros.OBJECT_BIT |
                PandaBros.ENEMY_HEAD_BIT|
                PandaBros.WIN_BIT |
                PandaBros.ITEM_BIT;

        fDef.shape = shape;
        b2Body.createFixture(fDef).setUserData(this);

        EdgeShape head = new EdgeShape(); //// change 6 to other number to figure out scale
        head.set(new Vector2(-2/PandaBros.PPM,6/ PandaBros.PPM), new Vector2(2/PandaBros.PPM, 6/ PandaBros.PPM));
        fDef.shape = head;
        fDef.filter.categoryBits = PandaBros.PANDA_HEAD_BIT;
        fDef.isSensor = true;

        b2Body.createFixture(fDef).setUserData(this);
        timeToDefineBigPanda = false;

    }

    private  void godModePanda() {
        //store current location of body
        Vector2 position = b2Body.getPosition();
        //destroy body
        world.destroyBody(b2Body);


        //create new body
        BodyDef bDef = new BodyDef();
        bDef.position.set(position);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4 / PandaBros.PPM, 4 / PandaBros.PPM);
        fDef.shape = shape;
        b2Body.createFixture(fDef).setUserData(this);

        fDef.filter.maskBits = PandaBros.GROUND_BIT |
                PandaBros.BRICK_BIT |
                PandaBros.COIN_BIT |
                PandaBros.ENEMY_BIT |
                PandaBros. ENEMY_HEAD_BIT|
                PandaBros.WIN_BIT |
                PandaBros.ITEM_BIT;


        //create head
        EdgeShape head = new EdgeShape(); //// change 6 to other number to figure out scale
        head.set(new Vector2(-2 / PandaBros.PPM, 4 / PandaBros.PPM), new Vector2(2 / PandaBros.PPM, 4 / PandaBros.PPM));
        fDef.shape = head;
        fDef.filter.categoryBits = PandaBros.PANDA_HEAD_BIT;
        fDef.isSensor = true;

        b2Body.createFixture(fDef).setUserData(this);
        //dont repeat this


            timeToDefineGodMode = false;
            timeToRedefinePanda = true;
    }

    private void redefinePanda(){
        //store current location of body
        Vector2 position = b2Body.getPosition();
        //destroy body
        world.destroyBody(b2Body);

        manager.aManager.get(manager.music).stop();



        //create new body
        BodyDef bDef = new BodyDef();
        bDef.position.set(position);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4/PandaBros.PPM, 4/PandaBros.PPM);
        fDef.filter.categoryBits = PandaBros.PANDA_BIT;
        fDef.shape = shape;
        b2Body.createFixture(fDef).setUserData(this);

        fDef.filter.maskBits = PandaBros.GROUND_BIT |
                PandaBros.BRICK_BIT |
                PandaBros.COIN_BIT |
                PandaBros.ENEMY_BIT |
                PandaBros.OBJECT_BIT |
                PandaBros.ENEMY_HEAD_BIT |
                PandaBros.WIN_BIT |
                PandaBros.ITEM_BIT;




        //create head
        EdgeShape head = new EdgeShape(); //// change 6 to other number to figure out scale
        head.set(new Vector2(-2/PandaBros.PPM,4/ PandaBros.PPM), new Vector2(2/PandaBros.PPM, 4/ PandaBros.PPM));
        fDef.shape = head;
        fDef.filter.categoryBits = PandaBros.PANDA_HEAD_BIT;
        fDef.isSensor = true;

        b2Body.createFixture(fDef).setUserData(this);
        //dont repeat this
        timeToRedefinePanda = false;
    }


}
