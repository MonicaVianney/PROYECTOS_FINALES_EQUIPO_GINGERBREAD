package Sprites;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.proyecto.MainGame;

import Screens.GameScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected GameScreen screen;
    protected Fixture fixture;
    protected MapObject object;

    public InteractiveTileObject(GameScreen screen, MapObject object){
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();

        //crear un objeto de la libreria box2d
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2)/ MainGame.PPM, (bounds.getY() + bounds.getHeight() / 2)/MainGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 /MainGame.PPM, bounds.getHeight() / 2 / MainGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

        /*
        *    bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/MainGame.PPM, (rect.getY() + rect.getHeight() / 2)/MainGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 /MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        * */
    }

    //public abstract void onHeadHit(Mario mario);
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * MainGame.PPM / 16),
                (int)(body.getPosition().y * MainGame.PPM / 16));
    }

    public abstract void onHeadHit();
}
