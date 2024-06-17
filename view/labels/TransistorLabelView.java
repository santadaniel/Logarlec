package graphic.view.labels;

import graphic.model.common.Observer;
import graphic.model.items.Item;
import graphic.model.items.Transistor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TransistorLabelView extends ItemLabelView {
    protected Icon linkedIcon;

    /**
     * A TransistorLabelView osztály konstruktora.
     * @param item      - Az eltárolt tárgy.
     * @param linked - A megfelelő ikon elérési útvonala.
     */
    public TransistorLabelView(Item item, String hasHealth, String noHealth, String linked) {
        super(item, hasHealth, noHealth);

        BufferedImage originalLinked = null;
        try {
            originalLinked = ImageIO.read(new File(linked));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledLinked = originalLinked.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        this.linkedIcon = new ImageIcon(scaledLinked);

        initialize();
    }

    /**
     * A Label beállításai és megjelenítésének paraméterei.
     */
    private void initialize(){
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(64, 64));
        setMinimumSize(new Dimension(64, 64));
        setMaximumSize(new Dimension(64, 64));

        if(item.hasPair()){
            setIcon(linkedIcon);
        }else if(!item.hasPair()){
            setIcon(hasHealthIcon);
        }
    }

    /**
     * Változáskor frissíti a megjelenítést.
     */
    @Override
    public void update() {
        removeAll();
        initialize();
    }

}
