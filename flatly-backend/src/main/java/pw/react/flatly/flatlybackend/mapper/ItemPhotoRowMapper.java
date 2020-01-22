package pw.react.flatly.flatlybackend.mapper;

import org.springframework.jdbc.core.RowMapper;
import pw.react.flatly.flatlybackend.model.ItemPhoto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemPhotoRowMapper implements RowMapper<ItemPhoto> {
    @Override
    public ItemPhoto mapRow(ResultSet rs, int arg1) throws SQLException {

        ItemPhoto photo = new ItemPhoto();
        photo.setId(Long.parseLong(rs.getString("id")));
        photo.setPhoto(rs.getString("photo").getBytes());
        //TODO: setItem
        return photo;
    }
}