package graphic.model.items;

import graphic.model.characters.Student;

/**
 * A légfrissítő egy aktiválással hasznosítható tárgy.
 */
public class AirConditioner extends Item {

    /**
     * Az AirConditioner osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public AirConditioner(){
        health=1;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Aktiválja a klímát egy adott diák számára.
     * Amennyiben az health attribútum 0, a függvény nem hajtódik végre.
     * A diák szobájának detoxikálását végzi el, ezután
     * a health-et csökkenti.
     * @param student Az a diák, aki aktiválja a klímát.
     */
    public void activate(Student student) {
        if (health <= 0)
            return;
        student.getRoom().detoxicate(); //Detoxicate the user's room.
        health--;
        notifyObservers();
    }
}
