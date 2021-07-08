package Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.proyecto.MainGame;

import java.sql.Struct;

import Screens.GameScreen;

public class Goomba extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;


    public Goomba(GameScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MainGame.PPM, 16 / MainGame.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //setRegion(walkAnimation.getKeyFrame(stateTime, true));

        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0 , 16, 16));
            stateTime = 0;
        }
        else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); //32
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ MainGame.PPM);
        //COLISIONES
        fdef.filter.categoryBits = MainGame.ENEMY_BIT; //se utiliza para saber la categoría del accesorio
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.MARIO_BIT; //Aquí se detecta con qué está colisionando el personaje mario

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


        //crear la cabeza aquí
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-4, 8).scl(1 / MainGame.PPM);
        vertice[1] = new Vector2(4, 8).scl(1 / MainGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MainGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / MainGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MainGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnHead() {

        setToDestroy = true;
        //MainGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }


}
