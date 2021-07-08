package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proyecto.MainGame;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import Items.Item;
import Items.ItemDef;
import Items.Mushroom;
import Scenes.Hud;
import Sprites.Enemy;
import Sprites.Goomba;
import Sprites.Mario;
import Tools.B2WorldCreator;
import Tools.WorldContactListener;

public class GameScreen implements Screen {
    //referencia a nuestro juego usado para fijar la pantalla
    private MainGame game;

    private TextureAtlas atlas;

    //variables para cámara y vista
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Variables de tipo TiledMap
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Variables de Box2D
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Mario player;
    private Goomba goomba;
    private Goomba goomba2;
    private Goomba goomba3;
    private Goomba goomba4;
    private Goomba goomba5;

    private Music music;

    private Array<Item> items;
    public LinkedBlockingQueue<ItemDef> itemsToSpawn;

    private Enemy enemy;



    public GameScreen(MainGame game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MainGame.V_WIDTH /MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, gamecam);
        hud = new Hud(game.batch);

        //Carga nuestro mapa y configura nuestro renderizador de mapas
        mapLoader  = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MainGame.PPM);

        //configura nuestra cámara para que se centre correctamente en el inicio del mapa
        gamecam.position.set(gamePort.getScreenWidth() / MainGame.PPM, gamePort.getScreenHeight() / MainGame.PPM, 0);

         //crea nuestro mundo en 2d, sin asignar gravedad a X pero -10 en y
        world = new World(new Vector2(0,-10), true);
        //permite depurar las líneas de nuestro mundo box2d
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);



        //crear a mario en nuestro mundo del juego
        player = new Mario(this);

        new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        music = MainGame.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        goomba = new Goomba(this, .64f, .16f); //.64f, .16f
        goomba2 = new Goomba(this, 5.64f, .16f);
        goomba3 = new Goomba(this, 13f, .16f);
        goomba4 = new Goomba(this, 16f, .16f);
        goomba5 = new Goomba(this, 20f, .16f);

    }

    public void spawnItem (ItemDef def){
        itemsToSpawn.add(def);
    }

    public void handleSpawningItems(){
        if (!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class){
                 items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }

    public void update(float dt){
        //manejar la entrada del usuario
        handleInput(dt);
        handleSpawningItems();

        world.step(1/60f, 6, 2);

        player.update(dt);
        goomba.update(dt);
        goomba2.update(dt);
        goomba3.update(dt);
        goomba4.update(dt);
        goomba5.update(dt);

        /*for (Enemy enemy : creator.getGoombas()){
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MainGame.PPM)
                enemy.b2body.setActive(true);
        }*/

        for (Item item : items)
            item.update(dt);


        hud.update(dt);

       /* if (player.currentState != Mario.State.DEAD){
            gamecam.position.x = player.b2body.getPosition().x;
        }*/

       gamecam.position.x = player.b2body.getPosition().x;

        //actualizar nuestro cámara de juego con las coordenadas correctas después de los cambios
        gamecam.update();

        //decirle a nuestro render que dibuje únicamente lo que nuestra cámara ve
        renderer.setView(gamecam);
    }


    //es el METODO que usamos para representar nuestra aplicación, actualizar el juego, etc.
    //Lo usamos para representar en pantalla
    @Override
    public void render(float delta) {
        //separa la lógica de actualización de la representación
        update(delta);

        //limpia la pantalla de juego con color negro
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderiza nuestro mapa de juego
        renderer.render();

        //renderiza nuestro Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        //configuración para que en la pantalla se dibuje el personaje y la cámara lo vea
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch); //al jugador le asigna el sprite correspondiente a la configuracion
        /*for (Enemy enemy : creator.getGoombas())
            enemy.draw(game.batch);*/


        goomba.draw(game.batch);
        goomba2.draw(game.batch);
        goomba3.draw(game.batch);
        goomba4.draw(game.batch);
        goomba5.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        game.batch.end();

        //configuración para que en la pantalla se dibuje lo que la cámara HUD ve
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /*CON ESTE METODO LIBERAMOS RECURSOS PARA QUE NO SE QUEDEN EN LA TARJETA DE VIDEO O MEMORIA*/
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
