package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.proyecto.MainGame;

import Scenes.Hud;
import Screens.GameScreen;

public class Brick extends InteractiveTileObject{
    public Brick (GameScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this); //Se asigna el userData al objeto mismo, así que tenemos acceso a este objeto en específico
        setCategoryFilter(MainGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(MainGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MainGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }

    /*@Override
    public void onHeadHit(Mario mario) {
        //if (mario.isBig()) {
            Gdx.app.log("Brick", "Collision");
            //setCategoryFilter(MainGame.DESTROYED_BIT);
           // getCell().setTile(null);
            //Hud.addScore(200);
           // MainGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
       // }
       // MainGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }*/
}
