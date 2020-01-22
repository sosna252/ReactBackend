package pw.react.flatly.flatlybackend.mapper;

import org.springframework.jdbc.core.RowMapper;
import pw.react.flatly.flatlybackend.model.Item;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ItemRowMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int arg1) throws SQLException {

        Item it = new Item();
        it.setId(Long.parseLong(rs.getString("id")));
        //TODO: setUser
        it.setStart_date_time(LocalDate.parse(rs.getString("start_date_time")));
        it.setEnd_date_time(LocalDate.parse(rs.getString("end_date_time")));
        it.setTitle(rs.getString("title"));
        it.setDescription(rs.getString("description"));
        //TODO: setItem_photo
        it.setRoom_number(Integer.parseInt(rs.getString("room_number")));
        it.setBeds(Integer.parseInt(rs.getString("beds")));
        it.setPrice(new BigDecimal(rs.getString("price")));
        it.setRating( new BigDecimal(rs.getString("rating")));
        it.setCity(rs.getString("city"));
        it.setAddress(rs.getString("address"));
        it.setCountry(rs.getString("country"));
        return it;
    }
}