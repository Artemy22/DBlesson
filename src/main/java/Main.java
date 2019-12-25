/*
встановити MySQL сервер -
запустити код з лекції та додати до нього 2 методи посиланн на лекцію https://drive.google.com/file/d/1rh3NUva8xp-WSt2oqPwlE3LKbFgFfFWl/view?usp=sharing
Написать код для добавления новых сотрудников
Написать код для выборки сотрудников по зарплате
 */

import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:13306/Test2";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "root";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add worker");
                    System.out.println("2: view workers with salary more than 1000");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    if ("1".equals(s)) {
                        addWorker(sc);
                    } else if ("2".equals(s)) {
                        viewWorkers();
                    } else {
                        return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS workers");
            st.execute("CREATE TABLE workers (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) NOT NULL, email VARCHAR(20), phone INT, salary INT)");
        } finally {
            st.close();
        }
    }

    private static void addWorker(Scanner sc) throws SQLException {
        System.out.print("Enter worker name: ");
        String name = sc.nextLine();
        System.out.print("Enter worker email: ");
        String email = sc.nextLine();
        System.out.print("Enter worker phone (!!!only digits): ");
        int phone = Integer.parseInt(sc.nextLine());
        System.out.print("Enter worker salary (!!!only digits): ");
        int salary = Integer.parseInt(sc.nextLine());

        PreparedStatement ps = conn.prepareStatement("INSERT INTO workers (name, email, phone, salary) VALUES(?, ?, ?, ?)");
        try {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, phone);
            ps.setInt(4, salary);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void viewWorkers() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM workers WHERE salary > 1000");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }
}