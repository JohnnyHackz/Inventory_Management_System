package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Inventory class represents the inventory system of the application.
 * It manages the parts and products in the inventory.
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a part to the inventory.
     * @param newestPart The part to add to the inventory.
     */
    public static void addPart(Part newestPart) {
        allParts.add(newestPart);
    }

    /**
     * Retrieves all parts in the inventory.
     * @return An observable list containing all parts in the inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Adds a product to the inventory.
     * @param newestProduct The product to add to the inventory.
     */
    public static void addProduct(Product newestProduct) {
        allProducts.add(newestProduct);
    }

    /**
     * Retrieves all products in the inventory.
     * @return An observable list containing all products in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Searches for a part in the inventory based on the part ID.
     * @param partID The ID of the part to search for.
     * @return The found part, or null if no part is found with the specified ID.
     */
    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    /**
     * Searches for a product in the inventory based on the product ID.
     * @param id The ID of the product to search for.
     * @return The found product, or null if no product is found.
     */
    public static Product lookupProduct(int id) {
        for (Product product : allProducts) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    /**
     * Searches for parts in the inventory based on the part name.
     * @param partName The name of the part to search for.
     * @return An observable list of parts that match the search criteria.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> PartName = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                PartName.add(part);
            }
        }
        return PartName;
    }

    /**
     * Searches for products in the inventory based on the product name.
     * @param productName The name of the product to search for.
     * @return An observable list of products that match the search criteria.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productNameList = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                productNameList.add(product);
            }
        }
        return productNameList;
    }

    /**
     * Updates a part in the inventory at the specified index.
     * @param index        The index of the part to update.
     * @param selectedPart The updated part to replace the existing part.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product in the inventory at the specified index.
     * @param index           The index of the product to update.
     * @param selectedProduct The updated product to replace the existing product.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes a part from the inventory.
     * @param selectedPart The part to delete from the inventory.
     * @return True if the part is successfully deleted and false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes a product from the inventory.
     * @param selectedProduct The product to delete from the inventory.
     * @return True if the product is successfully deleted, false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }
}