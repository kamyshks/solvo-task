package kamyshks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class LoadDto {
    private Integer id;
    private String name;

    public static LoadDto fromResultSet(final ResultSet resultSet) throws SQLException {
        return LoadDto.builder()
                .id(resultSet.getInt("load_id"))
                .name(resultSet.getString("load_name"))
                .build();
    }
}