package pw.react.flatly.flatlybackend.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pw.react.flatly.flatlybackend.mapper.ItemPhotoRowMapper;
import pw.react.flatly.flatlybackend.model.ItemPhoto;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemPhotoDaoImpl implements ItemPhotoDao {

    public ItemPhotoDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    private NamedParameterJdbcTemplate template;

    @Override
    public List<ItemPhoto> findAll() {
        return template.query("select * from public.item_photos", new ItemPhotoRowMapper());
    }

    @Override
    public void insertItemPhoto(ItemPhoto photo) {
        final String sql = "insert into public.item_photos(id, photo, item_id) values(:id, photo, item_id);";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", photo.getId())
                .addValue("photo", photo.getPhoto())
                .addValue("item_id", photo.getItem().getId());
        template.update(sql,param, holder);
    }

    @Override
    public void updateItemPhoto(ItemPhoto photo) {
        final String sql = "update public.item_photos set photo=:photo, item_id=:item_id where id=:id";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", photo.getId())
                .addValue("photo", photo.getPhoto())
                .addValue("item_id", photo.getItem().getId());
        template.update(sql,param, holder);
    }

    @Override
    public void executeUpdateItemPhoto(ItemPhoto photo) {
        final String sql = "update public.item_photos set photo=:photo, item_id=:item_id where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", photo.getId());
        map.put("photo", photo.getPhoto());
        map.put("item_id", photo.getItem().getId());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    @Override
    public void deleteItemPhoto(ItemPhoto photo) {
        final String sql = "delete from public.item_photos where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", photo.getId());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }
}
