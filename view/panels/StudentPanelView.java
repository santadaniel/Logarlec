package graphic.view.panels;

import graphic.controller.ActionHandler;
import graphic.model.characters.Entity;
import graphic.model.characters.Student;
import graphic.model.common.Observer;
import graphic.model.items.Item;
import graphic.model.places.Door;
import graphic.view.labels.EntityLabelView;
import graphic.view.labels.ItemLabelView;
import graphic.view.labels.LabelViewFactory;
import graphic.view.View;
import graphic.view.labels.StudentLabelView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A StudentPanelView osztály a felhasználói felület része, amely egy hallgató állapotát jeleníti meg.
 * Ez az osztály figyeli a hallgató állapotának változásait és frissíti a nézetet ennek megfelelően.
 */
public class StudentPanelView extends JPanel implements Observer {
    private final Student student;
    private ActionHandler callback;
    private JLabel idLabel;
    private JLabel stunnedLabel;
    private JLabel drunkLabel;
    private EntityLabelView studentLabel;
    private JPanel itemGridPanel;
    private JPanel selectedItemsPanel;
    private ItemLabelView item1Label;
    private Item item1;
    private ItemLabelView item2Label;
    private Item item2;
    private JComboBox<String> doorsComboBox;

    private LabelViewFactory factory = new LabelViewFactory();

    /**
     * Létrehozza a StudentPanelView objektumot és regisztrálja megfigyelőként a megadott hallgatóra és annak szobájára.
     *
     * @param student a megjelenítendő hallgató
     */
    public StudentPanelView(Student student) {
        this.student = student;
        student.addObserver(this);
        student.getRoom().addObserver(this);

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
     * Inicializálja a panelt és beállítja az összes alpanelt és azok kezdeti állapotát.
     */
    public void initialize() {
        // Set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Create panels
        add(createPlayerInfoPanel());
        add(createItemsPanel());
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(createLinkItemsPanel());
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(createTraversalPanel());
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(createEndTurnPanel());
    }

    /**
     * Létrehoz egy panelt a kör befejezéséhez szükséges gombbal.
     *
     * @return a kör befejezéséhez szükséges panel
     */
    private JPanel createEndTurnPanel() {
        JPanel endTurnPanel = new JPanel();
        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.addActionListener(e -> {
            if(callback != null) callback.endTurn();
        });
        endTurnPanel.add(endTurnButton);
        return endTurnPanel;
    }

    /**
     * Létrehoz egy panelt az ajtókon keresztüli mozgáshoz és kukucskáláshoz.
     *
     * @return az ajtókon keresztüli mozgáshoz és kukucskáláshoz szükséges panel
     */
    private JPanel createTraversalPanel() {
        JPanel traversalPanel = new JPanel();
        traversalPanel.setLayout(new BoxLayout(traversalPanel, BoxLayout.Y_AXIS));

        // 'Doors' text
        JPanel doorsTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doorsTextPanel.add(new JLabel("Doors: "));
        traversalPanel.add(doorsTextPanel);

        // Combobox
        JPanel comboboxPanel = new JPanel();
        doorsComboBox = new JComboBox<>();
        updateDoors();
        comboboxPanel.add(doorsComboBox);
        traversalPanel.add(comboboxPanel);

        // Buttons panel
        JPanel traversalButtonsPanel = new JPanel();
        // Move button
        JButton moveButton = new JButton("Move");
        moveButton.addActionListener(e -> {
            if(callback == null) return;
            String selectedDoorID = (String)doorsComboBox.getSelectedItem();
            student.getRoom().getDoors().stream().filter(door -> door.getID().equals(selectedDoorID)).findFirst().ifPresent(selectedDoor -> callback.move(selectedDoor));
        });
        traversalButtonsPanel.add(moveButton);
        // Peek button
        JButton peekButton = new JButton("Peek");
        peekButton.addActionListener(e -> {
            if(callback == null) return;
            String selectedDoorID = (String)doorsComboBox.getSelectedItem();
            student.getRoom().getDoors().stream().filter(door -> door.getID().equals(selectedDoorID)).findFirst().ifPresent(selectedDoor -> callback.peek(selectedDoor));
        });
        traversalButtonsPanel.add(peekButton);
        traversalPanel.add(traversalButtonsPanel);

        return traversalPanel;
    }

    /**
     * Létrehoz egy panelt az elemek összekapcsolásához.
     *
     * @return az elemek összekapcsolásához szükséges panel
     */
    private JPanel createLinkItemsPanel() {
        JPanel linkItemsPanel = new JPanel();
        linkItemsPanel.setLayout(new BoxLayout(linkItemsPanel, BoxLayout.Y_AXIS));

        // 'Link' text
        JPanel linkItemsTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linkItemsTextPanel.add(new JLabel("Link items: "));
        linkItemsPanel.add(linkItemsTextPanel);

        // Create selected items panel
        selectedItemsPanel = new JPanel();
        item1Label = getEmptyItemLabelView();
        item1Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isMiddleMouseButton(e)) {
                    item1 = null;
                    updateIconsOfSelectedItems();
                }
            }
        });

        item2Label = getEmptyItemLabelView();
        item2Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isMiddleMouseButton(e)) {
                    item2 = null;
                    updateIconsOfSelectedItems();
                }
            }
        });

        updateIconsOfSelectedItems();

        linkItemsPanel.add(selectedItemsPanel);

        // Link button
        JPanel linkItemsButtonPanel = new JPanel();
        JButton linkButton = new JButton("Link");
        linkButton.addActionListener(e -> {
            if(callback == null || item1 == null || item2 == null) return;
            callback.link(item1, item2);
            item1 = null;
            item2 = null;
            updateIconsOfSelectedItems();
        });
        linkItemsButtonPanel.add(linkButton);
        linkItemsPanel.add(linkItemsButtonPanel);

        return linkItemsPanel;
    }

    /**
     * Létrehoz egy panelt az elemek megjelenítéséhez.
     *
     * @return az elemek megjelenítéséhez szükséges panel
     */
    private JPanel createItemsPanel() {
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        // 'Items' text
        JPanel itemsTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemsTextPanel.add(new JLabel("Items: "));
        itemsPanel.add(itemsTextPanel);

        // Item grid panel
        itemGridPanel = new JPanel();
        ((FlowLayout)itemGridPanel.getLayout()).setHgap(0);
        updateItemGridPanel();
        itemsPanel.add(itemGridPanel);

        return itemsPanel;
    }

    /**
     * Létrehoz egy panelt a hallgató információinak megjelenítéséhez.
     *
     * @return a hallgató információinak megjelenítéséhez szükséges panel
     */
    private JPanel createPlayerInfoPanel() {
        // Get  JLabel for student and remove all mouse listeners of it
        studentLabel = factory.getEntityLabel(student);
        detachMouseListenersFromLabel(studentLabel);

        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.add(studentLabel, BorderLayout.EAST);

        JPanel playerDetailsPanel = new JPanel();
        playerDetailsPanel.setLayout(new BoxLayout(playerDetailsPanel, BoxLayout.Y_AXIS));
        idLabel = new JLabel("ID: " + student.getID());
        playerDetailsPanel.add(idLabel);
        stunnedLabel = new JLabel("Stunned for: " + student.getStunnedFor());
        playerDetailsPanel.add(stunnedLabel);
        drunkLabel = new JLabel("Drunk for: " + student.getDrunkFor());
        playerDetailsPanel.add(drunkLabel);
        playerInfoPanel.add(playerDetailsPanel, BorderLayout.CENTER);

        return playerInfoPanel;
    }

    /**
     * Frissíti a nézetet a hallgató állapotának változásakor.
     */
    @Override
    public void update() {
        updatePlayerDetails();
        updateItemGridPanel();
        studentLabel.update();
        if(!student.getItems().contains(item1)) item1 = null;
        if(!student.getItems().contains(item2)) item2 = null;
        updateIconsOfSelectedItems();
        updateDoors();

        revalidate();
        repaint();
    }

    /**
     * Frissíti a hallgató részleteit megjelenítő panelt.
     */
    private void updatePlayerDetails() {
        idLabel.setText("ID: " + student.getID());
        stunnedLabel.setText("Stunned for: " + student.getStunnedFor());
        drunkLabel.setText("Drunk for: " + student.getDrunkFor());
    }

    /**
     * Frissíti az elemeket megjelenítő rácspanelt.
     */
    private void updateItemGridPanel() {
        itemGridPanel.removeAll();

        for(Item item : student.getItems()) {
            // Get label of item
            ItemLabelView itemLabelView = factory.getItemLabel(item);
            // Attach correct mouse listeners
            detachMouseListenersFromLabel(itemLabelView);
            attachMouseListenerToLabelInInventory(itemLabelView);
            // Add label to grid
            itemGridPanel.add(itemLabelView);
        }
        for(int i = student.getItems().size(); i < 5; i++) {
            // Get empty label
            ItemLabelView emptyLabel = getEmptyItemLabelView();
            // Add to grid, no listeners needed
            itemGridPanel.add(emptyLabel);
        }

        itemGridPanel.revalidate();
        itemGridPanel.repaint();
    }

    /**
     * Frissíti a kiválasztott elemek ikonjait megjelenítő panelt.
     */
    private void updateIconsOfSelectedItems() {
        selectedItemsPanel.removeAll();

        if(item1 != null) item1Label.setIcon(factory.getItemLabel(item1).getIcon());
        else item1Label.setIcon(null);
        selectedItemsPanel.add(item1Label);

        if(item2 != null) item2Label.setIcon(factory.getItemLabel(item2).getIcon());
        else item2Label.setIcon(null);
        selectedItemsPanel.add(item2Label);

        selectedItemsPanel.revalidate();
        selectedItemsPanel.repaint();
    }

    /**
     * Frissíti az ajtók listáját a comboboxban.
     */
    private void updateDoors() {
        doorsComboBox.removeAllItems();
        for(Door door: student.getRoom().getDoors()) {
            if(door.isClosed() || (door.isOneway() && door.getSecondRoom().equals(student.getRoom()))) continue;
            doorsComboBox.addItem(door.getID());
        }
    }

    /**
     * Létrehoz egy üres ItemLabelView objektumot.
     *
     * @return egy üres ItemLabelView objektum
     */
    private ItemLabelView getEmptyItemLabelView() {
        return new ItemLabelView();
    }

    /**
     * Eltávolítja az összes egérhallgatót a megadott címkéről.
     *
     * @param label az a címke, amelyről az egérhallgatókat el kell távolítani
     */
    private void detachMouseListenersFromLabel(JLabel label) {
        for(MouseListener listener : label.getMouseListeners()) {
            label.removeMouseListener(listener);
        }
    }

    /**
     * Hozzáad egy egérhallgatót egy ItemLabelView objektumhoz a hallgató elemeihez való interakcióhoz.
     *
     * @param label az ItemLabelView objektum, amelyhez az egérhallgatót hozzá kell adni
     */
    private void attachMouseListenerToLabelInInventory(ItemLabelView label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if(callback != null) callback.activate(label.getItem());
                } else if(SwingUtilities.isMiddleMouseButton(e)) {
                    if(item1 == null) {
                        item1 = label.getItem();
                        updateIconsOfSelectedItems();
                    } else if(item2 == null){
                        item2 = label.getItem();
                        updateIconsOfSelectedItems();
                    }
                } else if(SwingUtilities.isRightMouseButton(e)) {
                    if(callback != null) callback.drop(label.getItem());
                }
            }
        });
    }

    /**
     * Visszaadja a hallgatót, amelyet ez a panel megjelenít.
     *
     * @return a hallgató
     */
    public Student getStudent(){
        return student;
    }
}
