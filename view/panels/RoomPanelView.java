package graphic.view.panels;

import graphic.controller.ActionHandler;
import graphic.model.characters.Entity;
import graphic.model.characters.Student;
import graphic.model.common.Observer;
import graphic.model.items.Item;
import graphic.model.places.Room;
import graphic.view.labels.EntityLabelView;
import graphic.view.labels.ItemLabelView;
import graphic.view.labels.LabelViewFactory;

import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A RoomPanelView osztály a felhasználói felület része, amely egy szoba állapotát jeleníti meg.
 * Ez az osztály figyeli a szoba állapotának változásait és frissíti a nézetet ennek megfelelően.
 */
public class RoomPanelView extends JPanel implements Observer {

    private final Room room;
    private ActionHandler callback;
    private JPanel roomStatusPanel;
    private JPanel roomIDPanel;
    private JPanel studentsPanel;
    private JPanel othersPanel;
    private JPanel itemsPanel;
    private Student activeStudent;

    private final LabelViewFactory factory = new LabelViewFactory();

    /**
     * Létrehozza a RoomPanelView objektumot és regisztrálja megfigyelőként a megadott szobára.
     *
     * @param _room a megjelenítendő szoba
     */
    public RoomPanelView(Room _room) {
        room = _room;
        room.addObserver(this);

        initialize();
    }

    /**
     * Regisztrál egy ActionHandler objektumot, amely kezeli a felhasználói műveleteket.
     *
     * @param actionHandler az ActionHandler, amely kezeli a felhasználói műveleteket
     */
    public void registerActionHandler(ActionHandler actionHandler) {
        this.callback = actionHandler;
    }

    /**
     * Ikont hoz létre egy adott színnel és állapottal.
     *
     * @param co a szín
     * @param status az állapot, amely meghatározza, hogy az ikon belseje kitöltött-e vagy sem
     * @return az ikon
     */
    private Icon createIcon(Color co, boolean status) {
        return new Icon() {
            private final int outerDiameter = 20;

            @Override
            public int getIconWidth() {
                return outerDiameter;
            }

            @Override
            public int getIconHeight() {
                return outerDiameter;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(co);
                g2d.fillOval(x, y, outerDiameter, outerDiameter);
                if (!status) {
                    g2d.setColor(Color.WHITE);
                    int innerDiameter = 16;
                    g2d.fillOval(x + (outerDiameter - innerDiameter) / 2, y + (outerDiameter - innerDiameter) / 2, innerDiameter, innerDiameter);
                }
            }
        };
    }

    /**
     * Inicializálja a panelt és beállítja az összes alpanelt és azok kezdeti állapotát.
     */
    public void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel roomAttributePanel = new JPanel(new BorderLayout());

        roomStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roomIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        updateRoomStatusPanel();

        roomIDPanel.add(new JLabel(room.getID()));

        roomAttributePanel.add(roomStatusPanel, BorderLayout.LINE_START);
        roomAttributePanel.add(roomIDPanel, BorderLayout.LINE_END);

        add(roomAttributePanel);

        add(new JSeparator(SwingConstants.HORIZONTAL));

        add(getTextPanel("Students:"));

        studentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateStudentsPanel();
        add(studentsPanel);

        add(getTextPanel("Others:"));

        othersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateLabels(othersPanel);
        add(othersPanel);

        add(getTextPanel("Items:"));

        JPanel itemBox = new JPanel(new BorderLayout());
        itemBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        itemBox.setPreferredSize(new Dimension(512, 100));
        itemBox.setMinimumSize(new Dimension(512, 100));
        itemBox.setMaximumSize(new Dimension(512, 100));

        itemsPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(itemsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        updateItemPanel();

        itemBox.add(BorderLayout.CENTER, scrollPane);
        add(itemBox);
    }

    /**
     * Frissíti a szoba állapotát jelző panelt.
     */
    private void updateRoomStatusPanel() {
        roomStatusPanel.removeAll();
        roomStatusPanel.add(new JLabel(createIcon(Color.GREEN, room.getToxic())));
        roomStatusPanel.add(new JLabel(createIcon(Color.BLUE, room.getWet())));
        roomStatusPanel.add(new JLabel(createIcon(Color.PINK, room.getSticky())));
        roomStatusPanel.add(new JLabel(createIcon(Color.ORANGE, room.getCursed())));
    }

    /**
     * Létrehoz egy panelt a megadott szöveggel.
     *
     * @param s a szöveg, amely megjelenik a panelen
     * @return a szöveges panel
     */
    private JPanel getTextPanel(String s) {
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textPanel.add(new JLabel(s));
        return textPanel;
    }

    /**
     * Frissíti a nézetet a szoba állapotának változásakor.
     */
    @Override
    public void update() {
        updateRoomStatusPanel();

        roomIDPanel.removeAll();
        roomIDPanel.add(new JLabel(room.getID()));

        studentsPanel.removeAll();
        updateStudentsPanel();

        othersPanel.removeAll();
        updateLabels(othersPanel);

        itemsPanel.removeAll();
        updateItemPanel();

        revalidate();
        repaint();
    }

    /**
     * Eltávolítja az összes egérhallgatót (MouseListener) a megadott címkéről (JLabel).
     *
     * @param label a címke, amelyről el kell távolítani az egérhallgatókat
     */
    private void detachMouseListenersFromLabel(JLabel label) {
        for(MouseListener listener : label.getMouseListeners()) {
            label.removeMouseListener(listener);
        }
    }

    /**
     * Hozzáad egy egérhallgatót (MouseListener) az adott címkéhez az elemek panelen belül.
     *
     * @param label az elem címkéje, amelyhez hozzá kell adni az egérhallgatót
     */
    private void attachMouseListenerToLabelInInventory(ItemLabelView label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if(callback != null) {
                        callback.pickup(label.getItem());
                    }
                }
            }
        });
    }

    /**
     * Frissíti a megadott panelt a szoba egyéb entitásaival.
     *
     * @param panel a frissítendő panel
     */
    private void updateLabels(JPanel panel) {
        panel.removeAll();
        for (Entity e : room.getOthers()) {
            JLabel otherLabel = factory.getEntityLabel(e);
            detachMouseListenersFromLabel(otherLabel);
            panel.add(otherLabel);
        }

        panel.revalidate();
        panel.repaint();
    }

    /**
     * Frissíti az elemek panelt a szobában található elemekkel.
     */
    private void updateItemPanel() {
        for (Item i : room.getItems()) {
            ItemLabelView itemLabelView = factory.getItemLabel(i);
            if (itemLabelView == null) {
                itemLabelView = new ItemLabelView();
            }
            detachMouseListenersFromLabel(itemLabelView);
            attachMouseListenerToLabelInInventory(itemLabelView);
            itemsPanel.add(itemLabelView);
        }
    }

    /**
     * Frissíti a hallgatók panelt a szobában található hallgatókkal.
     */
    private void updateStudentsPanel() {
        for (Student s : room.getStudents()) {
            if(!s.isKilled()){
                JPanel studentPanel = new JPanel();
                studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
                EntityLabelView studentLabel = factory.getEntityLabel(s);
                detachMouseListenersFromLabel(studentLabel);
                studentLabel.update();
                JLabel idLabel = new JLabel(s.getID());
                idLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                idLabel.setPreferredSize(new Dimension(64, 16));
                idLabel.setMinimumSize(new Dimension(64, 16));
                idLabel.setMaximumSize(new Dimension(64, 16));

                studentPanel.add(studentLabel);
                studentPanel.add(idLabel);
                studentsPanel.add(studentPanel);
            }
        }
    }

    /**
     * Visszaadja a szobát, amelyet ez a nézet megjelenít.
     *
     * @return a szoba
     */
    public Room getRoom(){
        return room;
    }
}
