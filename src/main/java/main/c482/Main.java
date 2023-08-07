package main.c482;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

/**
 * @author jakobberentsen
 * The Main class is where the application starts.
 * It extends the Application class from the JavaFX library.
 * FUTURE ENHANCEMENT: For a future enhancement I would include a sorting capability.
 * This would sort products and parts by name, ID, and include a feature that
 * lists prices from high to low or vice versa. It would extend the functionality of the application
 * by allowing users to find, access, and organize their products and parts more efficiently.
 */
public class Main extends Application {


    /**
     * The start method is called when the application is launched.
     * It loads the MainScreen.FXML file and sets up the main scene.
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs during loading of the MainScreen.FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 888, 365);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * The main method is the entry point of the application.
     * It launches the JavaFX application.
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        Part Sprocket = new InHouse(1, "belt", 10.00, 10, 3, 20, 1);
        Inventory.addPart(Sprocket);
        Part Chain = new InHouse(2, "chain", 15.00, 10, 3, 10,7);
        Inventory.addPart(Chain);
        Part Screw = new Outsourced(3, "screw", 2.00, 10, 3, 15, "Tim's Bike Shack");
        Inventory.addPart(Screw);

        Product product1 = new Product(1, "Big Wheel", 200.00, 5, 1, 5);
        Inventory.addProduct(product1);
        product1.addAssociatedPart(Sprocket);
        product1.addAssociatedPart(Chain);
        Product product2 = new Product(2, "Adult Bike", 200.00, 5, 1, 5);
        Inventory.addProduct(product2);
        product2.addAssociatedPart(Sprocket);
        product2.addAssociatedPart(Chain);
        Product product3 = new Product(3, "Moto Bike", 200.00, 5, 1, 5);
        Inventory.addProduct(product3);
        product3.addAssociatedPart(Sprocket);
        product3.addAssociatedPart(Chain);

        launch();
    }
}