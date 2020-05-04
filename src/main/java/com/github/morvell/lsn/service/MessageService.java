package com.github.morvell.lsn.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.morvell.lsn.domain.Message;
import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.UserSubscription;
import com.github.morvell.lsn.domain.Views;
import com.github.morvell.lsn.dto.EventType;
import com.github.morvell.lsn.dto.MessagePageDto;
import com.github.morvell.lsn.dto.MetaDto;
import com.github.morvell.lsn.dto.ObjectType;
import com.github.morvell.lsn.repo.MessageRepository;
import com.github.morvell.lsn.repo.UserSubscriptionRepository;
import com.github.morvell.lsn.util.WsSender;

/**
 * @author Андрей Захаров
 * @created 26/04/2020
 */
@Service
public class MessageService {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";

    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);

    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepo;

    private final UserSubscriptionRepository userSubscriptionRepo;

    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageService(MessageRepository messageRepo,
            UserSubscriptionRepository userSubscriptionRepo, WsSender wsSender) {

        this.messageRepo = messageRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
    }

    private void fillMeta(Message message) throws IOException {

        var text = message.getText();
        var matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            var url = text.substring(matcher.start(), matcher.end());

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

        var doc = Jsoup.connect(url).get();

        var title = doc.select("meta[name$=title],meta[property$=title]");
        var description = doc.select("meta[name$=description],meta[property$=description]");
        var cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(getContent(title.first()), getContent(description.first()),
                getContent(cover.first()));
    }

    private String getContent(Element element) {

        return element == null ? "" : element.attr("content");
    }

    public void delete(Message message) {

        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    public Message update(Message messageFromDb, Message message) throws IOException {

        messageFromDb.setText(message.getText());
        fillMeta(messageFromDb);
        var updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    public Message create(Message message, User user) throws IOException {

        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);
        var updatedMessage = messageRepo.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    public MessagePageDto findAll(Pageable pageable) {
        var page = messageRepo.findAll(pageable);
        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public MessagePageDto findForUser(Pageable pageable, User user) {
        List<User> channels = userSubscriptionRepo.findBySubscriber(user)
                .stream()
                .filter(UserSubscription::isActive)
                .map(UserSubscription::getChannel)
                .collect(Collectors.toList());

        channels.add(user);

        Page<Message> page = messageRepo.findByAuthorIn(channels, pageable);

        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }
}
