package com.github.morvell.lsn.controller;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.morvell.lsn.domain.Message;
import com.github.morvell.lsn.domain.Views;
import com.github.morvell.lsn.dto.EventType;
import com.github.morvell.lsn.dto.MetaDto;
import com.github.morvell.lsn.dto.ObjectType;
import com.github.morvell.lsn.repo.MessageRepository;
import com.github.morvell.lsn.util.WsSender;

@RestController
@RequestMapping("message")
public class MessageController {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";

    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);

    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepository;

    private final BiConsumer<EventType, Message> wsSender;

    public MessageController(MessageRepository messageRepository, WsSender sender) {

        this.messageRepository = messageRepository;
        this.wsSender = sender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @JsonView(Views.IdName.class)
    @GetMapping
    public List<Message> list() {

        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    public Message getOne(@PathVariable("id") Message message) {

        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) throws IOException {

        fillMeta(message);
        var save = messageRepository.save(message);
        wsSender.accept(EventType.CREATE, save);
        return save;
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb,
            @RequestBody Message message) throws IOException {

        BeanUtils.copyProperties(message, messageFromDb, "id");

        fillMeta(messageFromDb);
        var save = messageRepository.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, save);
        return save;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {

        messageRepository.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    private void fillMeta(Message message) throws IOException {

        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());

            matcher = IMG_REGEX.matcher(url);

            message.setLink(url);

            if (matcher.find()) {
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                MetaDto meta = getMeta(url);

                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(getContent(title.first()), getContent(description.first()),
                getContent(cover.first()));
    }

    private String getContent(Element element) {

        return element == null ? "" : element.attr("content");
    }
}
