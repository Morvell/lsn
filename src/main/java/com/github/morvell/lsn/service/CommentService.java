package com.github.morvell.lsn.service;

import org.springframework.stereotype.Service;

import com.github.morvell.lsn.domain.Comment;
import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.repo.CommentRepository;

/**
 * @author Андрей Захаров
 * @created 26/04/2020
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {

        this.commentRepository = commentRepository;
    }

    public Comment create(Comment comment, User user) {

        comment.setAuthor(user);
        return commentRepository.save(comment);
    }
}
