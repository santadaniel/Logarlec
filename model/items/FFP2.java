package graphic.model.items;

/**
 * Azon tárgy, amely a hallgatókat képes megvédeni mérges gázzal teli szoba hatásától.
 */
public class FFP2 extends Item {

    /**
     * Az FFP2 osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public FFP2(){
        health = 3;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Megvizsgálja ezen metódus, hogy az adott FFP2-es maszk, az adott állapotában képes-e megvédeni
     * egy hallgatót mérges gázzal teli szoba hatásától, és amennyiben igen igaz, ha nem hamis értékkel
     * tér vissza a metódus.
     *
     * @return Megvédi-e a hívóját a mérgező szoba támadásától.
     */
    @Override
    public boolean protectAgainstToxicRoom() {
        if (health <= 0)
            return false;
        health--;

        notifyObservers();
        return true;
    }

}
