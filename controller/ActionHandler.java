package graphic.controller;

import graphic.model.items.Item;
import graphic.model.places.Door;

/**
 * Az ActionHandler interfész meghatározza a játékban végrehajtható műveleteket.
 * Az interfész implementációinak konkrét logikát kell biztosítaniuk ezen műveletek kezelésére.
 */
public interface ActionHandler {

    /**
     * Áthelyezi a játékost a megadott ajtóhoz.
     * @param door az ajtó, amelyhez át kell helyezni a játékost
     */
    void move(Door door);

    /**
     * Lehetővé teszi a játékos számára, hogy bepillantsanak a megadott ajtón keresztül anélkül, hogy mozognának.
     * @param door az ajtó, amelyen keresztül be kell pillantani
     */
    void peek(Door door);

    /**
     * Felveszi a megadott tárgyat.
     * @param item a tárgy, amelyet fel kell venni
     */
    void pickup(Item item);

    /**
     * Ledobja a megadott tárgyat.
     * @param item a tárgy, amelyet le kell dobni
     */
    void drop(Item item);

    /**
     * Összekapcsol két tárgyat.
     * @param item1 az első tárgy, amelyet össze kell kapcsolni
     * @param item2 a második tárgy, amelyet össze kell kapcsolni
     */
    void link(Item item1, Item item2);

    /**
     * Aktiválja a megadott tárgyat.
     * @param item a tárgy, amelyet aktiválni kell
     */
    void activate(Item item);

    /**
     * Befejezi a játékos körét.
     */
    void endTurn();
}
