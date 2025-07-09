package com.maybank.liangquan.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maybank.liangquan.demo.object.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByUuid(String uuid);

}
