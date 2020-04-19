package com.github.morvell.lsn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.User;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
public interface UserDetailsRepository extends JpaRepository<User, String> {

}
