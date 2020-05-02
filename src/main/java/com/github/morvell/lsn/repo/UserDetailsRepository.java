package com.github.morvell.lsn.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.User;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
public interface UserDetailsRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = { "subscriptions", "subscribers" })
    Optional<User> findById(String s);
}
