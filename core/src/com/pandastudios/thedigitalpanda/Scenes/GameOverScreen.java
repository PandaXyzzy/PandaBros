package com.pandastudios.thedigitalpanda.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.Sprites.Panda;

import java.util.PriorityQueue;

public class GameOverScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;

    public GameOverScreen(Game game){
        this.game = game;
        viewport = new FitViewport(PandaBros.V_WIDTH, PandaBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,((PandaBros) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table gameOverTable = new Table();
        gameOverTable.center();
        gameOverTable.setFillParent(true);

        Label gameOverLable = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("Click to Play Again", font);


        gameOverTable.add(gameOverLable).expandX();
        gameOverTable.row();
        gameOverTable.add(playAgainLabel).expandX().padTop(10);

        stage.addActor(gameOverTable);
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()){
            game.setScreen(new PlayScreen((PandaBros) game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();

    }
}
