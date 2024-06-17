package graphic.model.characters;

import graphic.model.places.Door;
import graphic.model.places.Room;

/**
 * A takarító nő reprezentáló osztály.
 */
public class CleaningLady extends Entity {

    /**
     * A CleaningLady osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public CleaningLady(){
        objectNum++;
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Reagál, a paraméterben kapott entity bemutatkozására.
     * @param e A találkozott entitás.
     */
    public void meet(Entity e) {
        e.meet(this);
    }

    /**
     * Meghívja az ősosztály move(door) függvényét, ha ez igaz, akkor
     * kitaraítja a szobáját.
     *
     * @param door az ajtó, amelyen keresztül át akar menni a célszobába a karakter
     *
     * @return sikeres-e az átkelés
     */
    public boolean move(Door door) {
        boolean ret = super.move(door);
        if (ret)
            room.clean(this);

        return ret;
    }

    /**
     * Felülírja az ősosztály metódusát, hamissal tér vissza.
     *
     * @param destRoom
     * @return false
     */
    @Override
    public boolean toxicate(Room destRoom) {
        return false;
    }

    /**
     * Felülírja az ősosztály metódusát, hamissal tér vissza.
     *
     * @return false
     */
    @Override
    public boolean immobilize() {
        return false;
    }
}
