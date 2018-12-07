package com.pandastudios.thedigitalpanda.Sprites.TileObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.pandastudios.thedigitalpanda.Scenes.Hud;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Sprites.Items.ItemDef;
import com.pandastudios.thedigitalpanda.Sprites.Items.Mushroom;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Tools.Manager;




public class Coin extends InteractiveTileObject {


    private static TiledMapTileSet set;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object, Manager manager){
        super(screen, object, manager);
        set = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(PandaBros.COIN_BIT);
    }

    @Override
    public void onHeadHit(Panda panda) {
        Gdx.app.log("Coin", "Collision");
        if (getCell().getTile().getId() == BLANK_COIN){
            manager.aManager.get(manager.bump).play();
        }
        else {
            if (object.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16/PandaBros.PPM), Mushroom.class));
                manager.aManager.get(manager.spawnPowerup).play();
                Hud.addScore(100);
        }else
            manager.aManager.get(manager.coin).play();
            Hud.addScore(100);
        }


        getCell().setTile(set.getTile(BLANK_COIN));




    }
}
