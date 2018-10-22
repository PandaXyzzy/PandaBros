package com.pandastudios.thedigitalpanda.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controller {
    Viewport viewPort;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed, aPressed, bPressed;
    OrthographicCamera cam;


    public boolean isaPressed() {return aPressed;}
    public boolean isbPressed() {
        return bPressed;
    }
    public boolean isUpPressed() {
        return upPressed;
    }
    public boolean isDownPressed() {
        return downPressed;
    }
    public boolean isLeftPressed() {
        return leftPressed;
    }
    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewPort.update(width, height);
    }

    public Controller(){
        cam = new OrthographicCamera();
        viewPort = new FitViewport(800, 480, cam);
        stage = new Stage(viewPort);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.defaults().pad(5);
        table.setFillParent(true);
        table.left().bottom();

        Table abTable = new Table();
        abTable.setFillParent(true);
        abTable.right().bottom();




        //up button
        Image upImg = new Image(new Texture("flatDark25.png"));
        upImg.setSize(50,50);
        upImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });
        //down button
        Image downImg = new Image(new Texture("flatDark26.png"));
        downImg.setSize(50,50);
        downImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });
        //right button
        Image rightImg = new Image(new Texture("flatDark24.png"));
        rightImg.setSize(50,50);
        rightImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });
        //left button
        Image leftImg = new Image(new Texture("flatDark23.png"));
        leftImg.setSize(50,50);
        leftImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        //a button
        Image aButtonImg = new Image(new Texture("aButton.png"));
        aButtonImg.setSize(50,50);
        aButtonImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                aPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                aPressed = false;
            }
        });
        //B button
        Image bButtonImg = new Image(new Texture("bButton.png"));
        bButtonImg.setSize(50,50);
        bButtonImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bPressed = false;
            }
        });

        table.debug();

        table.add();
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        table.add();



        abTable.add(aButtonImg).size(upImg.getWidth(), upImg.getHeight()).pad(0,0,5,5);
        abTable.add(bButtonImg).size(upImg.getWidth(), upImg.getHeight()).padBottom(5);

        stage.addActor(table);
        stage.addActor(abTable);
        stage.addActor(abTable);
    }

    public void draw(){
        stage.draw();
    }
}
