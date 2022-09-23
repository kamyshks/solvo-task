package kamyshks.repository;

import kamyshks.exceptions.TransactionException;
import kamyshks.service.ConnectService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LocationRepository {

    public int getIdLocationByName(final String locationName) throws TransactionException {
        try (
                Connection connection = ConnectService.getConnect();
                Statement stmt = connection.createStatement();
        ) {
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery("SELECT id FROM LOCATION where name = " + locationName);

            while (rs.next()) {
                return rs.getInt("id");
            }
            rs.close();
        } catch (Exception e) {
            throw new TransactionException("Error during transaction to data base");
        }
        return -1;
    }
}
