package graphic.view.labels;

import graphic.model.characters.Entity;
import graphic.model.common.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.LabelView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EntityLabelView extends JLabel implements Observer {
    protected Entity entity;
    protected ImageIcon normalIcon;
    protected ImageIcon stunnedIcon;

    /**
     * Az EntityLabelView osztály konstruktora.
     * @param entity - Az eltárolt entitás.
     * @param normal - A megfelelő kép elérési útvonala.
     * @param stunned - A megfelelő kép elérési útvonala.
     */
    public EntityLabelView(Entity entity, String normal, String stunned) {
        this.entity = entity;
        BufferedImage originalNormal = null, originalStunned = null;
        try {
            originalNormal = ImageIO.read(new File(normal));
            originalStunned = ImageIO.read(new File(stunned));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledNormal = originalNormal.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        Image scaledStunned = originalStunned.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

        this.normalIcon = new ImageIcon(scaledNormal);
        this.stunnedIcon = new ImageIcon(scaledStunned);

        initialize();
    }

    /**
     * A függvény beállítja a Label alap és fontos tulajdonságait.
     */
    private void initialize() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(64, 85));
        setMinimumSize(new Dimension(64, 85));
        setMaximumSize(new Dimension(64, 85));

        if(entity.isStunned()){
            setIcon(stunnedIcon);
        }else{
            setIcon(normalIcon);
        }
    }

    /**
     * Változás esetén frissíti a megjelenítést.
     */
    @Override
    public void update() {
        removeAll();

        initialize();
    }

}
