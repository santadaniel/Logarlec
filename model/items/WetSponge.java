package graphic.model.items;

import graphic.model.places.Room;
import graphic.model.characters.Student;

/**
 * A szobát, amelyben a hallgató aktiválja ezt a tárgyat nedvessé teszi (letörlődik a tábla).
 */
public class WetSponge extends Item {

    /**
     * Az WetSponge osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public WetSponge(){
        health = 1;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Aktiválja az adott szivacsot ezzel, nedvesre állítva szobát,
     * amelyben a paraméterben kapott s hallgató tartózkodik.
     *
     * @param student A metódust meghívó hallgató.
     */
    public void activate(Student student) {
        if(health <= 0) {
            return;
        }

        Room studentRoom = student.getRoom();
        studentRoom.makeRoomWet();
        health--;

        notifyObservers();
    }

}
