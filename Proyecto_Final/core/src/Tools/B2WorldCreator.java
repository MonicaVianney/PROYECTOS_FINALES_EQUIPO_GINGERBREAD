package Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.proyecto.MainGame;

import Screens.GameScreen;
import Sprites.Brick;
import Sprites.Coin;
import Sprites.Goomba;

public class B2WorldCreator {
    private Array<Goomba> goombas;

    public B2WorldCreator(GameScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //crear variables de cuerpo y fijación
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        Filter filter;

        /*FixtureDef.filter.categoryBits = MainGame.OBJECT_BIT;
        FixtureDef.filter.maskBits = MainGame.ENEMY_BIT | MainGame.MARIO_BIT;*/

        //crear cuerpos de tierra/accesorios
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/ MainGame.PPM, (rect.getY() + rect.getHeight() / 2)/MainGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 /MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //crear tuberías/accesorios
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/MainGame.PPM, (rect.getY() + rect.getHeight() / 2)/MainGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 /MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MainGame.OBJECT_BIT;
            body.createFixture(fdef);
        }

        //crear cuerpos de ladrillos/accesorios
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen, object);
        }

        //crear cuerpos de monedas/accesorios
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object);
        }

        //crear a todos los goombas

        /*goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            goombas.add(new Goomba(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }*/

    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
}
