package graphic.view.labels;

import graphic.model.common.Observer;
import graphic.model.items.Item;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ItemLabelView extends JLabel implements Observer {
    protected Item item;
    protected Icon hasHealthIcon;
    protected Icon noHealthIcon;

    /**
     * Az ItemLabelView osztály konstruktora.
     * @param item - Az eltárol tárgy.
     * @param hasHealth - A megfelelő ikon elérési útvonala.
     * @param noHealth - A megfelelő ikon elérési útvonala.
     */
    public ItemLabelView(Item item, String hasHealth, String noHealth) {
        this.item = item;

        BufferedImage originalHasHealth, originalNoHealth;
        try {
            originalHasHealth = ImageIO.read(new File(hasHealth));
            originalNoHealth = ImageIO.read(new File(noHealth));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledHasHealth = originalHasHealth.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        Image scaledNoHealth = originalNoHealth.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

        this.hasHealthIcon = new ImageIcon(scaledHasHealth);
        this.noHealthIcon = new ImageIcon(scaledNoHealth);
        if(item.hasHealth()){
            setIcon(hasHealthIcon);
        }
        else{
            setIcon(noHealthIcon);
        }
        initialize();
    }

    public ItemLabelView() {
        initialize();
    }

    /**
     * A függvény beállítja a Label fontos tulajdonságait.
     */
    private void initialize() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(64, 64));
        setMinimumSize(new Dimension(64, 64));
        setMaximumSize(new Dimension(64, 64));
    }

    public Item getItem(){
        return item;
    }

    /**
     * Változás esetén frissíti a nézetet.
     */
    @Override
    public void update() {
        removeAll();
        if(item.hasHealth()){
            setIcon(hasHealthIcon);
        }
        else{
            setIcon(noHealthIcon);
        }
        initialize();
    }

}
