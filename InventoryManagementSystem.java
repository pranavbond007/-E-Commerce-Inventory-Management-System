import java.io.*;
import java.util.*;

class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Price: %.2f, Quantity: %d", id, name, price, quantity);
    }
}

public class InventoryManagementSystem {
    private static Map<Integer, Product> inventory = new HashMap<>();
    private static final String FILE_NAME = "inventory.ser";
    private static int productIdCounter = 1;

    public static void main(String[] args) {
        loadInventory();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== E-Commerce Inventory Management System ===");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product Quantity");
            System.out.println("3. Delete Product");
            System.out.println("4. Search Product");
            System.out.println("5. Display All Products");
            System.out.println("6. Save and Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addProduct(scanner);
                case 2 -> updateProduct(scanner);
                case 3 -> deleteProduct(scanner);
                case 4 -> searchProduct(scanner);
                case 5 -> displayAllProducts();
                case 6 -> {
                    saveInventory();
                    System.out.println("Inventory saved. Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.next();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();

        Product product = new Product(productIdCounter++, name, price, quantity);
        inventory.put(product.getId(), product);
        System.out.println("Product added successfully!");
    }

    private static void updateProduct(Scanner scanner) {
        System.out.print("Enter product ID to update: ");
        int id = scanner.nextInt();
        if (inventory.containsKey(id)) {
            System.out.print("Enter new quantity: ");
            int newQuantity = scanner.nextInt();
            inventory.get(id).setQuantity(newQuantity);
            System.out.println("Product quantity updated successfully!");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        int id = scanner.nextInt();
        if (inventory.remove(id) != null) {
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void searchProduct(Scanner scanner) {
        System.out.print("Enter product name to search: ");
        String name = scanner.next().toLowerCase();
        boolean found = false;

        for (Product product : inventory.values()) {
            if (product.getName().toLowerCase().contains(name)) {
                System.out.println(product);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No product found with the given name.");
        }
    }

    private static void displayAllProducts() {
        if (inventory.isEmpty()) {
            System.out.println("No products in the inventory.");
        } else {
            for (Product product : inventory.values()) {
                System.out.println(product);
            }
        }
    }

    private static void saveInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    private static void loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            inventory = (Map<Integer, Product>) ois.readObject();
            productIdCounter = inventory.size() + 1;
        } catch (FileNotFoundException e) {
            System.out.println("No previous inventory file found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }
}
