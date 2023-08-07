package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller that defines the AddPartController class.
 * Allows the user to add Part details.
 */
public class AddPartController implements Initializable {

    @FXML private Label machineIdOrCompanyName;
    @FXML private TextField InventoryTxt;
    @FXML private TextField MachineIdTxt;
    @FXML private TextField MaxTxt;
    @FXML private TextField MinTxt;
    @FXML private TextField NameTxt;
    @FXML private Button PartCancelButton;
    @FXML private Button PartSaveButton;
    @FXML private TextField PriceTxt;
    @FXML private AnchorPane addPartTableView;
    @FXML private ToggleGroup addPartTg;
    @FXML private TextField partIdTxt;
    @FXML private RadioButton partInHouse;
    @FXML private RadioButton partOutsourced;

    Stage stage;

    /**
     * Handles the action event when the In-House option is selected.
     * @param event The action event triggered by selecting the In-House option.
     */
    @FXML
    public void OnActionPartAddInHouse(ActionEvent event) {
        machineIdOrCompanyName.setText("Machine ID");
        partOutsourced.setSelected(false);
    }

    /**
     * Handles the action event when the Outsourced option is selected.
     * @param event The action event triggered by selecting the Outsourced option.
     */
    @FXML
    public void OnActionPartAddOutsourced(ActionEvent event) {
        machineIdOrCompanyName.setText("Company Name");
        partInHouse.setSelected(false);
    }

    /**
     * Handles the action event when the Cancel button is clicked.
     * Displays a confirmation dialog and returns to the Main Screen if confirmed.
     * @param event The action event triggered by clicking the Cancel button.
     * @throws IOException if an error occurs while loading the MainScreen.fxml file.
     */
    @FXML
    public void onActionCancelPartButton(ActionEvent event) throws IOException {
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
     * Handles the action event when the Save button is clicked.
     * Saves the new part details and returns to the Main Screen.
     * @param event The action event triggered by clicking the Save button.
     * @return Nothing.
     */
    @FXML
    public void onActionSavePartButton(ActionEvent event) {
        try {
            if (!(partInHouse.isSelected() || partOutsourced.isSelected())) {
                showAlert("InHouse or Outsourced must be selected.");
                return;
            }

            if (NameTxt.getText().isEmpty()) {
                showAlert("Name field must be filled in.");
                return;
            }

            if (MinTxt.getText().isEmpty()) {
                showAlert("Min field must be filled in.");
                return;
            }

            if (MaxTxt.getText().isEmpty()) {
                showAlert("Max field must be filled in.");
                return;
            }

            int Id = (int) (Math.random() * 1000);
            String name = NameTxt.getText();
            int stock = Integer.parseInt(InventoryTxt.getText());
            double price = Double.parseDouble(PriceTxt.getText());
            int max = Integer.parseInt(MaxTxt.getText());
            int min = Integer.parseInt(MinTxt.getText());

            if (min > max) {
                showAlert("Minimum must be less than the Maximum.");
                return;
            }

            if (stock < min || stock > max) {
                showAlert("The inventory must be within the minimum and maximum range.");
                return;
            }

            if (partInHouse.isSelected()) {
                int machineID = Integer.parseInt(MachineIdTxt.getText());
                InHouse addPart = new InHouse(Id, name, price, stock, min, max, machineID);
                Inventory.addPart(addPart);
            } else if (partOutsourced.isSelected()) {
                String companyName = MachineIdTxt.getText();
                Outsourced addPart = new Outsourced(Id, name, price, stock, min, max, companyName);
                Inventory.addPart(addPart);
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
     * Displays an alert with the specified message.
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     Initializes the controller class.
     This method is called automatically after the FXML file has been loaded.
     @param url The URL of the FXML file.
     @param resourceBundle The ResourceBundle associated with the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}