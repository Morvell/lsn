package com.github.morvell.lsn.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.morvell.lsn.domain.Message;
import com.github.morvell.lsn.domain.Views;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Андрей Захаров
 * @created 27/04/2020
 */
@Data
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto implements Serializable {

    private List<Message> messages;

    private int currentPage;

    private int totalPages;
}
