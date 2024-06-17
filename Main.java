package graphic;

import graphic.controller.Controller;
import graphic.model.util.MapBuilder;
import graphic.model.Game;
import graphic.view.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {

    /**
     * A program belépési pontja.
     * @param args a belépési paraméter.
     */
    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("build")) {
            MapBuilder builder = new MapBuilder();
            builder.build(args);
            return;
        }

        // Load gamestate
        Game game = new Game();
        try {
            FileInputStream fileIn = new FileInputStream("game.data");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
            game.initObservers();
            game.updateObjectNums();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not open file: game.data");
            return;
        }

        // Create view
        View view = new View();
        view.init(game);

        // Create controller
        Controller controller = new Controller(game, view);

        // Start game
        controller.startGame();
    }
}