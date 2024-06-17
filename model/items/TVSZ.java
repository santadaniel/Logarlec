package graphic.model.items;

/**
 * A játékban szereplő TVSZ objektumok reprezentálásért felel.
 * A felhasználóját megvédi az oktatók támadásaitól.
 */
public class TVSZ extends Item {

    /**
     * Az TVSZ osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public TVSZ(){
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
        health = 1;
    }

    /**
     * Arra szolgál, hogy a tárgy megvédje a hallgatót az oktatók támadásaitól.
     * Ha van élete megvédi a hívóját.
     *
     * @return Megvédi-e a hívóját az oktató támadásától.
     */
    public boolean protectAgainstProf() {
        if (health <= 0) {
            return false;
        }
        health--;
        notifyObservers();

        return true;
    }

}
