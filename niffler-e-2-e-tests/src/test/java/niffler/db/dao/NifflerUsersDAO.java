package niffler.db.dao;

import niffler.db.entity.UserEntity;

import java.sql.SQLException;

public interface NifflerUsersDAO {

  int createUser(UserEntity user);

  String getUserId(String userName);

}
