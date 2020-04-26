package com.github.morvell.lsn.service;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Service;

import com.github.morvell.lsn.domain.Comment;
import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.Views;
import com.github.morvell.lsn.dto.EventType;
import com.github.morvell.lsn.dto.ObjectType;
import com.github.morvell.lsn.repo.CommentRepository;
import com.github.morvell.lsn.util.WsSender;

/**
 * @author Андрей Захаров
 * @created 26/04/2020
 */
@Service
public class CommentService {

    private final CommentRepository commentRepo;

    private final BiConsumer<EventType, Comment> wsSender;

    public CommentService(CommentRepository commentRepo, WsSender wsSender) {

        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        Comment commentFromDb = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE, commentFromDb);

        return commentFromDb;
    }
}
