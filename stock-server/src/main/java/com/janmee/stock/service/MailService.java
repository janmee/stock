package com.janmee.stock.service;

import javax.mail.MessagingException;

/**
 * Created by Administrator on 2017/4/3.
 */
public interface MailService {
    void simpleSend(String to, String subject, String text) throws Exception;

    void htmlSend(String to, String subject, String text) throws MessagingException;
}
