package Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.proyecto.MainGame;

import Screens.GameScreen;
import Sprites.Mario;

public class Mushroom extends Item{
    public Mushroom(GameScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);
        velocity = new Vector2(0.7f,0); //0,0

    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); //32
        bdef.type = BodyDef.BodyType.DynamicBody; //puede moverse y es afectado por la gravedad
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ MainGame.PPM);
        fdef.filter.categoryBits = MainGame.ITEM_BIT;
        fdef.filter.maskBits = MainGame.MARIO_BIT | MainGame.OBJECT_BIT | MainGame.GROUND_BIT | MainGame.COIN_BIT | MainGame.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }


    @Override
    public void use(Mario mario) {
        destroy();
        //mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //body.setLinearVelocity(velocity);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);

    }
}
