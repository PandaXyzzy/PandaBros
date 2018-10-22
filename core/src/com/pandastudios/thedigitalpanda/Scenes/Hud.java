package com.pandastudios.thedigitalpanda.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pandastudios.thedigitalpanda.PandaBros;



public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;
    private static int score;

    Label countdownLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label levelLable;
    Label worldLabel;
    Label pandaLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(PandaBros.V_WIDTH, PandaBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLable = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        pandaLabel = new Label("PANDA", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(pandaLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLable).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);


    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >=1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount =0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
