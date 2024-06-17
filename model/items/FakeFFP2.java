package graphic.model.items;

/**
 * A hamis ffp2 maszkot reprezentáló osztály.
 */
public class FakeFFP2 extends FFP2 {

    /**
     * A FakeFFP2 osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public FakeFFP2(){
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Felülírja az ősosztály metódusát, hamissal tér vissza.
     *
     * @return false
     */
    @Override
    public boolean protectAgainstToxicRoom() {
        return false;
    }



}
