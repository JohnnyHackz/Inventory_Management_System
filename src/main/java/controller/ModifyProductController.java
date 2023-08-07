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
 * The controller class for the Modify Product screen.
 * Allows the user to modify an existing product's details.
 */
public class ModifyProductController implements Initializable {
    @FXML private TableColumn<Part, Integer> modAssocInvLevel;
    @FXML private TableColumn<Part, Integer> modAssocPartId;
    @FXML private TableColumn<Part, String> modAssocPartName;
    @FXML private TableColumn<Part, Double> modAssocPricePerUnit;
    @FXML private TableColumn<Part, Integer> modInvLevel;
    @FXML private TableColumn<Part, Integer> modPartID;
    @FXML private TableColumn<Part, String> modPartName;
    @FXML private TableColumn<Part, Double> modPricePerUnit;
    @FXML private TextField modProductId;
    @FXML private TextField modProductInv;
    @FXML private TextField modProductMax;
    @FXML private TextField modProductMin;
    @FXML private TextField modProductName;
    @FXML private TextField modProductPrice;
    @FXML private TextField modSearchPartId;
    @FXML private TableView<Part> modAssocPartTableview;
    @FXML private TableView<Part> modProductTableView;

    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();
    private Product swapProduct;
    private int currentIndex = 0;

    private Stage stage;

    /**
     * Sets the product to be modified.
     * @param product The product to be modified.
     */
    public void setSwapProduct(Product product) {
        this.swapProduct = product;
        currentIndex = Inventory.getAllProducts().indexOf(product);
    }

    /**
     * Handles the action event when the Cancel button is clicked.
     * Returns to the Main Screen.
     * @param event The action event triggered by the Cancel button.
     * @throws IOException if an error occurs while loading the MainScreen.fxml file.
     */
    @FXML
    void onActionCancelAssoc(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent addPart = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(addPart));
        stage.show();
    }

    /**
     * Handles the action event when the Add button is clicked.
     * Adds a selected part to the associated parts of the product.
     * @param event The action event triggered by the Add button.
     */
    @FXML
    void onActionModAdd(ActionEvent event) {
        Part chosenPart = modProductTableView.getSelectionModel().getSelectedItem();
        if (chosenPart != null) {
            assocPartList.add(chosenPart);
            modAssocPartTableview.setItems(assocPartList);
        }
    }

    /**
     * Handles the action event when the Remove button is clicked.
     * LOGICAL ERROR: The issues was in onActionModRemoveAssocPart method of the ModifyProductController class.
     * There was a logical error where the associated part was not being removed from the assocPartList correctly.
     * It was attempting to remove the selected part directly from the assocPartList, which was causing
     * an inconsistency with the associated parts of the product.
     * I fixed this issue by updating the code to remove the part from both the assocPartList and the swapProduct object
     * by calling the deleteAssociatedPart method. This ensures that the associated part is properly removed
     * from both the list and the product.
     * @throws UnsupportedOperationException if no associated part is selected.
     */
    @FXML
    void onActionModRemoveAssocPart(ActionEvent event) {
            Part selectedPart = modAssocPartTableview.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Remove Associated Part");
            alert.setContentText("Are you sure you want to remove this part from the associated parts?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                swapProduct.deleteAssociatedPart(selectedPart);
                assocPartList.remove(selectedPart);
            }
            } else {
                throw new UnsupportedOperationException("No associated part selected.");
            }
    }

    /**
     * Handles the action event when the Search button is clicked.
     * Searches for a part based on the entered part ID or name.
     * @param event The action event triggered by the Search button.
     */
    @FXML
    void onActionModSearchPartId(ActionEvent event) {
        String searchText = modSearchPartId.getText().trim();

        if (searchText.isEmpty()) {
            showAlert("Please enter a part ID or name to search.");
            return;
        }

        try {
            int searchId = Integer.parseInt(searchText);
            Part foundPart = Inventory.lookupPart(searchId);

            if (foundPart != null) {
                selectAndScrollToPart(foundPart);
            } else {
                searchByName(searchText);
            }
        } catch (NumberFormatException e) {
            searchByName(searchText);
        }
    }

    /**
     * Searches for a part by name and updates the table view accordingly.
     * @param searchText The name of the part to search for.
     * @throws UnsupportedOperationException if no part is found with the specified ID or name.
     */
    private void searchByName(String searchText) {
        ObservableList<Part> parts = Inventory.getAllParts();
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();

        for (Part part : parts) {
            if (part.getName().equalsIgnoreCase(searchText)) {
                matchingParts.add(part);
            }
        }

        if (!matchingParts.isEmpty()) {
            modProductTableView.setItems(matchingParts);
            modProductTableView.getSelectionModel().selectFirst();
            modProductTableView.scrollTo(0);
        } else {
            throw new UnsupportedOperationException("No part found with the specified ID or name.");
        }
    }

    /**
     * Selects and scrolls to the specified part in the product table view.
     * @param part The part to select and scroll to.
     */
    private void selectAndScrollToPart(Part part) {
        modProductTableView.getSelectionModel().select(part);
        modProductTableView.scrollTo(part);
    }

    /**
     * Handles the action event when the Save button is clicked.
     * Saves the modified product details and returns to the Main Screen.
     * @param event The action event triggered by the Save button.
     */
    @FXML
    void onActionSaveAssoc(ActionEvent event) {
        try {
            if (modProductName.getText().isEmpty()) {
                showAlert("Name field must be filled in.");
                return;
            }

            if (modProductMin.getText().isEmpty()) {
                showAlert("Min field must be filled in.");
                return;
            }

            if (modProductMax.getText().isEmpty()) {
                showAlert("Max field must be filled in.");
                return;
            }

            int id = Integer.parseInt(modProductId.getText());
            String name = modProductName.getText();
            int inventory = Integer.parseInt(modProductInv.getText());
            double price = Double.parseDouble(modProductPrice.getText());
            int max = Integer.parseInt(modProductMax.getText());
            int min = Integer.parseInt(modProductMin.getText());

            if (min > max) {
                showAlert("Minimum must be less than the Maximum.");
                return;
            }

            if (inventory < min || inventory > max) {
                showAlert("The inventory must be within the minimum and maximum range.");
                return;
            }

            Product revisedProduct = new Product(id, name, price, inventory, min, max);
            Inventory.updateProduct(currentIndex, revisedProduct);

            //This for loop ensures that the associated parts of the product are also updated in the revisedProduct.
            // It adds each Part from the assocPartList to the revisedProduct by calling the addAssociatedPart method.
            // This way, the modified product retains its associated parts.
            for (Part part : assocPartList) {
                revisedProduct.addAssociatedPart(part);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
            Parent mainScreen = loader.load();
            Scene scene = new Scene(mainScreen);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (NumberFormatException | IOException e) {
            showAlert("Invalid input format. Please enter valid numeric values.");
        }
    }

    /**
     * Displays an alert with the specified error message.
     * @param message The error message to display.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the product details for modification.
     * @param product The product to be modified.
     */
    public void deliverProductDetails(Product product) {
        modProductId.setText(String.valueOf(product.getId()));
        modProductName.setText(product.getName());
        modProductInv.setText(String.valueOf(product.getStock()));
        modProductPrice.setText(String.valueOf(product.getPrice()));
        modProductMax.setText(String.valueOf(product.getMax()));
        modProductMin.setText(String.valueOf(product.getMin()));
        setSwapProduct(product);
        //assocPartList.clear(); // Clear the list before adding associated parts
       //assocPartList.setAll(product.getAssociatedParts());


        assocPartList.addAll(product.getAssociatedParts()); // Add associated parts to the existing list

    }

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProductTableView.setItems(Inventory.getAllParts());
        modPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize assocPartList
        assocPartList = FXCollections.observableArrayList();

        // Add parts to associated table (bottom)
        modAssocPartTableview.setItems(assocPartList);
        modAssocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAssocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAssocInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modAssocPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}