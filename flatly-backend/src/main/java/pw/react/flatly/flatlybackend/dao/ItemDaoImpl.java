package pw.react.flatly.flatlybackend.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pw.react.flatly.flatlybackend.mapper.ItemRowMapper;
import pw.react.flatly.flatlybackend.model.Item;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDaoImpl implements ItemDao {

    public ItemDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    private NamedParameterJdbcTemplate template;

    @Override
    public List<Item> findAll() {
        return template.query("select * from public.items", new ItemRowMapper());
    }

    @Override
    public void insertItem(Item it) {
        final String sql = "insert into public.items(id, author_id, start_date_time, end_date_time, title, description, photo_id, room_number, beds, price, rating, city, address, country) values(:id, :author_id, :start_date_time, :end_date_time, :title, :description, :photo_id, :room_number, :beds, :price, :rating, :city, :address, :country);";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", it.getId())
                .addValue("author_id", it.getUser().getId())
                .addValue("start_date_time", it.getStart_date_time())
                .addValue("end_date_time", it.getEnd_date_time())
                .addValue("title", it.getTitle())
                .addValue("description", it.getDescription())
                .addValue("room_number", it.getRoom_number())
                .addValue("beds", it.getBeds())
                .addValue("price", it.getPrice())
                .addValue("rating", it.getRating())
                .addValue("city", it.getCity())
                .addValue("address", it.getAddress())
                .addValue("country", it.getCountry());
        template.update(sql,param, holder);
    }

    @Override
    public void updateItem(Item it) {
        final String sql = "update public.items set author_id=:author_id, start_date_time=:start_date_time, end_date_time=:end_date_time, title=:title, description=:description, photo_id=:photo_id, room_number=:room_number, beds=:beds, price=:price, rating=:rating, city=:city, address=:address, country=:country where id=:id";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", it.getId())
                .addValue("author_id", it.getUser().getId())
                .addValue("start_date_time", it.getStart_date_time())
                .addValue("end_date_time", it.getEnd_date_time())
                .addValue("title", it.getTitle())
                .addValue("description", it.getDescription())
                .addValue("room_number", it.getRoom_number())
                .addValue("beds", it.getBeds())
                .addValue("price", it.getPrice())
                .addValue("rating", it.getRating())
                .addValue("city", it.getCity())
                .addValue("address", it.getAddress())
                .addValue("country", it.getCountry());
        template.update(sql,param, holder);
    }

    @Override
    public void executeUpdateItem(Item it) {
        final String sql = "update public.items set author_id=:author_id, start_date_time=:start_date_time, end_date_time=:end_date_time, title=:title, description=:description, photo_id=:photo_id, room_number=:room_number, beds=:beds, price=:price, rating=:rating, city=:city, address=:address, country=:country where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", it.getId());
        map.put("author_id", it.getUser().getId());
        map.put("start_date_time", it.getStart_date_time());
        map.put("end_date_time", it.getEnd_date_time());
        map.put("title", it.getTitle());
        map.put("description", it.getDescription());
        map.put("room_number", it.getRoom_number());
        map.put("beds", it.getBeds());
        map.put("price", it.getPrice());
        map.put("rating", it.getRating());
        map.put("city", it.getCity());
        map.put("address", it.getAddress());
        map.put("country", it.getCountry());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    @Override
    public void deleteItem(Item it) {
        final String sql = "delete from public.items where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", it.getId());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }
}
