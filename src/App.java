import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import product.ProductDb;
import product.ProductModel;

public class App {
    public static void main(String[] args) {
        try (Connection connection = Database.getConnection(); Scanner scanner = new Scanner(System.in)) {
            ProductDb.initProductModel(connection);

            while (true) {
                System.out.println("\n---------- Product Manager ----------");
                System.out.println("1 - Add product");
                System.out.println("2 - List products");
                System.out.println("3 - Update product");
                System.out.println("4 - Delete product");
                System.out.println("5 - Exit");
                System.out.print("Choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addProduct(connection, scanner);
                        break;
                    case 2:
                        listProducts(connection);
                        break;
                    case 3:
                        updateProduct(connection, scanner);
                        break;
                    case 4:
                        deleteProduct(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Goodbye...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addProduct(Connection connection, Scanner scanner) throws SQLException {
        try {
            ProductDb product = new ProductDb();
            System.out.print("Product name: ");
            product.setName(scanner.nextLine());
            System.out.print("Product value: ");
            product.setValue(scanner.nextFloat());
            System.out.print("Product quantity: ");
            product.setQuantity(scanner.nextInt());
            product.insertProduct(connection, product);
            System.out.println("\n -=-=-=> Product added successfully! <=-=-=-");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listProducts(Connection connection) throws SQLException {
        List<ProductModel> products = new ProductDb().getAllProducts(connection);
        if (products.isEmpty()) {
            System.out.println("No products encountered.");
        } else {
            System.out.println("\n<<---------- Products list ---------->>");
            for (ProductModel product : products) {
                System.out.println("Id : " + product.getId());
                System.out.println("Name : " + product.getName());
                System.out.println("Value : " + product.getValue());
                System.out.println("Quantity : " + product.getQuantity());
                System.out.println();
            }
            System.out.println("<<---------- Products End ---------->>");
        }
    }

    private static void updateProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Id of product: ");
        int id = scanner.nextInt();
        if (!ProductDb.checkProductId(connection, id)) {
            System.out.println("Invalid Id");
            System.exit(1);
        }

        scanner.nextLine();
        ProductDb product = new ProductDb();
        System.out.print("New product name: ");
        product.setName(scanner.nextLine());
        System.out.print("New product value: ");
        product.setValue(scanner.nextFloat());
        System.out.print("New product quantity: ");
        product.setQuantity(scanner.nextInt());

        product.updateProduct(connection, product, id);
        System.out.println("\n -=-=-=> Product updated successfully! <=-=-=-");
    }

    private static void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Id of product: ");
        int id = scanner.nextInt();
        ProductDb product = new ProductDb();
        product.deleteProduct(connection, id);
        System.out.println("\n -=-=-=> Product deleted successfully! <=-=-=-");
    }
}
