package product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDb extends ProductModel {
    private static void createProductsTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "value REAL NOT NULL," +
                "quantity INTEGER NOT NULL" +
                ");";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    public void insertProduct(Connection connection, ProductModel productModel) throws SQLException {
        String sql = "INSERT INTO products (name, value, quantity) VALUES (?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productModel.getName());
            statement.setFloat(2, productModel.getValue());
            statement.setInt(3, productModel.getQuantity());
            statement.execute();
        }
    }

    public List<ProductModel> getAllProducts(Connection connection) throws SQLException {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM products;";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ProductModel product = new ProductModel();

                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setValue(resultSet.getFloat("value"));
                product.setQuantity(resultSet.getInt("quantity"));
                products.add(product);
            }
        }
        return products;
    }

    public void updateProduct(Connection connection, ProductModel productModel, int id) throws SQLException {
        String sql = "UPDATE products SET name = ?, value = ?, quantity = ? WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productModel.getName());
            statement.setFloat(2, productModel.getValue());
            statement.setInt(3, productModel.getQuantity());
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    public void deleteProduct(Connection connection, int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public static boolean checkProductId(Connection connection, int id) throws SQLException {
        String sql = "SELECT id FROM products where id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            statement.setInt(1, id);

            statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }
    }

    public static void initProductModel(Connection connection) throws SQLException {
        createProductsTable(connection);
    }
}