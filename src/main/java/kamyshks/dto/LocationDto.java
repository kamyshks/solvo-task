package kamyshks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LocationDto {
    private Integer id;
    private String name;
    private List<LoadDto> loads;

    public static LocationDto fromResultSet(final ResultSet resultSet) throws SQLException {
        return LocationDto.builder()
                .id(resultSet.getInt("loc_id"))
                .name(resultSet.getString("loc_name"))
                .loads(new ArrayList<>())
                .build();
    }

}
