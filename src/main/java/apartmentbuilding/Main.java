package apartmentbuilding;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Connection conn;

    public static void main(String[] args) {
        DBConect preConn = new DBConect();

        Scanner sc = new Scanner(System.in);

        try {
            try {
                conn = DriverManager.getConnection(preConn.getUrl(), preConn.getUser(), preConn.getPassword());
                initDB();

                while (true) {
                    System.out.println("1: add apartment info");
                    System.out.println("2: delete apartment");
                    System.out.println("3: change apartments price");
                    System.out.println("4: view apartment");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartmentInfo(sc);
                            break;
                        case "2":
                            deleteApartment(sc);
                            break;
                        case "3":
                            changeApartmentsPrice(sc);
                            break;
                        case "4":
                            viewApartment();
                            break;
                        default:
                            return;
                    }
                }
            }finally {
                sc.close();
                if (conn != null) conn.close();
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }




    }


    private static void initDB() throws SQLException {
        //Statement st = conn.createStatement();
        try(Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Apartments");
            st.execute("CREATE TABLE Apartments (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, address VARCHAR(200) " +
                    "NOT NULL,district VARCHAR(100), area INT, quantityRooms INT,price BIGINT)");
        }
    }

    private static void addApartmentInfo(Scanner sc) throws SQLException {
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter district: ");
        String district = sc.nextLine();
        System.out.print("Enter area: ");
        String sArea = sc.nextLine();
        int area = Integer.parseInt(sArea);
        System.out.print("Enter quantity rooms: ");
        String sQuantityRooms = sc.nextLine();
        int quantityRooms = Integer.parseInt(sQuantityRooms);
        System.out.print("Enter price: ");
        String sPrice = sc.nextLine();
        long price = Long.parseLong(sPrice);


        PreparedStatement ps = conn.prepareStatement("INSERT INTO Apartments (address, district, area, quantityRooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, address);
            ps.setString(2, district);
            ps.setInt(3, area);
            ps.setInt(4, quantityRooms);
            ps.setLong(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void deleteApartment(Scanner sc) throws SQLException {
        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Clients WHERE address = ?");
        try {
            ps.setString(1, address);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void changeApartmentsPrice(Scanner sc) throws SQLException {
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter new price: ");
        String sPrice = sc.nextLine();
        long price = Long.parseLong(sPrice);

        PreparedStatement ps = conn.prepareStatement("UPDATE Apartments SET price = ? WHERE address = ?");
        try {
            ps.setLong(1, price);
            ps.setString(2, address);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void viewApartment() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Apartments");
        try {

            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
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
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

}
