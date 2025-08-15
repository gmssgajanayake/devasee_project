package com.devasee.users.repository;

import com.devasee.users.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<AppUser, String> {

    boolean existsByEmail(String email);

    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.name = 'ADMIN'")
    List<AppUser> findAllAdmins();
}
