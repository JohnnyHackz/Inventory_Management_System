package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;

/**
 * The controller class for the Modify Part screen.
 * Allows the user to modify an existing part's details.
 */
public class ModifyPartController {
    @FXML public ToggleGroup modParts;
    @FXML private RadioButton modInHouse;
    @FXML private RadioButton modOutSource;
    @FXML private TextField modPartId;
    @FXML private TextField modPartInv;
    @FXML private TextField modPartMachineId;
    @FXML private TextField modPartMax;
    @FXML private TextField modPartMin;
    @FXML private TextField modPartName;
    @FXML private TextField modPartPrice;
    @FXML private AnchorPane modPartTableView;
    @FXML private Text machIdOrCompName;

    private int presentIndex = 0;
    public Stage stage;

    /**
     * Handles the action event when the Cancel button is clicked.
     * Returns to the Main Screen.
     * @param event The action event triggered by the Cancel button.
     * @throws IOException if an error occurs while loading the MainScreen.fxml file.
     */
    @FXML
    void onActionCancelMod(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent addPart = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(addPart));
        stage.show();
    }

    /**
     * Handles the action event when the In-House radio button is selected.
     * Updates the label and deselects the Out-Source radio button.
     * @param event The action event triggered by the In-House radio button.
     */
    @FXML
    void onActionModInHouse(ActionEvent event) {
        machIdOrCompName.setText("Machine ID");
        modOutSource.setSelected(false);
    }

    /**
     * Handles the action event when the Out-Source radio button is selected.
     * Updates the label and deselects the In-House radio button.
     * @param event The action event triggered by the Out-Source radio button.
     */
    @FXML
    void onActionModOutSource(ActionEvent event) {
        machIdOrCompName.setText("Company Name");
        modInHouse.setSelected(false);
    }

    /**
     * Handles the action event when the Save button is clicked.
     * Updates the part details based on the input fields and saves the changes.
     * @param event The action event triggered by the Save button.
     */
    @FXML
    private void onActionSaveMod(ActionEvent event) {
        try {
            int id = Integer.parseInt(modPartId.getText());
            String name = modPartName.getText();
            int stock = Integer.parseInt(modPartInv.getText());
            double price = Double.parseDouble(modPartPrice.getText());
            int max = Integer.parseInt(modPartMax.getText());
            int min = Integer.parseInt(modPartMin.getText());
            //Name field must be filled in.
            if (name.isEmpty()) {
                showAlert("Name field must be filled in.");
                return;
            }
            //Min field must be filled in.
            if (modPartMin.getText().isEmpty()) {
                showAlert("Min field must be filled in.");
                return;
            }
            //Max field must be filled in.
            if (modPartMax.getText().isEmpty()) {
                showAlert("Max field must be filled in.");
                return;
            }
            //Min should be less than the max.
            if (min > max) {
                showAlert("Minimum must be less than the Maximum.");
                return;
            }
            //Inventory must be within the Min and Max range.
            else if (stock < min || stock > max) {
                showAlert("The inventory must be within the minimum and maximum range.");
                return;
            }

            int machineID;
            String companyName;

            if (modInHouse.isSelected()) {
                machineID = Integer.parseInt(modPartMachineId.getText());
                InHouse updatedPart = new InHouse(id, name, price, stock, min, max, machineID);
                Inventory.updatePart(presentIndex, updatedPart);
            } else if (modOutSource.isSelected()) {
                companyName = modPartMachineId.getText();
                Outsourced updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.updatePart(presentIndex, updatedPart);
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }   catch (NumberFormatException | IOException e) {
            showAlert("Invalid input format. Please enter valid numeric values.");
        }
    }

    /**
     * Displays an alert with the specified message.
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Sets the part details to be displayed and edited.
     * @param selectedIndex The index of the selected part in the inventory.
     * @param part The part object containing the details to be displayed.
     */
    public void deliverPartDetails(int selectedIndex, Part part) {
        presentIndex = selectedIndex;

        modPartId.setText(String.valueOf(part.getId()));
        modPartName.setText(part.getName());
        modPartInv.setText(String.valueOf(part.getStock()));
        modPartPrice.setText(String.valueOf(part.getPrice()));
        modPartMax.setText(String.valueOf(part.getMax()));
        modPartMin.setText(String.valueOf(part.getMin()));

        if (part instanceof InHouse) {
            modInHouse.setSelected(true);
            modOutSource.setSelected(false);
            machIdOrCompName.setText("Machine ID");
            modPartMachineId.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else if (part instanceof Outsourced) {
            modInHouse.setSelected(false);
            modOutSource.setSelected(true);
            machIdOrCompName.setText("Company Name");
            modPartMachineId.setText(((Outsourced) part).getCompanyName());
        }
    }

}