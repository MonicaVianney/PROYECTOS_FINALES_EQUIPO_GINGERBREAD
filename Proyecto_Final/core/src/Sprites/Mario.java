package Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.proyecto.MainGame;

import Screens.GameScreen;

public class Mario  extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private Animation <TextureRegion> marioRun;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation<TextureRegion> bigMarioRun;
    private Animation<TextureRegion> growMario;

    private float stateTimer; //se utiliza para dar seguimiento a la cantidad de tiempo en que estamos en el estado actual
    private boolean runningRight; //esto es para definir en qué dirección está corriendo mario

    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;


    public Mario(GameScreen screen){

        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //obtener las animaciones de correr y agregarlas a la animación marioJump
        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        marioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

        //obtener set de animación para cuando Mario cambia de tamaño
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 1, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 1, 0, 16, 32));
        growMario = new Animation<TextureRegion>(0.2f, frames);


        //obtener las animaciones de salto y agregarlas a la animación marioJump
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 32);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        //crear región de textura para la animación de mario
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 32);

        //asignar valores iniciales para la ubicación de mario, tales como ancho, largo y posición inicial en la pantalla
        setBounds(1,11, 16 / MainGame.PPM, 16 / MainGame.PPM);
        setRegion(marioStand);

        defineMario();

    }

    public void update (float dt){
        if (marioIsBig)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 /MainGame.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        /*if (timeToDefineBigMario)
            defineBigMario();*/
        /*if (timeToRedefineMario)
            redefineMario();*/
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true) ;
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (marioIsDead)
            return State.DEAD;
        else if (runGrowAnimation)
            return State.GROWING;
        else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0 )
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void grow(){
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        MainGame.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public boolean isBig(){
        return marioIsBig;
    }

    public void hit(){
        if (marioIsBig){
            marioIsBig = false;
            timeToRedefineMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight()/2);
            MainGame.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
        }else{
            MainGame.manager.get("audio/sounds/mario_music.wav", Music.class).stop();
            MainGame.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = MainGame.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0,4f), b2body.getWorldCenter(), true);

        }
    }

    /*public void redefineMario(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        //COLISIONES
        fdef.filter.categoryBits = MainGame.MARIO_BIT; //se utiliza para saber la categoría del accesorio
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT|
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT | MainGame.ITEM_BIT; //Aquí se detecta con qué está colisionando el personaje mario

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0,-14/MainGame.PPM));
        b2body.createFixture(fdef);


        //SE CREA UN SENSOR EN LA CABEZA DE MARIO PARA LAS COLISIONES DE SALTO
        EdgeShape head = new EdgeShape(); //Un EdgeShape es básicamente una línea que divide dos puntos u objetos
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM)); //se dibuja la linea
        fdef.filter.categoryBits = MainGame.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToRedefineMario = false;
    }*/

    /*public void defineBigMario(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10/MainGame.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        //COLISIONES
        fdef.filter.categoryBits = MainGame.MARIO_BIT; //se utiliza para saber la categoría del accesorio
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT|
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT | MainGame.ITEM_BIT; //Aquí se detecta con qué está colisionando el personaje mario

        fdef.shape = shape;
        b2body.createFixture(fdef);


        //SE CREA UN SENSOR EN LA CABEZA DE MARIO PARA LAS COLISIONES DE SALTO
        EdgeShape head = new EdgeShape(); //Un EdgeShape es básicamente una línea que divide dos puntos u objetos
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM)); //se dibuja la linea
        fdef.filter.categoryBits = MainGame.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }*/

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        //COLISIONES
        fdef.filter.categoryBits = MainGame.MARIO_BIT; //se utiliza para saber la categoría del accesorio
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT|
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT | MainGame.ITEM_BIT; //Aquí se detecta con qué está colisionando el personaje mario

        fdef.shape = shape;
        b2body.createFixture(fdef);
       /* shape.setPosition(new Vector2(0,-14/MainGame.PPM));
        b2body.createFixture(fdef);*/


        //SE CREA UN SENSOR EN LA CABEZA DE MARIO PARA LAS COLISIONES DE SALTO
        EdgeShape head = new EdgeShape(); //Un EdgeShape es básicamente una línea que divide dos puntos u objetos
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM)); //se dibuja la linea
        //fdef.filter.categoryBits = MainGame.MARIO_BIT; //MARIO_HEAD_BIT
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
        //timeToDefineBigMario = false;
    }
}
