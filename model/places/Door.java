package graphic.model.places;

import graphic.model.characters.Entity;

import java.io.Serializable;

/**
 * Az ajtót reprezentáló osztály.
 */
public class Door implements Serializable {



    /**
     * Ezen statikus tagváltozó azt számolja hány példány jött már létre ezn osztálybból.
     */
    protected static int objectNum=0;

    /**
     * Ezen tagváltozó egy adott példány egyedi azonósítására szolgáló String értéket tárol.
     */
    protected String id;

    /**
     * Az ajtó egyik oldalán lévő szoba.
     */
    private Room firstRoom;

    /**
     * Az ajtó másik oldaján lévő szoba.
     */
    private Room secondRoom;

    /**
     * Ezen attribútum bool értékkel tárolja, hogy az adott ajtót lehet-e
     * használni.
     */
    private boolean closed;

    /**
     * Ezen attribútum bool értékkel tárolja, hogy az adott ajtó egyirányú-e.
     * Igaz érték esetén ezen ajtón keresztül csak a firstRoom által tárolt szobából lehet a
     * secondRoom által tárolt szobába menni.
     */
    private boolean oneway;

    /**
     * Beállítja az egyirányúságot.
     *
     * @param val az érték, amire változik
     */
    public void setOneway(boolean val) {
        oneway = val;
    }

    /**
     *
     * @return igaz, ha egyirányú, hamis, ha nem.
     */
    public boolean isOneway() {
        return oneway;
    }

    /**
     * A Door osztály konstruktora.
     * Növeli az objectNum értékét és beállítja az azonosítót.
     */
    public Door(){
        objectNum++;
        String[] objectName=getClass().getName().split("\\.");
        id=objectName[objectName.length-1]+"#"+objectNum;
    }

    /**
     * Megváltoztatja az ajtó állapotát (nyitott vagy zárt).
     */
    public void changeClosed() {
    closed = !closed;
    }

    /**
     * Engedi az entitást át az ajtón, ha az az adott szobából jön.
     *
     * @param e      Az áthaladni kívánó entitás.
     * @param src Az a szoba, amelyből az entitás jön.
     * @return Igaz, ha az entitás áthaladhatott az ajtón, egyébként hamis.
     */
    public boolean letEntityThrough(Entity e, Room src) {

        Room dest = getNeighbour(src);

        if(closed){
            return  false;
        }

        if (firstRoom.equals(dest) || secondRoom.equals(dest) ) {
            if(oneway){
                if(!secondRoom.equals(dest)){
                    return false;
                }
            }
            return dest.acceptEntity(e);
        }
        return false;
    }

    /**
     * Kicseréli az ajtóhoz kapcsolódó szobákat.
     *
     * @param oldRoom A régi szoba.
     * @param newRoom Az új szoba.
     */
    public void swapRoom(Room oldRoom, Room newRoom) {

        if (firstRoom.equals(oldRoom))
            firstRoom = newRoom;
        if (secondRoom.equals(oldRoom))
            secondRoom = newRoom;
    }

    /**
     * Beállítja az ajtóhoz kapcsolódó szobákat.
     *
     * @param r1 Az egyik szoba.
     * @param r2 A másik szoba.
     */
    public void setNeighbours(Room r1, Room r2) {
        firstRoom = r1;
        secondRoom = r2;
    }

    /**
     * Visszaadja az ajtóhoz kapcsolódó szomszédos szobát.
     *
     * @param r Az aktuális szoba.
     * @return A szomszédos szoba.
     */
    public Room getNeighbour(Room r) {

        if (r.equals(firstRoom)) {
            return secondRoom;
        }
        if (r.equals(secondRoom)) {
            return firstRoom;
        }

        return null;
    }



    /**
     * @return Visszatér az osztály id tagváltozójával.
     */
    public String getID(){
        return id;
    }

    public String getinfo() {
        StringBuilder builder = new StringBuilder();
        String newLine = System.lineSeparator();

        builder.append("ID: ").append(id).append(newLine);
        builder.append("First Room: ").append(firstRoom.getID()).append(newLine);
        builder.append("Second Room: ").append(secondRoom.getID()).append(newLine);
        builder.append("Closed: ").append(closed).append(newLine);
        builder.append("Oneway: ").append(oneway).append(newLine);

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

    public boolean isClosed() {
        return closed;
    }

    public Room getSecondRoom() {
        return secondRoom;
    }

    public Room getFirstRoom() {
        return firstRoom;
    }
}
