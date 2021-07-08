package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.proyecto.MainGame;


import Items.Item;
import Sprites.Enemy;
import Sprites.InteractiveTileObject;
import Sprites.Mario;

public class WorldContactListener implements ContactListener {

    /*CONTACT LISTENER
    Es lo que se manda a llamar cuando dos accesorios en box2d colisionan unos con otros
    * */
    @Override
    public void beginContact(Contact contact) {
        /*Es mandado a llamar cuando dos objetos comienzan a colisionar
        * */
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //asi es como nos damos cuenta si la colisión está en la cabeza de Mario
        if (fixA.getUserData()=="head" || fixB.getUserData() =="head"){
            Fixture head  = fixA.getUserData() == "head"  ?  fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            //VERIFICAR SI EL OBJETO FORMA PARTE DE LA CLASE InteractiveTileObject
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        //QUE SUCEDE EN CASO DE COLISIONES DE OBJETOS, ENEMIGOS, PERSONAJE, ETC.

        switch (cDef){
           /* case MainGame.MARIO_HEAD_BIT | MainGame.BRICK_BIT:
            case MainGame.MARIO_HEAD_BIT | MainGame.COIN_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;*/
            case MainGame.ENEMY_HEAD_BIT | MainGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead();
                else if (fixB.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixB.getUserData()).hitOnHead();
                break;
            case MainGame.ENEMY_BIT | MainGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
           /* case MainGame.MARIO_BIT | MainGame.ENEMY_BIT:
                Gdx.app.log("MARIO", "DIED");
            case MainGame.ENEMY_BIT | MainGame.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;*/
            /*case MainGame.MARIO_BIT | MainGame.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit();
                else
                    ((Mario) fixA.getUserData()).hit();
            case MainGame.ITEM_BIT | MainGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;*/
            case MainGame.ITEM_BIT | MainGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    if (!(fixA.getUserData() instanceof String))
                        ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
            /*case MainGame.ITEM_BIT | MainGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead();
                else if (fixB.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixB.getUserData()).hitOnHead();
                break;*/
        }


    }

    @Override
    public void endContact(Contact contact) {
        /*
        * Cuando dos accesorios se desconectan uno del otro
        * */
        Gdx.app.log("End contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        /*Nos da la oportunidad de que una vez que ha habido una colisión, puedan modificarse las caracterísitcas
        de esa colisión
        * */
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        /*Nos da los resultados de lo que ocurre cuando se da una colisión, lo cual pueden ser varias cosas
        * */
    }
}
