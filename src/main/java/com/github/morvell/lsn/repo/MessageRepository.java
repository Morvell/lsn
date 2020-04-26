package com.github.morvell.lsn.repo;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.Message;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = { "comments" })
    List<Message> findAll();
}
