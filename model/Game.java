package graphic.model;

import graphic.model.characters.Entity;
import graphic.model.characters.Student;
import graphic.model.items.*;
import graphic.model.places.Door;
import graphic.model.places.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Az játékot reprezentáló osztály.
 */
public class Game implements Serializable {

    /**
     * A játék állapotát tárolja, igaz, ha meg lett nyerve
     */
    private boolean gameWon = false;

    /**
     * Azt tárolja, hogy hányadik körben jár a játék.
     */
    private int turn=1;

    /**
     * A játékban lévő szobák listája.
     */
    private final ArrayList<Room> rooms = new ArrayList<>();

    /**
     * A játékban lévő entitások listája.
     */
    private final ArrayList<Entity> entities = new ArrayList<>();

    /**
     * @return A játékban lévő entitások listája.
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * @return A játékban lévő szobák listája.
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Jelzi, hogy a játék megnyerésre került.
     */
    public void gameWon() {
        gameWon = true;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Hozzáad egy entitást a játékhoz.
     * @param e A hozzáadandó entitás.
     */
    public void addEntity(Entity e) {
        entities.add(e);
        e.setGame(this);
    }

    /**
     * Eltávolít egy entitást a játékból.
     * @param e Az eltávolítandó entitás.
     */
    public void entityDied(Entity e) {
        entities.remove(e);
    }

    /**
     * Hozzáad egy szobát a játékhoz.
     * @param r A hozzáadandó szoba.
     */
    public void addRoom(Room r) {
        rooms.add(r);
    }

    /**
     * Eltávolít egy szobát a játékból.
     * @param r Az eltávolítandó szoba.
     */
    public void removeRoom(Room r) {
        rooms.remove(r);
    }

    /**
     * Végrehajt egy játék ciklust, frissítve az entitások és szobák állapotát.
     */
    public void tick() {
        for (Entity e : entities) {
            e.tick();
        }

        for (Room r : rooms) {
            r.tick();
        }

        Random random = new Random();
        if(random.nextBoolean()) {
            int roomsCount = rooms.size();
            int first = random.nextInt(roomsCount/2);
            int second = random.nextInt(roomsCount/2, roomsCount);
            Room firstRoom = rooms.get(first);
            Room secondRoom = rooms.get(second);
            firstRoom.merge(secondRoom);

            roomsCount = rooms.size();
            int index = random.nextInt(roomsCount);
            Room roomToSplit = rooms.get(index);
            roomToSplit.split();
        }

        turn++;
    }

    /**
     * Meghatározza, hogy a játék véget ért-e.
     * A játék akkor ér véget, ha a fordulószám meghaladja a 30-at, vagy ha a játék már megnyerték.
     * @return true, ha a játék véget ért, egyébként false.
     */
    public boolean gameEnded(){
        boolean studentsAreDead = true;
        for (Entity e : entities) {
            if (e instanceof Student)
                if (!e.isKilled()) {
                    studentsAreDead = false;
                    break;
                }
        }

        return turn > 30 || studentsAreDead || gameWon;
    }

    /**
     * Visszaadja a játék aktuális állapotát olvasható formában.
     * @return "ongoing", ha a játék folyamatban van, "won", ha a játékot megnyerték, egyébként "lost".
     */
    public String getReadableStatus() {
        if(!gameEnded()) return "ongoing";
        if(gameWon) return "won";
        return "lost";
    }

    /**
     * @return Visszatér a játék összes információjával.
     */
    public String getinfo() {
        StringBuilder builder = new StringBuilder();
        String newLine = System.lineSeparator();
        String tab = "\t";

        builder.append("Game state: ");
        builder.append(getReadableStatus());
        builder.append(newLine);

        builder.append("Characters of Game: ");
        if(entities.isEmpty()) builder.append("No entities.");
        builder.append(newLine);

        for(Entity entity : entities) {
            String[] lines = entity.getinfo().split(newLine);
            for (String line : lines) {
                builder.append(tab);
                builder.append(line);
                builder.append(newLine);
            }
        }

        builder.append("Rooms of Game: ");
        if(rooms.isEmpty()) builder.append("No rooms.");
        builder.append(newLine);
        for(Room room : rooms) {
            String[] lines = room.getinfo().split(newLine);
            for(String line : lines) {
                builder.append(tab);
                builder.append(line);
                builder.append(newLine);
            }
        }

        return builder.toString().trim();
    }

    /**
     * Az objektumok aktuális számlálóinak listája.
     * Ez a lista tárolja az aktuális számlálókat, amelyeket későbbi állapotok visszaállítására használnak fel.
     */
    private final ArrayList<Integer> objectNums=new ArrayList<>();

    /**
     * Mentésre kerülnek az objektumok aktuális számlálói.
     * Az aktuális számlálók hozzáadódnak az objectNums listához,
     * amelyet a későbbi állapotok visszaállítására használnak fel.
     */
    public void saveObjectNum(){
        objectNums.add(Door.getObjectNum());
        objectNums.add(Entity.getObjectNum());
        objectNums.add(Item.getObjectNum());
        objectNums.add(Room.getObjectNum());
    }

    /**
     * Az objektumok aktuális számlálóit frissíti a korábban elmentett értékekkel.
     * Az objectNums lista tartalmát használja fel a számlálók frissítésére.
     */
    public void updateObjectNums() {
        Door.updateObjectNum(objectNums.get(0));
        Entity.updateObjectNum(objectNums.get(1));
        Item.updateObjectNum(objectNums.get(2));
        Room.updateObjectNum(objectNums.get(3));
    }

    public void initObservers() {
        for(Entity entity : entities) {
            entity.initObserversList();
            for(Item item : entity.getItems()) {
                item.initObserversList();
            }
        }
        for(Room room : rooms) {
            room.initObserversList();
            for(Item item : room.getItems()) {
                item.initObserversList();
            }
        }
    }
}
