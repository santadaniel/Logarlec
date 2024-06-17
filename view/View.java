package graphic.view;

import graphic.controller.ActionHandler;
import graphic.controller.Controller;
import graphic.model.Game;
import graphic.model.characters.Entity;
import graphic.model.characters.Student;
import graphic.model.places.Room;
import graphic.view.panels.RoomPanelView;
import graphic.view.panels.StudentPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A View osztály a játék grafikus felhasználói felületét (GUI) képviseli.
 * Felelős a hallgatói és szobai panelek megjelenítéséért és frissítéséért.
 */
public class View extends JFrame {
    private Controller callback;
    private final List<StudentPanelView> studentPanels;
    private final List<RoomPanelView> roomPanels;
    private StudentPanelView activeStudentPanel;
    private RoomPanelView activeRoomPanel;
    private JPanel mainPanel = new JPanel(new BorderLayout());

    /**
     * Létrehozza a View objektumot és inicializálja a panel listákat.
     */
    public View(){
        studentPanels = new ArrayList<>();
        roomPanels = new ArrayList<>();
    }

    /**
     * Regisztrál egy ActionHandler objektumot, amely kezeli a felhasználói műveleteket a hallgatói és szobai paneleken.
     *
     * @param actionHandler az ActionHandler, amely kezeli a felhasználói műveleteket
     */
    public void registerActionHandler(ActionHandler actionHandler) {
        for(StudentPanelView sp : studentPanels) {
            sp.registerActionHandler(actionHandler);
        }
        for(RoomPanelView rp : roomPanels) {
            rp.registerActionHandler(actionHandler);
        }
    }

    /**
     * Frissíti a nézetet a megadott hallgató állapotának megfelelően.
     *
     * @param student a hallgató, amelynek állapota megváltozott
     */
    public void update(Student student) {
        mainPanel.removeAll();
        for(StudentPanelView studentPanel : studentPanels){
            if(studentPanel.getStudent().equals(student)){
                if(activeStudentPanel != null)
                    mainPanel.remove(activeStudentPanel);
                activeStudentPanel = studentPanel;
                mainPanel.add(activeStudentPanel, BorderLayout.WEST);
            }
        }
        displayRoom(student.getRoom());
        activeStudentPanel.update();
        activeRoomPanel.update();
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Megjeleníti a megadott szobát a nézetben.
     *
     * @param room a megjelenítendő szoba
     */
    private void displayRoom(Room room) {
        for(RoomPanelView roomPanel : roomPanels){
            if(roomPanel.getRoom().equals(room)){
                if(activeRoomPanel != null)
                    mainPanel.remove(activeRoomPanel);
                activeRoomPanel = roomPanel;
                mainPanel.add(roomPanel, BorderLayout.EAST);
            }
        }
    }

    /**
     * Megjeleníti a megadott szobába való bepillantás nézetét.
     *
     * @param room a megtekintett szoba
     */
    public void peek(Room room) {
        mainPanel.removeAll();
        JPanel backPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;

        JButton backButton = new JButton("back");
        backButton.addActionListener(l -> {
            mainPanel.removeAll();
            mainPanel.add(activeStudentPanel, BorderLayout.WEST);
            mainPanel.add(activeRoomPanel, BorderLayout.EAST);
            pack();
            setLocationRelativeTo(null); // Center the frame on the screen
            mainPanel.repaint();
            mainPanel.revalidate();
        });
        backPanel.add(backButton, gbc);
        mainPanel.add(backPanel, BorderLayout.WEST);
        for(RoomPanelView roomPanel : roomPanels){
            if(roomPanel.getRoom().equals(room)){
                mainPanel.add(roomPanel, BorderLayout.EAST);
            }
        }
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Inicializálja a nézetet a játék modellje alapján.
     *
     * @param model a játék modellje
     */
    public void init(Game model){
        for(Entity e : model.getEntities()) {
            if (e instanceof Student student) {
                studentPanels.add(new StudentPanelView(student));
            }
        }
        for(Room r : model.getRooms()){
            roomPanels.add(new RoomPanelView(r));
        }
        add(mainPanel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Logarlec");
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    /**
     * Megnyitja a nézetet és láthatóvá teszi az ablakot.
     */
    public void start() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    /**
     * Megjelenít egy üzenetet a játék végén.
     *
     * @param text a megjelenítendő üzenet
     */
    public void displayEndMessage(String text){
        // Custom panel for the dialog
        JPanel panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(text);
        panel.add(label);

        // Custom button for the dialog
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        panel.add(okButton);

        // Create and display the dialog
        JDialog dialog = new JDialog(this, "The Game Ended!", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this); // Center the dialog
        dialog.setVisible(true);
    }
}
