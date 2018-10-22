package com.pandastudios.thedigitalpanda.Tools;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Manager {

    public AssetManager aManager = new AssetManager();

    public static final AssetDescriptor<Music> music=
            new AssetDescriptor<Music>("audio/music/mario_music.ogg", Music.class);
    public static final AssetDescriptor<Sound> coin=
            new AssetDescriptor<Sound>("audio/sounds/coin.wav", Sound.class);
    public static final AssetDescriptor<Sound> bump=
            new AssetDescriptor<Sound>("audio/sounds/bump.wav", Sound.class);
    public static final AssetDescriptor<Sound> breakblock=
            new AssetDescriptor<Sound>("audio/sounds/breakblock.wav", Sound.class);
    public static final AssetDescriptor<Sound> spawnPowerup=
            new AssetDescriptor<Sound>("audio/sounds/spawnpowerup.wav", Sound.class);
    public static final AssetDescriptor<Sound> powerup=
            new AssetDescriptor<Sound>("audio/sounds/powerup.wav", Sound.class);
    public static final AssetDescriptor<Sound> powerdown=
            new AssetDescriptor<Sound>("audio/sounds/powerdown.wav", Sound.class);
    public static final AssetDescriptor<Sound> stomp=
            new AssetDescriptor<Sound>("audio/sounds/stomp.wav", Sound.class);
    public static final AssetDescriptor<Sound> RIP=
            new AssetDescriptor<Sound>("audio/sounds/RIP.wav", Sound.class);
    public void load()
    {
        aManager.load("audio/music/mario_music.ogg", Music.class);
        aManager.load("audio/sounds/coin.wav", Sound.class);
        aManager.load("audio/sounds/bump.wav", Sound.class);
        aManager.load("audio/sounds/breakblock.wav", Sound.class);
        aManager.load("audio/sounds/spawnpowerup.wav", Sound.class);
        aManager.load("audio/sounds/powerup.wav", Sound.class);
        aManager.load("audio/sounds/powerdown.wav", Sound.class);
        aManager.load("audio/sounds/stomp.wav", Sound.class);
        aManager.load("audio/sounds/RIP.wav", Sound.class);
        aManager.finishLoading();
    }

    public void dispose()
    {
        aManager.dispose();
    }

}
