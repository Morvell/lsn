package com.github.morvell.lsn.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.Message;
import com.github.morvell.lsn.domain.User;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = { "comments" })
    Page<Message> findByAuthorIn(List<User> users, Pageable pageable);
}
