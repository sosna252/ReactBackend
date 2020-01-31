package pw.react.flatly.flatlybackend.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.mapper.UserRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    public UserDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    private NamedParameterJdbcTemplate template;

    @Override
    public List<User> findAll() {
        return template.query("select * from public.users", new UserRowMapper());
    }

    @Override
    public void insertUser(User usr) {
        final String sql = "insert into public.users(id, login, password, first_name, last_name, security_token) values(:id, :login, :password, :first_name, :last_name, :security_token);";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", usr.getId())
                .addValue("login", usr.getLogin())
                .addValue("password", usr.getPassword())
                .addValue("first_name", usr.getFirst_name())
                .addValue("last_name", usr.getLast_name())
                .addValue("security_token", usr.getSecurityToken());
        template.update(sql,param, holder);
    }

    @Override
    public void updateUser(User usr) {
        final String sql = "update public.users set login=:login, password=:password, first_name=:first_name, lastName=:last_name, security_token=:security_token where id=:id";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", usr.getId())
                .addValue("login", usr.getLogin())
                .addValue("password", usr.getPassword())
                .addValue("first_name", usr.getFirst_name())
                .addValue("last_name", usr.getLast_name())
                .addValue("security_token", usr.getSecurityToken());
        template.update(sql,param, holder);
    }

    @Override
    public void executeUpdateUser(User usr) {
        final String sql = "update public.users set login=:login, password=:password, first_name=:first_name, lastName=:last_name, security_token=:security_token where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", usr.getId());
        map.put("login", usr.getLogin());
        map.put("password", usr.getPassword());
        map.put("first_name", usr.getFirst_name());
        map.put("last_name", usr.getLast_name());
        map.put("security_token", usr.getSecurityToken());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    @Override
    public void deleteUser(User usr) {
        final String sql = "delete from public.users where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", usr.getId());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }
}
