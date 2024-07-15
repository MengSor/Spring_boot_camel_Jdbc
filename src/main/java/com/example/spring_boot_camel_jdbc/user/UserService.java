package com.example.spring_boot_camel_jdbc.user;

import com.example.spring_boot_camel_jdbc.user.statement.StatementSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements UserRepository{
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    RowMapper<User> rowMapper = ((rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email")
    ));

    @Override
    public List<User> findUserAll() {
        return jdbcTemplate.query(StatementSql.FIND_USER_ALL.getStatement(),rowMapper);
    }

    @Override
    public User findUserById(Long id) {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(StatementSql.FIND_USER_BY_ID.getStatement(), rowMapper,id);
        }catch (DataAccessException e){
            log.info("Find By Id not found : " + id,e);
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        int insert = jdbcTemplate.update(StatementSql.CREATE_USER.getStatement(),user.getId(),user.getName(),user.getEmail());
        if (insert == 1){
            log.info("new create " + user.getName());
        }
    }

    @Override
    public void updateUser(User user,Long id) {
       int update = jdbcTemplate.update(StatementSql.UPDATE_USER.getStatement(),user.getName(),user.getEmail(),id);
       if (update == 1){
           log.info("update" + user.getName());
       }
    }

    @Override
    public void deleteUser(Long id) {
      int delete = jdbcTemplate.update(StatementSql.DELETE_USER.getStatement(),id);
      if (delete == 1){
          log.info("delete" + id);
      }
    }
}
