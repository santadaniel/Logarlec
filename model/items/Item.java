package graphic.model.items;

import graphic.model.characters.Entity;
import graphic.model.common.Observer;
import graphic.model.places.Room;
import graphic.model.characters.Student;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A játékban megtalálható tárgyakat reprezentáló osztály.
 * A tárgyak felhasználásáért, összekapcsolásáért, valamint felvételéért felel.
 */
public abstract class Item implements Serializable {

    /**
     * Ezen statikus tagváltozó azt számolja hány példány jött már létre ezn osztálybból.
     */
    protected static int objectNum=0;

    /**
     * Ezen tagváltozó egy adott példány egyedi azonósítására szolgáló String értéket tárol.
     */
    protected String id;

    /***
     * Az osztály observereit tartalmazó lista gyűjtemény.
     */
    protected ArrayList<Observer> observers=new ArrayList<>();

    /**
     * Ezen tagváltozó egy adott példány életét reprezentálja.
     */
    protected int health;

    /**
     * Tárol egy tárgyat, ezzel a tárggyal van összekapcsolva, ha az érvényes objektum.
     */
    protected Item pair;

    /**
     * Az Item osztály konstruktora.
     * Beállítja a párt null-ra
     */
    public Item(){
        observers = new ArrayList<>();
        objectNum++;

        pair = null;
    }

    /**
     * Ebben az implementációban igazzal tér vissza. Arra szolgál, hogy például a logarléc vagy
     * a tranzisztor felüldefiniálja és új viselkedést vezessenek be.
     *
     * @param entity A metódust meghívó entitás.
     * @return Itt a tárgy felvehetőséget jelzi; azaz igaz, ha fel lehet venni és hamis, ha nem.
     */
    public boolean pickup(Entity entity) {
        return true;
    }

    /**
     * Ez egy üres implementáció, arra szolgál, hogy a leszármazott tárgyak felüldefiniálják,
     * ha parancsra fel lehet őket használni.
     *
     * @param student A hallgató, aki meghívta a metódust.
     */
    public void activate(Student student) {
    }

    /**
     * Arra szolgál, hogy a tárgy megvédje a hallgatót a mérgező szoba támadásától.
     * Ebben az implementációban hamissal tér vissza, a leszármazottak, amiknek van ilyen képességük felüldefiniálják.
     *
     * @return A tárgy megvédi-e a hallgatót a mérgező szobától.
     */
    public boolean protectAgainstToxicRoom() {
        return false;
    }

    /**
     * Arra szolgál, hogy a tárgy megvédje a hallgatót az oktatók támadásaitól.
     * Ebben az implementációban hamissal tér vissza, a leszármazottak, amiknek van ilyen képességük felüldefiniálják.
     *
     * @return A tárgy megvédi-e a hallgatót az oktató támadásától.
     */
    public boolean protectAgainstProf() {
        return false;
    }

    /**
     * Ebben az implementációban üres. Arra szolgál, hogy felülíríák a leszármazottak, amiket össze lehet kapcsolni.
     *
     * @param item Tárgy, amivel össze szeretnénk kapcsolni a hívott tárgyat.
     */
    public void link(Item item) {
    }

    /**
     * Ebben az implementációban hamissal tér vissza, arra szolgál, hogy a tranzisztor felüldefiniálja.
     * Használata azt biztosítja, hogy tranzisztort csak tranzisztorral lehessen összekapcsolni.
     *
     * @param transistor Transistor, amivel össze szeretnénk kapcsolni a tárgyat.
     * @return Össze sikerült-e kapcsolni a tárgyat a kapott tranzisztorral.
     */
    public boolean link(Transistor transistor) {
        return false;
    }

    /**
     * @param pair a pair attribútumot erre állítja.
     */
    public void setPair(Item pair) {
        this.pair = pair;

        notifyObservers();
    }

    /**
     * A tárgy szobáját frissíti, itt egy üres implementáció.
     * @param r Az új szoba
     */
    public void updateRoom(Room r) {
    }

    /**
     * @return Visszatér az osztály id tagváltozójával.
     */
    public String getID(){
        return id;
    }

    /**
     * @return Visszatér a tárgy összes információjával.
     */
    public String getinfo() {
        StringBuilder builder = new StringBuilder();
        String newLine = System.lineSeparator();

        builder.append("ID: ").append(id).append(newLine);
        builder.append("Health: ").append(health).append(newLine);
        builder.append("Pair: ");
        if(pair == null) builder.append("No pair.");
        else builder.append(pair.getID());
        builder.append(newLine);

        return builder.toString();
    }

    /**
     * @return Az osztályból létrehozott példányok számát adja vissza.
     */
    public static int getObjectNum(){
        return objectNum;
    }

    /**
     * Frissíti az objectNum statikus tagváltozót, ha nagyobb értéket kap.
     * @param value az új érték amire módosulni próbál
     */
    public static void updateObjectNum(int value){
        if(objectNum<value)
            objectNum=value;
    }

    /**
     * Az observers gyűjtemény elemei közé rakja a paraméterben kapott elemet.
     * @param obs A gyűjteménybe helyezendő elem.
     */
    public void addObserver(Observer obs){
        observers.add(obs);
    }

    /**
     * Az observers gyűjtemény elemei közül kiveszi a paraméterben kapott elemet.
     * @param obs A gyűjteményből kivevendő elem.
     */
    public void removeObserver(Observer obs){
        observers.remove(obs);
    }

    /**
     * Értesíti az összes observer-t, és frissíti, amely ezen osztályt figyeli
     */
    public void notifyObservers(){
        for (Observer obs:observers) {
            obs.update();
        }
    }

    /**
     * A függvénnyel lekérdezhető, hogy a tárgynak van-e élete még.
     * @return Igaz/Hamis az értéknek megfelelően.
     */
    public boolean hasHealth(){
        return health>0;
    }

    /**
     * A függvénnyel lekérdezhető, hogy az adott tárgynak van-e párja.
     * @return Igaz/Hamis az értéknek megfelelően.
     */
    public boolean hasPair() { return pair != null; }

    public void initObserversList() {
        observers = new ArrayList<>();
    }
}
