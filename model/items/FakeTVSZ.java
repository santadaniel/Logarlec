package graphic.model.items;

/**
 * A hamis tvsz reprezentáló osztály.
 */
public class FakeTVSZ extends TVSZ {

    /**
     * A FakeTVSZ osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public FakeTVSZ() {
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Felülírja az ősosztály metódusát, hamissal tér vissza.
     *
     * @return false
     */
    @Override
    public boolean protectAgainstProf() {
        return false;
    }
}
