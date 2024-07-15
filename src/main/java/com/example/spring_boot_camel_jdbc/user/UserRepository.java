package com.example.spring_boot_camel_jdbc.user;

import java.util.List;


public interface UserRepository {
   List<User> findUserAll();
   User findUserById(Long id);
   void saveUser(User user);
   void updateUser(User user, Long id);
   void deleteUser(Long id);
}
