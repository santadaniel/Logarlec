package graphic.model.items;

import graphic.model.characters.Student;

/**
 * A sör egy aktiválással hasznosítható tárgy.
 * Aktiválásával a hallgató védettségre tehet szert, (részeggé válással) az oktatók támadásával szemben.
 */
public class Beer extends Item {

    public Beer(){
        health = 1;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Aktiválja a tárgyat, és a tárgy health-ét megfelelő értékre állítja.
     * Meghívja a hallgató védetté („részeggé”) válásához szükséges függvényeket.
     *
     * @param student A metódust meghívó hallgató.
     */
    public void activate(Student student) {
        if(health==0) {
            return;
        }
        student.setDrunkFor(3);
        health--;
        for (Item _item : student.getItems()) {
            if (_item instanceof Beer)
                continue;
            student.drop(_item);
            break;
        }
        notifyObservers();
    }
}
