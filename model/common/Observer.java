package graphic.model.common;

/**
 * Az Observer interfész meghatározza a megfigyelők (observerek) számára a frissítési metódust.
 * A megfigyelési mintában a megfigyelők értesítést kapnak, amikor az általuk figyelt objektum állapota megváltozik.
 */
public interface Observer {

    /**
     * Ez a metódus akkor kerül meghívásra, amikor a megfigyelt objektum állapota megváltozik.
     * Az implementációknak meg kell határozniuk, hogy milyen lépéseket tegyenek a frissítéskor.
     */
    void update();
}
