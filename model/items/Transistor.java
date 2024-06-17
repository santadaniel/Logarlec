package graphic.model.items;

import graphic.model.characters.Entity;
import graphic.model.places.Room;
import graphic.model.characters.Student;

/**
 * A játékban megtalálható tranzisztorokat reprezentáló osztály.
 * A tranzisztorok összekapcsolásáért, aktiválásért és felvételéért felel.
 */
public class Transistor extends Item {

    /**
     * Az aktív állapotot reprezentálja.
     */
    private boolean active;

    /**
     * Az Transistor osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public Transistor(){
        health = -1;

        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Tárol egy szobát. Ha aktív és van párja, akkor a párjának aktiválása esetén
     * az aktiválást kezdeményező hallgató ebbe a szobába fog kerülni.
     */
    private Room room = null;

    /**
     * A tranzisztor (és párjának) állapotától függően eltérően viselkedik.
     * Ha a tranzisztornak nincs párja, akkor azonnal visszatér.
     * Ha van párja, de az nem aktív, akkor beregisztrál egy szobát és aktívvá állítja a tranzisztort,
     * majd lehelyezi azt a szobában. Ha van párja és az aktív, akkor ez a tranzisztor kikerül
     * a hallgató tárgyai közül a hallgató szobájába és a párjában beregisztrált szobához
     * teleportálja felhasználóját, a hallgatót.
     *
     * @param student A metódust meghívó hallgató.
     */
    public void activate(Student student) {
        if (pair == null) {
            return;
        }

        Transistor pair = (Transistor)this.pair;
        if(pair.active) {
            if(pair.teleport(student)) {
                setPair(null);
            }
        } else {
            setRoom(student.getRoom());
            setActive(true);
            student.drop(this);
        }
        notifyObservers();
    }

    /**
     * Ha a tranzisztor aktív, akkor hamissal tér vissza, ezzel megtagadva a felvételt.
     * Ellenkező esetben igazzal tér vissza.
     *
     * @param entity A metódust meghívó entitás.
     * @return Fel lehet-e venni a tranzisztort.
     */
    public boolean pickup(Entity entity) {
        return !active;
    }

    /**
     * Meghívja a paraméterként kapott tárgyon a link(transistor) fejlécű
     * metódust, ha az tranzisztor, akkor a link(transistor) igazzal tér vissza, ilyenkor a link
     * hívója is beregisztrálja magában a másik tárgyat, hiszen az egy tranzisztor.
     *
     * @param item Az a tárgy, amivel össze szeretnénk kapcsolni a hívott tranzisztort.
     */
    public void link(Item item) {
        if(item.link(this)) {
            setPair(item);

            notifyObservers();
        }
    }

    /**
     * Beregisztrálja magában a kapott tranzisztort egy párként, majd igazzal tér vissza.
     *
     * @param transistor Az a tranzisztor amivel össze szeretnénk kapcsolni ezt a tranzisztort.
     * @return Sikeres volt-e a paraméterként kapott tranzisztor beregisztrálása.
     */
    public boolean link(Transistor transistor) {
        setPair(transistor);
        notifyObservers();
        return true;
    }

    /**
     * Megpróbálja áttolni a kapott hallgatót a regisztrált szobába.
     * Ha ez sikerül, akkor elejteti a hallgatóval a párját és átállítja a szobákat, majd deaktiválja magát és lekapcsolja a párját.
     * Különben nem tesz semmit, a kapcsolat megmarad, a hallgató a régi szobában marad.
     *
     * @param student A tranzisztor párjának az aktiválását kiváltó hallgató.
     * @return Sikerült-e a teleport.
     */
    boolean teleport(Student student) {
        boolean accepted = room.acceptEntity(student);
        if(accepted) {
            // Move student
            student.drop(pair);
            student.getRoom().removeEntity(student);
            room.addEntity(student);
            student.setRoom(room);
            // Deactivate and unlink
            setActive(false);
            setPair(null);
        }

        notifyObservers();
        return accepted;
    }

    /**
     * @param room erre állítja be a transistor szobáját.
     */
    public void setRoom(Room room) {
        this.room = room;

        notifyObservers();
    }

    /**
     * @param active erre állítja be a transistor active állapotát.
     */
    public void setActive(boolean active) {
        this.active = active;

        notifyObservers();
    }

    /**
     * Frissíti a szobát.
     * @param r erre frissíti a szobát.
     */
    @Override
    public void updateRoom(Room r) {
        setRoom(r);
    }

    /**
     * @return Visszatér az entitás összes információjával.
     */
    public String getinfo() {
        String newLine = System.lineSeparator();
        StringBuilder builder = new StringBuilder(super.getinfo());
        builder.append("Active: ").append(active).append(newLine);
        builder.append("Room: ");
        if(room == null) builder.append("No room.");
        else builder.append(room.getID());
        builder.append(newLine);

        return builder.toString();
    }
}
