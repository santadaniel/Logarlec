package graphic.controller;

import graphic.model.*;
import graphic.model.characters.CleaningLady;
import graphic.model.characters.Entity;
import graphic.model.characters.Prof;
import graphic.model.characters.Student;
import graphic.model.items.Item;
import graphic.model.places.Door;
import graphic.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * A játékot kezelő Controller osztály, amely vezérli a játék folyamatát és az entitások közötti interakciót a játékkörnyezettel.
 */
public class Controller implements ActionHandler {

    /**
     * A játékot tároló objektum.
     */
    private Game game;

    private View view;

    /**
     * Az aktív karaktert tároló entitás objektum.
     */
    private Entity activeCharacter;

    /**
     * Az aktív karaktert tároló elem sorszáma.
     */
    private int activeCharacterIndex;

    /**
     * Egy Random objektum, amelyet közösen használunk a véletlenszerűség generálásához.
     */
    private final Random sharedRandom;

    /**
     * Konstruktor egy Controller példány létrehozásához egy adott játékobjektummal.
     */
    public Controller(Game game, View view) {
        this.game = game;
        this.view = view;
        this.view.registerActionHandler(this);
        sharedRandom = new Random();
        activeCharacterIndex=0;
    }

    /**
     * Indítja a játékot a felhasználói bemenet fogadásával és a játék ciklusának futtatásával.
     */
    public void startGame() {
        ArrayList<Entity> entities = game.getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if(entity instanceof Student student) {
                activeCharacterIndex = i;
                activeCharacter = student;
                view.update((Student) activeCharacter);
                break;
            }
        }
        view.start();
    }

    /**
     * Kezeli a professzor körét egy adott professzor entitás esetén.
     * @param prof Az aktív professzor entitás, akinek a körét kezelni kell.
     */
    public void handleProfTurn(Prof prof) {
        ArrayList<Door> doors = prof.getRoom().getDoors();
        if(!doors.isEmpty()) {
            int index = sharedRandom.nextInt(doors.size());
            prof.move(doors.get(index));
        }

        ArrayList<Item> items = prof.getRoom().getItems();
        if(!items.isEmpty()) {
            int index = sharedRandom.nextInt(items.size());
            prof.pickUp(items.get(index));
        }
    }

    /**
     * Kezeli a takarítónő körét egy adott takarítónő entitás esetén.
     * @param cleaningLady Az aktív takarítónő entitás, akinek a körét kezelni kell.
     */
    public void handleCleaningLadyTurn(CleaningLady cleaningLady) {
        ArrayList<Door> doors = cleaningLady.getRoom().getDoors();
        if(!doors.isEmpty()) {
            int index = sharedRandom.nextInt(doors.size());
            cleaningLady.move(doors.get(index));
        }
    }

    /**
     * Mozgatja az aktív karaktert egy adott ajtón keresztül egy másik szobába.
     * @param selectedDoor Az ajtó amin keresztül mozogjon.
     */
    public void move(Door selectedDoor) {
        if(activeCharacter instanceof Student student && !student.getStudentMoved()) {
            student.move(selectedDoor);
            student.setStudentMoved(true);
            view.update(student);
        }
    }

    /**
     * Az aktív karakter felveszi a paraméterben kaott trágyat.
     * @param item a kapott tárgy.
     */
    public void pickup(Item item) {
        activeCharacter.pickUp(item);
        if(activeCharacter instanceof Student student) {
            view.update(student);
        }
    }


    /**
     * Aktivál egy tárgyat az aktív karakter jelenlegi szobájában.
     * @param item A tárgy amelyet aktivál
     */
    public void activate(Item item) {
        activeCharacter.activateItem(item);
        if(activeCharacter instanceof Student student) {
            view.update(student);
        }
    }



    /**
     *  Elhelyez egy tárgyat az aktív karakter jelenlegi szobájában.
     * @param item Az elhelyezett tárgy.
     */
    public void drop(Item item) {
        activeCharacter.drop(item);
        if(activeCharacter instanceof Student student) {
            view.update(student);
        }
    }


    /**
     * Összekapcsol két tárgyat az aktív karakter jelenlegi szobájában.
     * @param item1 Az egyik összekapcsolandó tárgy.
     * @param item2 A másik összekapcsolandó tárgy.
     */
    public void link(Item item1, Item item2) {
        if(item1.equals(item2)) return;
        activeCharacter.linkItems(item1, item2);
        if(activeCharacter instanceof Student student) {
            view.update(student);
        }
    }


    /**
     * Átnézés egy masik szobába.
     * @param door Ajtó amelyen át szeretnénk tekinteni.
     */
    public void peek(Door door){
        view.peek(door.getNeighbour(activeCharacter.getRoom()));
    }

    /**
     * Véget vett az aktív játékos körének.
     */
    public void endTurn(){
        ArrayList<Entity> entities = game.getEntities();
        if(activeCharacter instanceof Student student)
            student.setStudentMoved(false);
        activeCharacterIndex++;

        if(activeCharacterIndex == entities.size()) {
            game.tick();
            activeCharacterIndex = activeCharacterIndex % entities.size();
        }

        activeCharacter = entities.get(activeCharacterIndex);
        while (!(activeCharacter instanceof Student student) || student.isKilled()) {
            if(game.gameEnded()) break;

            if (activeCharacter instanceof Prof prof) {
                handleProfTurn(prof);
            } else if (activeCharacter instanceof CleaningLady cleaningLady) {
                handleCleaningLadyTurn(cleaningLady);
            }
            activeCharacterIndex++;

            if(activeCharacterIndex == entities.size()) {
                game.tick();
                activeCharacterIndex = activeCharacterIndex % entities.size();
            }

            activeCharacter = entities.get(activeCharacterIndex);
        }

        if(game.isGameWon()) {
            view.displayEndMessage("You won!");
            return;
        } else if(game.gameEnded()) {
            view.displayEndMessage("You lost!");
            return;
        }

        if(activeCharacter instanceof Student student) view.update(student);
    }
}