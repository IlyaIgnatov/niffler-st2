package niffler.db.dao;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

  private final TransactionTemplate transactionTemplate;
  private final JdbcTemplate jdbcTemplate;
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


  public NifflerUsersDAOSpringJdbc() {
    DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
    this.transactionTemplate = new TransactionTemplate(transactionManager);
    this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
  }

  @Override
  public int createUser(UserEntity user) {


    return transactionTemplate.execute(st -> {
      KeyHolder keyHolder = new GeneratedKeyHolder();

      int st1 = jdbcTemplate.update("INSERT INTO users "
                      + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                      + " VALUES (?, ?, ?, ?, ?, ?)",
              user.getUsername(),
              pe.encode(user.getPassword()),
              user.getEnabled(),
              user.getAccountNonExpired(),
              user.getAccountNonLocked(),
              user.getCredentialsNonExpired(),
              keyHolder
      );

      final UUID createdUserId;

      if (keyHolder.getKeys().size() > 1) {
        createdUserId = UUID.fromString((String)keyHolder.getKeys().get("id"));
      } else {
        createdUserId= UUID.fromString(keyHolder.getKey().toString());
      }


      String insertAuthoritiesSql = "INSERT INTO authorities (user_id, authority) VALUES ('%s', '%s')";

      List<String> sqls = user.getAuthorities()
              .stream()
              .map(ae -> ae.getAuthority().name())
              .map(a -> String.format(insertAuthoritiesSql, createdUserId, a))
              .toList();

      int updateRows=0;
      for (String sql_st : sqls) {
        updateRows += jdbcTemplate.update(sql_st);
      }

      return updateRows;
    });
  }

  @Override
  public UserEntity readUser(UUID uuid) {
    return null;
  }

  @Override
  public int updateUser(UserEntity user) {
    return 0;
  }

  @Override
  public int deleteUser(UUID uuid) {
    return 0;
  }

  @Override
  public UUID getUserId(String userName) {
    return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
        rs -> {return UUID.fromString(rs.getString(1));},
        userName
    );
  }

  /*@Override
  public int removeUser(UserEntity user) {
    return transactionTemplate.execute(st -> {
      jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
      return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
    });
  }*/
}
