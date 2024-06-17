package graphic.model.items;

import graphic.model.characters.Entity;

/**
 * A játékban megtalálható logarléc objektumot reprezentálja.
 * Különleges felelőssége, hogy oktatóknak ne engedje a felvételét, hallgatóknak pedig igen.
 */
public class Logarlec extends Item {

    /**
     * Az Logarlec osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public Logarlec(){
        health = -1;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * A paraméterében megkapja, hogy melyik entitás szeretné felvenni.
     * Ezen entitáson meghívja a tryForLogarLec metódust,
     * hogy eldöntse, hogy az entitás felveheti-e az objektumot vagy sem.
     *
     * @param entity A metódust meghívó entitást.
     * @return Itt a tárgy felvehetőséget jelzi; azaz igaz, ha fel lehet venni és hamis, ha nem.
     */
    public boolean pickup(Entity entity) {
        return entity.tryForLogarlec();
    }

}
