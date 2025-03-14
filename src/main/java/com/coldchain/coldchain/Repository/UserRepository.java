package com.coldchain.coldchain.Repository;

import com.coldchain.coldchain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

//    Optional<User> getUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
