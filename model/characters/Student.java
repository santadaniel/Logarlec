package graphic.model.characters;

import graphic.model.items.Beer;
import graphic.model.items.Item;
import graphic.model.places.Room;

/**
 * A hallgatót reprezentáló osztály.
 */
public class Student extends Entity {

    /**
     * Ez a tagváltozó a részegség állapotát mutatja.
     */
    private int drunkFor;
    /**
     * Ez a tagváltozó azt jelzi, hogy mozgott e már a hallgató.
     */
    private boolean moved = false;

    /**
     * A tanuló konstruktura.
     * @param name az id attribútumot átállítja erre
     */
    public Student(String name){
        id=name;
    }

    /**
     * A hívott karaktert megpróbálja megölni ezen metódus. Ez sikeres amennyiben nem tud védekezni.
     */
    public void kill(Entity murderer) {
        if (drunkFor > 0) {
            return;
        }

        for (Item i : items) {
            if (i.protectAgainstProf()) {
                return;
            }
        }

        murderer.getRoom().addItems(items);

        items.clear();
        notifyObservers();

        setKilled();
    }

    /**
     *  Ezen függvény  a paraméterben kapott tárolt tárgy aktiválási folyamatát indítja el.
     * @param item aktiválni kivánt tárgy
     */
    @Override
    public void activateItem(Item item) {
        if(this.isStunned() || this.isKilled()) return;

        item.activate(this);
    }


    /**
     * Ezen metódus abban az estben hívódik, amennyiben az adott hallgató logarléc típusú tárgyat próbál felvenni. A student osztály
     * által ezen felüldefiniált verziója a metódusnak, a játék megnyeréséhez vezet.
     * @return sikerül-e a logarlécet felvenni ((hallgató esetén, azaz itt mindig true))
     */
    public boolean tryForLogarlec(){
        game.gameWon();

        return true;
    }

    /**
     * Reagál, a paraméterben kapott entity bemutatkozására.
     * @param entity A találkozott entitás.
     */
    public void meet(Entity entity) {
        entity.meet(this);
    }

    /**
     * Ezen függvény bemutatja a paraméterben kapott oktatónak önmagát.
     * @param prof oktató amelynek bemutatkozik az adott hallgató
     */
    public void meet(Prof prof){
        prof.meet(this);
    }

    /**
     * Az adott hallgatót megmérgezését végzi ezen függvény, amennyiben nincs védekezési lehetőseég.
     * @return sikeres-e a mérgezés
     * @param room a szoba emyleből a mérgező
     */
    public boolean toxicate(Room room) {
        for (Item i : items) {
            if(i.protectAgainstToxicRoom()) {
                return false;
            }
        }

        setStunnedFor(3);
        room.addItems(items);

        items.clear();
        notifyObservers();

        return true;
    }

    /**
     * Ezen metódus, az adott karaktert megpróbálja lebénítani.
     * Ezen implementácójában a metódus visszatérési értéke hamis, mivel a hallgatók ilyen
     * formában nem béníthatók.
     * @return sikeres-e a bénítás ((mindig false ebben az esetben))
     */
    public boolean immobilize() {
        return false;
    }

    /**
     * Ezen függvény a paraméterben kapott két tárgyat próbálja összepárosítani.
     * @param item1 azon tárgy, amelyre meghívódik az összepárosítás
     * @param item2 azon tárgy ameylet tovább adjuk paraméterként
     */
    @Override
    public void linkItems(Item item1, Item item2) {
        if(this.isStunned() || this.isKilled()) return;
        if(!items.contains(item1)) return;
        if(!items.contains(item2)) return;

        item1.link(item2);
    }

    /**
     * Ezen függvény az adott hallgatót részeggé teszi 3 értékkel.
     */
    public void makeStudentDrunk() {
        drunkFor = 3;
        notifyObservers();
    }

    /**
     * Ezen metódus adott időegységenként szükséges feladatok elvégzését látja el az adott
     * karakteren. Ezen implementációja esetén, ha szükséges a ittaségi attribútum értékét csökkenti.
     */
    @Override
    public void tick() {
        super.tick();
        if (drunkFor > 0) {
            drunkFor--;

            notifyObservers();
        }
    }

    /**
     * A paraméterben kapott értékre állítja a hallgató ittasági állapotát.
     * @param value a beállítandó érték
     */
    public void setDrunkFor(int value){
        drunkFor = value;

        notifyObservers();
    }

    /**
     * Visszaadja, hogy a hallgató részeg-e.
     * @return Igaz/Hamis az érték alapján.
     */
    public boolean isDrunk(){ return  drunkFor>0; }

    /**
     * @return Visszatér a tanuló összes információjával.
     */
    public String getinfo() {
        String entityInfo = super.getinfo();
        int pos = entityInfo.indexOf("Killed");
        String newLine = System.lineSeparator();

        StringBuilder builder = new StringBuilder(entityInfo);
        builder.insert(pos, "Drunk for: " + drunkFor + newLine);

        return builder.toString();
    }

    /**
     * Visszaadja azt az időtartamot, ameddig a karakter elkábult állapotban van.
     * @return az elkábulás időtartama
     */
    public int getStunnedFor() {
        return stunnedFor;
    }

    /**
     * Visszaadja azt az időtartamot, ameddig a karakter ittas állapotban van.
     * @return az ittasság időtartama
     */
    public int getDrunkFor() {
        return drunkFor;
    }

    /**
     * Ezzel a metódussal tudjuk jelezni, hogy a hallgató mozgott és nem tud tovább.
     */
    public void setStudentMoved(boolean b){
        moved = b;
    }

    /**
     * Visszaadaj, hogy a Hallgató mozgott e már.
     * @return a mozgathatóságot adja vissza.
     */
    public boolean getStudentMoved(){
        return moved;
    }
}