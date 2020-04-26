package com.github.morvell.lsn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.Comment;

/**
 * @author Андрей Захаров
 * @created 26/04/2020
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
