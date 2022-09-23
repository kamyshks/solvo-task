package kamyshks.service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectService {

    private static final String FILE_NAME = "/port.db";

    /**
     * Connect to a SQLite
     */
    public static Connection getConnect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + FILE_NAME);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return connection;
    }

    /**
     * Initial a SQLite
     */
    public static void initialDB() {
        if(!new File(System.getProperty("user.dir") + FILE_NAME).exists()){
        try (
                Connection connection =  ConnectService.getConnect();
                Statement stmt = connection.createStatement();
        ) {
                String sql = "CREATE TABLE IF NOT EXISTS location " +
                        "(ID       INTEGER         PRIMARY KEY AUTOINCREMENT," +
                        "NAME      VARCHAR(32)     NOT NULL CHECK (LENGTH(NAME) > 0));";
                stmt.executeUpdate(sql);

                sql = "CREATE TABLE IF NOT EXISTS loads " +
                        "(ID       INTEGER         PRIMARY KEY AUTOINCREMENT," +
                        "NAME      VARCHAR(32)     NOT NULL CHECK (LENGTH(NAME) > 0), " +
                        "LOC_ID    INTEGER         REFERENCES location (ID));";
                stmt.executeUpdate(sql);

            sql = "INSERT INTO location(NAME) VALUES (100);" +
                    "INSERT INTO location(NAME) VALUES (101);" +
                    "INSERT INTO location(NAME) VALUES (102)";
                stmt.executeUpdate(sql);

            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
        }
    }
}