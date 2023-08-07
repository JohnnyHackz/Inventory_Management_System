package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Product class represents a product in the inventory.
 * It contains information about the product and associated parts.
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id, stock, min, max;
    private double price;
    private String name;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.price = price;
        this.name = name;
    }

    /**
     * Retrieves the list of associated parts with the product.
     * @return The list of associated parts.
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     * Sets the list of associated parts for the product.
     * @param associatedParts The list of associated parts to set.
     */
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    /**
     * Retrieves the ID of the product.
     * @return The product ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the product.
     * @param id The product ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the stock level of the product.
     * @return The stock level.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock level of the product.
     * @param stock The stock level to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Retrieves the minimum allowed stock level of the product.
     * @return The minimum stock level.
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum allowed stock level of the product.
     * @param min The minimum stock level to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Retrieves the maximum allowed stock level of the product.
     * @return The maximum stock level.
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximum allowed stock level of the product.
     * @param max The maximum stock level to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Retrieves the price of the product.
     * @return The price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     * @param price The price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the name of the product.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a part to the list of associated parts for the product.
     * @param part The part to add.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from the list of associated parts for the product.
     * @param selectedAssociatedPart The associated part to delete.
     * @return True if the part was successfully deleted, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.removeIf(part -> part.getId() == selectedAssociatedPart.getId());
        //return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Retrieves all the associated parts for the product.
     * @return The list of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}