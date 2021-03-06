package com.pandastudios.thedigitalpanda.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Tools.Manager;


public class Turtle extends Enemy {
        public static final int KICK_LEFT = -2;
        public static final int KICK_RIGHT = 2;
        public enum State {WALKING, MOVING_SHELL, STANDING_SHELL, DEAD}
        public State currentState;
        public State previousState;
        private float stateTime;
        private Animation<TextureRegion> walkAnimation;
        private Array<TextureRegion> frames;
        private TextureRegion shell;
        private float deadRotationDegrees;
        private boolean setToDestroy;
        private boolean destroyed;


        public Turtle(PlayScreen screen, float x, float y, Manager manager) {
            super(screen, x, y);
            frames = new Array<TextureRegion>();
            frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
            shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
            walkAnimation = new Animation(0.2f, frames);
            currentState = previousState = State.WALKING;
            deadRotationDegrees = 0;

            setBounds(getX(), getY(), 16 / PandaBros.PPM, 24 / PandaBros.PPM);

        }

        @Override
        protected void defineEnemy() {
            BodyDef bdef = new BodyDef();
            bdef.position.set(getX(), getY());
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2Body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(6 / PandaBros.PPM);
            fdef.filter.categoryBits = PandaBros.ENEMY_BIT;
            fdef.filter.maskBits = PandaBros.GROUND_BIT |
                    PandaBros.COIN_BIT |
                    PandaBros.BRICK_BIT |
                    PandaBros.ENEMY_BIT |
                    PandaBros.OBJECT_BIT |
                    PandaBros.PANDA_BIT;

            fdef.shape = shape;
            b2Body.createFixture(fdef).setUserData(this);

            //Create the Head here:
            PolygonShape head = new PolygonShape();
            Vector2[] vertice = new Vector2[4];
            vertice[0] = new Vector2(-5, 8).scl(1 / PandaBros.PPM);
            vertice[1] = new Vector2(5, 8).scl(1 / PandaBros.PPM);
            vertice[2] = new Vector2(-3, 3).scl(1 / PandaBros.PPM);
            vertice[3] = new Vector2(3, 3).scl(1 / PandaBros.PPM);
            head.set(vertice);

            fdef.shape = head;
            fdef.restitution = 1.8f;
            fdef.filter.categoryBits = PandaBros.ENEMY_HEAD_BIT;
            b2Body.createFixture(fdef).setUserData(this);
        }

        @Override
        public void hitOnHead(Panda panda) {
            if(currentState == State.STANDING_SHELL) {
                if(panda.b2Body.getPosition().x > b2Body.getPosition().x)
                    velocity.x = -2;
                else
                    velocity.x = 2;
                currentState = State.MOVING_SHELL;
                System.out.println("Set to moving shell");
            }
            else {
                currentState = State.STANDING_SHELL;
                velocity.x = 0;
            }

        }

    @Override
    public void onEnemyHit(Enemy enemy) {
        if (enemy instanceof Turtle) {
            if (((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
                Killed();
            }
            else if (currentState == State.MOVING_SHELL &&((Turtle)enemy).currentState == State.WALKING)
            return;
        else
            reverseVelocity(true, false);
        }
        else if (currentState != State.MOVING_SHELL)
            reverseVelocity(true, false);
    }

    public TextureRegion getFrame(float dt){
            TextureRegion region;

            switch (currentState){
                case MOVING_SHELL:
                case STANDING_SHELL:
                    region = shell;
                    break;
                case WALKING:
                default:
                    region = walkAnimation.getKeyFrame(stateTime, true);
                    break;
            }

            if(velocity.x > 0 && region.isFlipX() == false){
                region.flip(true, false);
            }
            if(velocity.x < 0 && region.isFlipX() == true){
                region.flip(true, false);
            }
            stateTime = currentState == previousState ? stateTime + dt : 0;
            //update previous state
            previousState = currentState;
            //return our final adjusted frame
            return region;
        }

        @Override
        public void update(float dt) {
            setRegion(getFrame(dt));
            if(currentState == State.STANDING_SHELL && stateTime > 5){
                currentState = State.WALKING;
                velocity.x = 1;
                System.out.println("WAKE UP SHELL");
            }

            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - 8 /PandaBros.PPM);

            if(currentState == State.DEAD){
                deadRotationDegrees +=3;
                rotate(deadRotationDegrees);
                if (stateTime >5 && !destroyed){
                    world.destroyBody(b2Body);
                    destroyed = true;
                }
            }else
            b2Body.setLinearVelocity(velocity);
        }

        public void draw(Batch batch){
            if(!destroyed)
                super.draw(batch);
        }


        @Override
        public void hitByEnemy(Enemy enemy) {
            reverseVelocity(true, false);
        }

        public void kick(int direction){
            velocity.x = direction;
            currentState = State.MOVING_SHELL;
        }


    public void Killed(){
            currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = PandaBros.NOTHING_BIT;

        for (Fixture fixture  : b2Body.getFixtureList())
            fixture.setFilterData(filter);
        b2Body.applyLinearImpulse(new Vector2(0,5f),b2Body.getWorldCenter(),true);
    }
}

