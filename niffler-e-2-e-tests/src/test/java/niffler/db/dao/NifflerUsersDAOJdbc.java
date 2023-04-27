package niffler.db.dao;

import java.sql.*;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public int createUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement st1 = conn.prepareStatement("INSERT INTO users "
                    + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                    + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                st1.setString(1, user.getUsername());
                st1.setString(2, pe.encode(user.getPassword()));
                st1.setBoolean(3, user.getEnabled());
                st1.setBoolean(4, user.getAccountNonExpired());
                st1.setBoolean(5, user.getAccountNonLocked());
                st1.setBoolean(6, user.getCredentialsNonExpired());

                executeUpdate = st1.executeUpdate();

                final UUID createdUserId;

                try (ResultSet keys = st1.getGeneratedKeys()) {
                    if (keys.next()) {
                    createdUserId = UUID.fromString(keys.getString(1));
                    user.setId(createdUserId);
                } else {
                        throw new IllegalArgumentException("Unable to create user, no uuid");
                    }
                }

                String insertAuthoritiesSql = "INSERT INTO authorities (user_id, authority) VALUES ('%s', '%s')";

                List<String> sqls = user.getAuthorities()
                        .stream()
                        .map(ae -> ae.getAuthority().name())
                        .map(a -> String.format(insertAuthoritiesSql, createdUserId, a))
                        .toList();

                for (String sql : sqls) {
                    try (Statement st2 = conn.createStatement()) {
                        st2.executeUpdate(sql);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeUpdate;
    }

    @Override
    public UserEntity readUser(UUID uuid) {

        UserEntity userEntity = new UserEntity();

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE id=(?)")) {
            st.setObject(1, uuid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userEntity.setId(UUID.fromString(rs.getString(1)));
                userEntity.setUsername(rs.getString(2));
                userEntity.setPassword(rs.getString(3));
                userEntity.setEnabled(rs.getBoolean(4));
                userEntity.setAccountNonExpired(rs.getBoolean(5));
                userEntity.setAccountNonLocked(rs.getBoolean(6));
                userEntity.setCredentialsNonExpired(rs.getBoolean(7));
            } else {
                throw new IllegalArgumentException("Can`t find user by given uuid: " + uuid);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userEntity;
    }

    @Override
    public int updateUser(UUID uuid, UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection();
             PreparedStatement st1 = conn.prepareStatement("UPDATE users SET "
                     + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)="
                     + "(?, ?, ?, ?, ?, ?) WHERE id=(?)")) {
            st1.setString(1, user.getUsername());
            st1.setString(2, pe.encode(user.getPassword()));
            st1.setBoolean(3, user.getEnabled());
            st1.setBoolean(4, user.getAccountNonExpired());
            st1.setBoolean(5, user.getAccountNonLocked());
            st1.setBoolean(6, user.getCredentialsNonExpired());
            st1.setObject(7, uuid);

            executeUpdate = st1.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeUpdate;
    }

    @Override
    public int deleteUser(UUID uuid) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement st1 = conn.prepareStatement("DELETE FROM authorities WHERE user_id=(?)")) {
                st1.setObject(1, uuid);

                try (PreparedStatement st2 = conn.prepareStatement("DELETE FROM users WHERE id=(?)")) {
                    st2.setObject(1, uuid);

                    executeUpdate = st2.executeUpdate();
                }

            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeUpdate;
    }

    @Override
    public UUID getUserId(String userName) {
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            st.setString(1, userName);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString(1));
            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
