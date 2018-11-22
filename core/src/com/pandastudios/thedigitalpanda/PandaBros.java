package com.pandastudios.thedigitalpanda;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandastudios.thedigitalpanda.Screens.*;
import com.pandastudios.thedigitalpanda.Tools.Manager;

public class PandaBros extends Game {
	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short PANDA_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
    public static final short PANDA_HEAD_BIT = 512;
	public static final short WIN_BIT = 1024;




	public Manager manager;


	@Override
	public void create () {

		manager = new Manager();
		manager.load();

		while (!manager.aManager.update()) {
			System.out.println(manager.aManager.getProgress());
		}


		batch = new SpriteBatch();


		setScreen(new PlayScreen(this));

	}


	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();

	}

	@Override
	public void render () {
		super.render();
	}

}
