package graphic.model.util;

import graphic.model.Game;
import graphic.model.places.*;
import graphic.model.characters.*;
import graphic.model.items.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MapBuilder {
    private Game game;

    private Scanner sharedScanner;

    public MapBuilder() {
        game = new Game();
    }

    public void build(String[] args) {
        String filepath = null;
        try {
            if (args.length < 2) {
                sharedScanner = new Scanner(System.in);
            } else {
                filepath = "maps" + File.separator + args[1];
                sharedScanner = new Scanner(new File(filepath));
            }

            while (sharedScanner.hasNextLine()) {
                String line = sharedScanner.nextLine();
                String[] cmd = line.split(" ");
                switch (cmd[0]) {
                    case "load" -> load(cmd);
                    case "save" -> save(cmd);
                    case "addPlayer" -> addPlayer(cmd);
                    case "addNPC" -> addNPC(cmd);
                    case "addRoom" -> addRoom(cmd);
                    case "addDoor" -> addDoor(cmd);
                    case "addItem" -> addItem(cmd);
                    default -> System.out.println("Invalid command: " + line);
                }
            }
        } catch (Exception ex) {
            System.out.println("Did not find " + filepath);
        } finally {
            sharedScanner.close();
        }
    }

    /**
     * Betölti a játék állapotát egy megadott fájlból.
     * @param cmd A parancs argumentumai, amelyek között a második a fájl elérési útja.
     */
    public void load(String[] cmd) {
        try {
            FileInputStream fileIn = new FileInputStream(cmd[1]);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            // Nothing happens
        }

        game.updateObjectNums();
    }

    /**
     * Elmenti a játék állapotát egy megadott fájlba.
     * @param cmd A parancs argumentumai, amelyek között a második a fájl elérési útja.
     */
    public void save(String[] cmd) {

        try {
            FileOutputStream fileOut = new FileOutputStream(cmd[1]);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            game.saveObjectNum();
            out.writeObject(game);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            // Nothing happens
        }

    }

    /**
     * Hozzáad egy új játékost a játékhoz.
     * @param cmd A parancs argumentumai
     */
    public void addPlayer(String[] cmd) {
        if(cmd.length < 3) return;

        Entity student;
        ArrayList<Entity> entities = game.getEntities();
        for(Entity entity : entities) {
            if(entity.getID().equals(cmd[1])) {
                return;
            }
        }

        ArrayList<Room> rooms = game.getRooms();
        for(Room room : rooms) {
            if(room.getID().equals(cmd[2])) {
                student = new Student(cmd[1]);
                student.setRoom(room);
                room.addEntity(student);
                game.addEntity(student);
                return;
            }
        }
    }

    /**
     * Hozzáad egy új NPC-t a játékhoz.
     * @param cmd A parancs argumentumai
     */
    public void addNPC(String[] cmd) {
        if(cmd.length < 3) return;

        Entity entity = null;
        try {
            Class<?> npcClass = Class.forName("graphic.model.characters." + cmd[1]);
            entity = (Entity) npcClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            // Nothing happens
        }
        if(entity == null) return;

        ArrayList<Room> rooms = game.getRooms();
        for(Room room : rooms) {
            if(room.getID().equals(cmd[2])) {
                entity.setRoom(room);
                room.addEntity(entity);
                game.addEntity(entity);
                return;
            }
        }
    }

    /**
     * Hozzáad egy új szobát a játékhoz.
     * @param cmd A parancs argumentumai
     */
    public void addRoom(String[] cmd) {
        if(cmd.length < 2) return;

        Room room = new Room();
        int capacity = Integer.parseInt(cmd[1]);
        room.setCapacity(capacity);

        for(int i = 2; i < cmd.length; i++) {
            switch (cmd[i]) {
                case "toxic" -> room.makeRoomToxic();
                case "wet" -> room.makeRoomWet();
                case "cursed" -> room.makeRoomCursed();
                case "sticky" -> room.makeRoomSticky();
            }
        }

        game.addRoom(room);
    }

    /**
     * Hozzáad egy új ajtót a játékhoz.
     * @param cmd A parancs argumentumai
     */
    public void addDoor(String[] cmd) {
        if(cmd.length < 3) return;

        Room room1 = null;
        Room room2 = null;
        boolean oneway = false;

        ArrayList<Room> rooms = game.getRooms();
        for(Room room : rooms) {
            if(room.getID().equals(cmd[1])) {
                room1 = room;
            }
            if(room.getID().equals(cmd[2])) {
                room2 = room;
            }
        }

        if(room1 == null || room2 == null || room1 == room2) return;

        if(cmd.length > 3 && cmd[3].equals("oneway")) oneway = true;

        Door door = new Door();
        door.setNeighbours(room1, room2);
        door.setOneway(oneway);
        room1.addDoor(door);
        room2.addDoor(door);
    }

    /**
     * Hozzáad egy új tárgyat a játékhoz.
     * @param cmd A parancs argumentumai
     */
    public void addItem(String[] cmd) {
        if(cmd.length < 3) return;

        Item item = null;
        try {
            Class<?> npcClass = Class.forName("graphic.model.items." + cmd[1]);
            item = (Item) npcClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            // Nothing happens
        }
        if(item == null) return;

        ArrayList<Room> rooms = game.getRooms();
        for(Room room : rooms) {
            if(room.getID().equals(cmd[2])) {
                room.addItem(item);
                return;
            }
        }

        ArrayList<Entity> entities = game.getEntities();
        for(Entity entity : entities) {
            if(entity.getID().equals(cmd[2])) {
                entity.addItem(item);
                return;
            }
        }
    }
}
