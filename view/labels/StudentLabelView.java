package graphic.view.labels;

import graphic.model.characters.Entity;
import graphic.model.characters.Student;
import graphic.model.common.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.LabelView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StudentLabelView extends EntityLabelView {
    private Student student;
    private ImageIcon drunkIcon;

    /**
     * A StudentLabelView osztály konstruktora.
     * @param student - Az eltárolt entitás.
     * @param normal - A megfelelő kép elérési útvonala.
     * @param stunned - A megfelelő kép elérési útvonala.
     */
    public StudentLabelView(Entity student, String normal, String stunned, String drunk) {
        super(student, normal, stunned);

        BufferedImage originalDrunk = null;
        try {
            originalDrunk = ImageIO.read(new File(normal));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledDrunk = originalDrunk.getScaledInstance(64, 64, Image.SCALE_SMOOTH);

        this.drunkIcon = new ImageIcon(scaledDrunk);
        this.student = (Student)student;

        initialize();
    }

    /**
     * A létrehozáshos és frissítéshez szükséges beállításokat állítja be.
     */
    private void initialize() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(64, 85));
        setMinimumSize(new Dimension(64, 85));
        setMaximumSize(new Dimension(64, 85));

        if(student.isStunned()){
            setIcon(stunnedIcon);
        }else if(student.isDrunk()){
            setIcon(drunkIcon);
        }else{
            setIcon(normalIcon);
        }

    }

    /**
     * Frissíti a képet változás esetén.
     */
    public void update() {
        removeAll();

        initialize();
    }
}
