package graphic.model.items;

import graphic.model.places.Room;
import graphic.model.characters.Student;

/**
 * Ez a tárgy az őt felhasználó hallgató szobáját mérgezővé teszi, aktiválás hatására.
 */
public class Camembert extends Item {

    /**
     * A Camembert osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public Camembert(){
        health=1;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Aktiválja a tárgyat, és a tárgy health-ét megfelelő értékre állítja.
     * Meghívja a mérgező szoba létrejövéséhez szükséges függvényeket.
     *
     * @param student A metódust meghívó hallgató.
     */
    public void activate(Student student) {

        if(health==0) {
            return;
        }

        Room studentRoom = student.getRoom();
        studentRoom.makeRoomToxic();
        health--;
        notifyObservers();
    }
}
