package kamyshks.repository;

import kamyshks.dto.LoadDto;
import kamyshks.dto.LocationDto;
import kamyshks.exceptions.TransactionException;
import kamyshks.service.ConnectService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class LoadRepository {


    public void insertLoad(final int locId) throws TransactionException {
        try (Connection connection = ConnectService.getConnect();
             Statement stmt = connection.createStatement();
        ) {
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sql = "INSERT INTO loads (NAME, LOC_ID)" + " VALUES ('" + uuid + "', " + locId + ");";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new TransactionException("Error during insert loads");
        }
    }

    public Map<String, Integer> getCountLoads(final String locName) throws TransactionException {
        Map<String, Integer> countLoads = new HashMap<>();
        try (Connection connection = ConnectService.getConnect();
             Statement stmt = connection.createStatement();
        ) {
            connection.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery("SELECT loc.name, count(*) " +
                    "FROM loads JOIN location as loc " +
                    "ON loads.loc_id = loc.id " +
                    "WHERE loc.name IN (" + locName + ")" +
                    "GROUP BY loads.LOC_ID;");

            while (rs.next()) {
                countLoads.put(rs.getString("name"), rs.getInt("count(*)"));
            }
            rs.close();
        } catch (SQLException e) {
            throw new TransactionException("The location names not found");
        }
        return countLoads;
    }

    public List<LocationDto> getAllLoads() {
        try (Connection connection = ConnectService.getConnect();
             Statement stmt = connection.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT loc.id as loc_id, loc.name as loc_name, loads.id as load_id, loads.name as load_name " +
                            "FROM loads JOIN location as loc " +
                            "ON loads.loc_id = loc.id ORDER BY loc.id");
            return fromResultSetToList(rs);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    public List<LocationDto> fromResultSetToList(final ResultSet resultSet) throws SQLException {
        List<LocationDto> allLoads = new ArrayList<>();
        LocationDto actual = null;
        while (resultSet.next()) {
            LocationDto location = LocationDto.fromResultSet(resultSet);
            if (actual == null || !actual.getId().equals(location.getId())) {
                actual = location;
                allLoads.add(actual);
            }
            LoadDto loadDto = LoadDto.fromResultSet(resultSet);
            if (loadDto.getId() != null) {
                actual.getLoads().add(loadDto);
            }
        }

        return allLoads;
    }

}
