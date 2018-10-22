package com.pandastudios.thedigitalpanda.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.pandastudios.thedigitalpanda.Scenes.Hud;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Tools.Manager;

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object, Manager manager){
        super(screen, object, manager);
        fixture.setUserData(this);
        setCategoryFilter(PandaBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Panda panda) {
        Gdx.app.log("Brick", "Collision");
        if (panda.isBig){
            setCategoryFilter(PandaBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        manager.aManager.get(manager.breakblock).play();
    }else {
            manager.aManager.get(manager.bump).play();
            Gdx.app.log("Brick", "Small Panda Collision");
        }
    }

}
