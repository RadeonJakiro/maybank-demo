package com.maybank.liangquan.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maybank.liangquan.demo.object.User;
import com.maybank.liangquan.demo.object.UserCredential;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {

	Optional<UserCredential> findByUserUuid(String user_uuid);

	Optional<UserCredential> findByUser(User user);

	Optional<UserCredential> findByUsername(String userId);

}
