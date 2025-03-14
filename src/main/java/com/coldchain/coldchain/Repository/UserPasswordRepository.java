package com.coldchain.coldchain.Repository;

import com.coldchain.coldchain.entity.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPasswordRepository extends JpaRepository<UserPassword, String> {
}
