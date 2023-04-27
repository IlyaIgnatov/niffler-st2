package niffler.db.dao;

import niffler.db.entity.UserEntity;

import java.sql.SQLException;
import java.util.UUID;

public interface NifflerUsersDAO {

    int createUser(UserEntity user);

    int readUser(UserEntity user);

    int updateUser(UUID uuid, UserEntity user);

    int deleteUser(UserEntity user);

    UUID getUserId(String userName);

}
