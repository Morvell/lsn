package com.github.morvell.lsn.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.Message;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = { "comments" })
    Page<Message> findAll(Pageable pageable);
}
