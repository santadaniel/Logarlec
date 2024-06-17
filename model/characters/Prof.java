package graphic.model.characters;


/**
 * A professzort reprezentáló osztály.
 */
public class Prof extends Entity {

    /**
     * Az Prof osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public Prof(){
        objectNum++;
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Reagál, a paraméterben kapott entity bemutatkozására.
     * @param entity A találkozott entitás.
     */
    public void meet(Entity entity) {
        entity.meet(this);
    }

    /**
     * A paraméterben kapott bemutatkozó hallgatóra, meghívja a megöléséhez
     * szükséges függvényt ezen függvény.
     * @param student a hallgató amelyet megtámad
     */
    public void meet(Student student){
        student.kill(this);
    }
}
