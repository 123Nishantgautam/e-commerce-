package main;

import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String URL =
            "jdbc:mysql://localhost:3307/ecommerce_db";

    static final String USER = "root";
    static final String PASSWORD = "765546";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    // A. Establish Connection
    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }
    }

    // B. Add Order
    public static void addOrder() {
        try {
            String sql = "INSERT INTO Orders " +
                    "(customer_name, product_name, quantity, unit_price, status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter Customer Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Product Name: ");
            String product = sc.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();

            System.out.print("Enter Unit Price: ");
            double price = sc.nextDouble();
            sc.nextLine();

            System.out.print("Enter Status: ");
            String status = sc.nextLine();

            ps.setString(1, name);
            ps.setString(2, product);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setString(5, status);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Order Added Successfully!");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // C. Display All Orders
    public static void displayOrders() {
        try {
            String sql = "SELECT * FROM Orders";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n===== ALL ORDERS =====");

            while (rs.next()) {
                System.out.println("\nOrder ID: " +
                        rs.getInt("order_id"));

                System.out.println("Customer Name: " +
                        rs.getString("customer_name"));

                System.out.println("Product Name: " +
                        rs.getString("product_name"));

                System.out.println("Quantity: " +
                        rs.getInt("quantity"));

                System.out.println("Unit Price: " +
                        rs.getDouble("unit_price"));

                System.out.println("Status: " +
                        rs.getString("status"));
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // D. Search Order
    public static void searchOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT * FROM Orders WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Order ID: " +
                        rs.getInt("order_id"));

                System.out.println("Customer: " +
                        rs.getString("customer_name"));

                System.out.println("Product: " +
                        rs.getString("product_name"));

                System.out.println("Quantity: " +
                        rs.getInt("quantity"));

                System.out.println("Price: " +
                        rs.getDouble("unit_price"));

                System.out.println("Status: " +
                        rs.getString("status"));
            } else {
                System.out.println("Order Not Found!");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // E. Update Order Status
    public static void updateStatus() {
        try {
            System.out.print("Enter Order ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Status: ");
            String status = sc.nextLine();

            String sql =
                    "UPDATE Orders SET status = ? WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Status Updated Successfully!");
            } else {
                System.out.println("Order Not Found!");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // F. Delete Order
    public static void deleteOrder() {
        try {
            System.out.print("Enter Order ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            String sql = "DELETE FROM Orders WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Order Deleted Successfully!");
            } else {
                System.out.println("Order Not Found!");
            }

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Main Method
    public static void main(String[] args) {

        connect();

        if (con == null) {
            return;
        }

        int choice;

        do {
            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Add New Order");
            System.out.println("2. Display All Orders");
            System.out.println("3. Search Order");
            System.out.println("4. Update Order Status");
            System.out.println("5. Delete Order");
            System.out.println("6. Exit");

            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addOrder();
                    break;

                case 2:
                    displayOrders();
                    break;

                case 3:
                    searchOrder();
                    break;

                case 4:
                    updateStatus();
                    break;

                case 5:
                    deleteOrder();
                    break;

                case 6:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 6);

        try {
            con.close();
            sc.close();
            System.out.println("Database Connection Closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
