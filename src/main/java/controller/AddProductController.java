package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * The controller class for the Add Product screen.
 * Allows the user to add a new product to the inventory.
 */
public class AddProductController implements Initializable {

    private final ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    @FXML private TableView<Part> addProductTableView;
    @FXML private TableColumn<Part, Integer> addInvLevelCol;
    @FXML private TableColumn<Part, Integer> addPartIDCol;
    @FXML private TableColumn<Part, String> addPartNameCol;
    @FXML private TableColumn<Part, Double> addPriceCostCol;
    @FXML private TextField addProductInv;
    @FXML private TextField addProductMax;
    @FXML private TextField addProductMin;
    @FXML private TextField addProductName;
    @FXML private TextField addProductPrice;
    @FXML private TextField addProductSearchBox;
    @FXML private TableView <Part> assocPartTableView;
    @FXML private TableColumn<?, ?> assocInvCol;
    @FXML private TableColumn<?, ?> assocPartIdCol;
    @FXML private TableColumn<?, ?> assocPartNameCol;
    @FXML private TableColumn<?, ?> assocPriceCostCol;


    Stage stage;

    /**
     * Handles the action event when the search button is clicked.
     * Searches for parts based on the entered text and populates the table view with the search results.
     *
     * @param event The action event triggered by clicking the search button.
     * @return Nothing.
     */
    @FXML
    void addProductSearch(ActionEvent event) {
        String searchProductText = addProductSearchBox.getText();
        ObservableList<Part> outcome = Inventory.lookupPart(searchProductText);
        try{
            while(outcome.size() == 0){
                int partID = Integer.parseInt(searchProductText);
                outcome.add(Inventory.lookupPart(partID));
            }
            addProductTableView.setItems(outcome);
        } catch(NumberFormatException e){
            Alert invalidParts = new Alert(Alert.AlertType.ERROR);
            invalidParts.setTitle("Error");
            invalidParts.setContentText("The part you entered was not found.");
            invalidParts.showAndWait();
        }

    }

    /**
     * Handles the action event when the Add button is clicked.
     * Adds the selected part to the associated parts of the product.
     *
     * @param event The action event triggered by clicking the Add button.
     * @return Nothing.
     */
    @FXML
    void onActionAddProduct(ActionEvent event) {
        Part chosenPart = addProductTableView.getSelectionModel().getSelectedItem();
        assocPartList.add(chosenPart);
        assocPartTableView.setItems(assocPartList);
    }

    /**
     * Handles the action event when the Cancel button is clicked.
     * Displays a confirmation dialog and returns to the Main Screen if confirmed.
     *
     * @param event The action event triggered by clicking the Cancel button.
     * @throws IOException if an error occurs while loading the MainScreen.fxml file.
     * @return Nothing.
     */
    @FXML
    void onActionCancelAssocPart(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
            Parent mainScreen = loader.load();
            stage.setScene(new Scene(mainScreen));
            stage.show();
        }
    }

    /**
     * Handles the action event when the Remove button is clicked.
     * Removes the selected associated part from the product.
     *
     * @param event The action event triggered by clicking the Remove button.
     * @return Nothing.
     */
    @FXML
    void onActionRemoveAssocPart(ActionEvent event) {
        Part partsSelected = assocPartTableView.getSelectionModel().getSelectedItem();

        if (partsSelected != null && assocPartList.contains(partsSelected)) {
            assocPartList.remove(partsSelected);
        } else {
            showErrorAlert();
        }
    }

    /**
     * Displays an error alert.
     *
     * @return Nothing.
     */
    void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a valid part from the list.");
        alert.showAndWait();
    }

    /**
     * Handles the action event when the Save button is clicked.
     * Saves the new product details and returns to the Main Screen.
     * @param event The action event triggered by clicking the Save button.
     * @return Nothing.
     */
    @FXML
    public void onActionSaveProduct(ActionEvent event){
        try {
            // Generate a unique ID for the product
            int uniqueID = (int) (Math.random() * 100);

            if(addProductName.getText().isEmpty()){
                showAlert("Name field must be filled in.");
                return;
            }

            String name = addProductName.getText();
            int stock = Integer.parseInt(addProductInv.getText());
            double price = Double.parseDouble(addProductPrice.getText());
            int max = Integer.parseInt(addProductMax.getText());
            int min = Integer.parseInt(addProductMin.getText());

            if (min > max) {
                showAlert("Minimum must be less than the Maximum.");
                return;
            }


            if (stock < min || stock > max) {
                showAlert("The inventory must be within the minimum and maximum range.");
                return;
            }

            Product product = new Product(uniqueID, name, price, stock, min, max);

            for (Part part : assocPartList) {
                product.addAssociatedPart(part);
            }

            // Add the product to the Inventory
            Inventory.getAllProducts().add(product);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
            Parent mainScreen = loader.load();
            stage.setScene(new Scene(mainScreen));
            stage.show();
        } catch (NumberFormatException | IOException e) {
            showAlert("Invalid input format. Please enter valid numeric values.");
        }
    }

    /**
     * Displays an alert with the specified message.
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Initializes the Add Product screen.
     * This method is automatically called after the FXML file has been loaded.
     * It sets up the initial state of the screen and populates the table views with data.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Add parts to the parts table (top).
        assocPartTableView.setItems(assocPartList);
        addProductTableView.setItems(Inventory.getAllParts());
        addPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Add parts to associated parts table (bottom).
        assocPartTableView.setItems(assocPartList);
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
