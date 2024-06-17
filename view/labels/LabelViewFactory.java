package graphic.view.labels;

import graphic.model.characters.*;
import graphic.model.items.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A LabelViewFactory osztály felelős az ItemLabelView és EntityLabelView objektumok létrehozásáért és tárolásáért.
 * Ez a gyár minta biztosítja, hogy minden elemhez és entitáshoz egyedi nézet (view) tartozzon.
 */
public class LabelViewFactory {
    private Map<Item, ItemLabelView> itemLabelViews = new HashMap<>();
    private Map<Entity, EntityLabelView> entityLabelViews = new HashMap<>();

    /**
     * Visszaadja az adott elemhez (item) tartozó ItemLabelView objektumot.
     * Ha az elemhez még nem tartozik nézet, akkor újat hoz létre és hozzáadja a térképhez (map).
     *
     * @param item az elem, amelyhez a nézetet kérjük
     * @return az adott elemhez tartozó ItemLabelView
     */
    public ItemLabelView getItemLabel(Item item) {
        if (!itemLabelViews.containsKey(item)) {
            ItemLabelView itemLabelView = null;
            if (item.getClass().equals(Beer.class)) {
                itemLabelView = new ItemLabelView(item, "res/Beer.png", "res/Beer_NoHealth.png");
            } else if(item.getClass().equals(AirConditioner.class)){
                itemLabelView = new ItemLabelView(item, "res/AirConditioner.png", "res/AirConditioner_NoHealth.png");
            } else if (item.getClass().equals(Camembert.class)) {
                itemLabelView = new ItemLabelView(item, "res/Camembert.jpg", "res/Camembert_NoHealth.png");
            } else if(item.getClass().equals(FakeFFP2.class)){
                itemLabelView = new ItemLabelView(item, "res/FFP2.png", "res/FFP2_NoHealth.png");
            } else if (item.getClass().equals(FakeLogarlec.class)) {
                itemLabelView = new ItemLabelView(item, "res/Logarlec.png", "res/Logarlec.png");
            } else if(item.getClass().equals(FakeTVSZ.class)){
                itemLabelView = new ItemLabelView(item, "res/TVSZ.png", "res/TVSZ_NoHealth.png");
            } else if (item.getClass().equals(FFP2.class)) {
                itemLabelView = new ItemLabelView(item, "res/FFP2.png", "res/FFP2_NoHealth.png");
            } else if(item.getClass().equals(Logarlec.class)){
                itemLabelView = new ItemLabelView(item, "res/Logarlec.png", "res/Logarlec.png");
            } else if (item.getClass().equals(Transistor.class)) {
                itemLabelView = new TransistorLabelView(item, "res/Transistor.png", "res/Transistor_NoHealth.png", "res/Transistor_Linked.png");
            } else if(item.getClass().equals(TVSZ.class)){
                itemLabelView = new ItemLabelView(item, "res/Tvsz.png", "res/TVSZ_NoHealth.png");
            } else if(item.getClass().equals(WetSponge.class)){
                itemLabelView = new ItemLabelView(item, "res/WetSponge.png", "res/WetSponge_NoHealth.png");
            }

            itemLabelViews.put(item, itemLabelView);
            item.addObserver(itemLabelView);
            return itemLabelView;
        } else {
            return itemLabelViews.get(item);
        }
    }

    /**
     * Visszaadja az adott entitáshoz (entity) tartozó EntityLabelView objektumot.
     * Ha az entitáshoz még nem tartozik nézet, akkor újat hoz létre és hozzáadja a térképhez (map).
     *
     * @param entity az entitás, amelyhez a nézetet kérjük
     * @return az adott entitáshoz tartozó EntityLabelView
     */
    public EntityLabelView getEntityLabel(Entity entity) {
        if (!entityLabelViews.containsKey(entity)) {
            EntityLabelView entityLabelView = null;
            if(entity.getClass().equals(CleaningLady.class)){
                entityLabelView = new EntityLabelView(entity, "res/CleaningLady.png", "res/CleanningLady_Stunned.png");
            } else if(entity.getClass().equals(Prof.class)){
                entityLabelView = new EntityLabelView(entity, "res/Professor.png", "res/Professor_Stunned.png");
            } else if(entity.getClass().equals(Student.class)){
                entityLabelView = new StudentLabelView(entity,"res/Student.png", "res/Student_Stunned.png", "res/Student_Drunk.png");
            }
            entityLabelViews.put(entity, entityLabelView);
            return entityLabelView;
        } else {
            return entityLabelViews.get(entity);
        }
    }
}
