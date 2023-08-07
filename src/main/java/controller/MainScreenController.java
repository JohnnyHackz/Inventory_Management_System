package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * The MainScreenController class controls the main screen of the application.
 * It implements the Initializable interface for initialization after loading the FXML file.
 */
public class MainScreenController implements Initializable {

    @FXML private AnchorPane IMSTableView;
    @FXML private Button addPartButton;
    @FXML private Button addProductButton;
    @FXML private Button deletePartButton;
    @FXML private Button deleteProductButton;
    @FXML private Button exitMain;
    @FXML private TableView<Part> mainScreenPartsTable;
    @FXML private TableColumn<Part, Integer> partIDCol;
    @FXML private TableColumn<Part, Integer> partInventoryCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Double> partPriceCol;
    @FXML private TableView<Product> mainScreenProductsTable;
    @FXML private Button modifyPartButton;
    @FXML private Button modifyProductButton;
    @FXML private TextField partSearchBox;
    @FXML private Label partTable;
    @FXML private AnchorPane partTableView;
    @FXML private TableColumn<Product, Integer> productIDCol;
    @FXML private TableColumn<Product, Integer> productInventoryCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Double> productPriceCol;
    @FXML private TextField productSearchBox;
    @FXML private Label productTable;
    @FXML private AnchorPane productTableView;

    Stage stage;

    /**
     * Initializes the main screen controller.
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainScreenPartsTable.setItems(Inventory.getAllParts());
        mainScreenProductsTable.setItems(Inventory.getAllProducts());
    }

    /**
     * Handles the event when the "Add" button for parts is clicked.
     * Opens the Add Part screen.
     * @param event The event representing the action of clicking the button.
     * @throws IOException If an error occurs during loading of the Add Part screen.
     */
    @FXML
    void mainScreenAddPartsButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent addPart = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(addPart));
        stage.show();

        System.out.println("add part");
    }

    /**
     * Handles the event when the "Add" button for products is clicked.
     * Opens the Add Product screen.
     * @param event The event representing the action of clicking the button.
     * @throws IOException If an error occurs during loading of the Add Product screen.
     */
    @FXML
    void mainScreenAddProductsButton(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent addPart = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(addPart));
        stage.show();

        System.out.println("add Prod");
    }

    /**
     * Handles the event when the "Delete" button for products is clicked.
     * Deletes the selected product from the inventory.
     * @param event The event representing the action of clicking the button.
     * @throws IOException If an error occurs during the deletion of the product.
     */
    @FXML
    void mainScreenDeleteProductButton(ActionEvent event) throws IOException {
        Product chosenProduct = mainScreenProductsTable.getSelectionModel().getSelectedItem();

        if (chosenProduct == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: Please select a product.");
            alert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product");

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (chosenProduct.getAllAssociatedParts().size() > 0) {
                Alert notAllowedDelete = new Alert(Alert.AlertType.ERROR);
                notAllowedDelete.setTitle("Error");
                notAllowedDelete.setContentText("ERROR: Cannot delete a product with associated parts.");
                notAllowedDelete.showAndWait();
                return;
            }
            Inventory.deleteProduct(chosenProduct);
        }
    }

    /**
     * Handles the event when the "Modify" button for parts is clicked.
     * Opens the Modify Part screen.
     * @param event The event representing the action of clicking the button.
     * @throws IOException If an error occurs during loading of the Modify Part screen.
     */
    @FXML
    void mainScreenModifyPartsButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            ModifyPartController MPController = loader.getController();
            Part selectedPart = mainScreenPartsTable.getSelectionModel().getSelectedItem();
            int selectedIndex = mainScreenPartsTable.getSelectionModel().getSelectedIndex();
            MPController.deliverPartDetails(selectedIndex, selectedPart);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error: Select a part first");
            alert.show();
        }
    }

    /**
     * Handles the event when the "Modify" button for products is clicked.
     * Opens the Modify Product screen.
     * @param event The event representing the action of clicking the button.
     * @throws IOException If an error occurs during loading of the Modify Product screen.
     */
    @FXML
    void mainScreenModifyProductsButton(ActionEvent event) throws IOException {
        try {
            if (mainScreenProductsTable.getSelectionModel().getSelectedItem() == null) {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR, "Error: No Part was selected.");
                alert.setTitle("Error");
                alert.showAndWait();
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
                loader.load();
                ModifyProductController modProductController = loader.getController();
                modProductController.deliverProductDetails(mainScreenProductsTable.getSelectionModel().getSelectedItem());
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the event when the "Delete" button for parts is clicked.
     * Deletes the selected part from the inventory.
     * @param event The event representing the action of clicking the button.
     * @throws IOException If an error occurs during the deletion of the part.
     */
    @FXML
    void mainScreenDeletePartButton(ActionEvent event) throws IOException {
        Part part = mainScreenPartsTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("ERROR: No part selected");
            alert.showAndWait();
        } else {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                Inventory.deletePart(part);
            } else {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /**
     * Handles the event when the "Exit" button is clicked.
     * Closes the application.
     * @param exitButtonPush The event representing the action of clicking the button.
     */
    @FXML
    void mainScreenExitButton(ActionEvent exitButtonPush) {
        Stage stage = (Stage) ((Node) exitButtonPush.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the event when the search button for parts is clicked.
     * Performs a search based on the entered search term and displays the results in the parts table.
     * @param event The event representing the action of clicking the search button.
     */
    @FXML
    void mainScreenPartSearch(ActionEvent event) {
        String searchTerm = partSearchBox.getText().trim();

        if (searchTerm.isEmpty()) {
            resetPartsTable();
        } else {
            ObservableList<Part> searchResults = FXCollections.observableArrayList();
            try {
                int partId = Integer.parseInt(searchTerm);
                Part foundPart = Inventory.lookupPart(partId);
                if (foundPart != null) {
                    searchResults.add(foundPart);
                }
            } catch (NumberFormatException e) {
                for (Part part : Inventory.getAllParts()) {
                    if (part.getName().toLowerCase().startsWith(searchTerm.toLowerCase())) {
                        searchResults.add(part);
                    }
                }
            }
            mainScreenPartsTable.setItems(searchResults);
            if (searchResults.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: No parts found matching the search term.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Resets the parts table to its original state.
     * Clears the search box text and displays all parts in the table.
     */
    @FXML
    void resetPartsTable() {
        partSearchBox.setText("");
        mainScreenPartsTable.setItems(Inventory.getAllParts());
    }

    /**
     * Handles the event when the search button for products is clicked.
     * Performs a search based on the entered search term and displays the results in the products table.
     * @param event The event representing the action of clicking the search button.
     */
    @FXML
    void mainScreenProductSearch(ActionEvent event) {
        String searchTerm = productSearchBox.getText().trim();

        if (searchTerm.isEmpty()) {
            resetProductTable();
        } else {
            ObservableList<Product> searchResults = FXCollections.observableArrayList();
            try {
                int productId = Integer.parseInt(searchTerm);
                Product foundProduct = Inventory.lookupProduct(productId);
                if (foundProduct != null) {
                    searchResults.add(foundProduct);
                }
            } catch (NumberFormatException e) {
                for (Product product : Inventory.getAllProducts()) {
                    if (product.getName().toLowerCase().startsWith(searchTerm.toLowerCase())) {
                        searchResults.add(product);
                    }
                }
            }
            mainScreenProductsTable.setItems(searchResults);
            productSearchBox.setText("");

            if (searchResults.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: No products found matching the search term.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Resets the products table to its original state.
     * Clears the search box text and displays all products in the table.
     */
    @FXML
    void resetProductTable() {
        productSearchBox.setText("");
        mainScreenProductsTable.setItems(Inventory.getAllProducts());
        mainScreenProductsTable.getSelectionModel().clearSelection();
    }
}