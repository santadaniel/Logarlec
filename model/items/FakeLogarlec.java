package graphic.model.items;

import graphic.model.characters.Entity;

/**
 * A hamis logarlec reprezentáló osztály.
 */
public class FakeLogarlec extends Logarlec {

    /**
     * A FakeLogarlec osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public FakeLogarlec(){
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Felülírja az ősosztály metódusát, hamissal tér vissza.
     *
     * @return false
     */
    @Override
    public boolean pickup(Entity entity) {
        return true;
    }

}
