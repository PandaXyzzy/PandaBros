package com.pandastudios.thedigitalpanda.Sprites.TileObjects;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.pandastudios.thedigitalpanda.PandaBros;
import com.pandastudios.thedigitalpanda.Screens.PlayScreen;
import com.pandastudios.thedigitalpanda.Sprites.Panda;
import com.pandastudios.thedigitalpanda.Tools.Manager;

public class Win extends InteractiveTileObject {
    private static TiledMapTileSet set;


    public Win(PlayScreen screen, MapObject object, Manager manager){
        super(screen, object, manager);
        set = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(PandaBros.WIN_BIT);
    }


    @Override
    public void onHeadHit(Panda panda) {
        panda.setPandaWins();

    System.out.print("YOU WIN!");

    }
}
