package com.janmee.stock.service.impl;

import com.janmee.stock.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public class MailServiceImpl implements MailService {
    @Value("${mail.from}")
    private String from = "56508820@qq.com";

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 方法名: simpleSend
     * 方法作用: TODO 简单邮件发送
     * 创建时间：2015-2-7 下午06:47:30
     *
     * @param @throws Exception
     *                返回值类型： void
     * @throws
     */
    public void simpleSend(String to, String subject, String text) throws Exception {
        // 构建简单邮件对象
        //SimpleMailMessages实现了MimeMessageHelper，为普通邮件模板，支持文本。
        SimpleMailMessage smm = new SimpleMailMessage();
        // 设定邮件参数
        smm.setFrom(from);
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(text);
        // 发送邮件
        javaMailSender.send(smm);
    }


    /**
     *
     * 方法名: richContentSend
     * 方法作用:
     * 创建时间：2015-2-7 下午06:47:14
     * @param @throws MessagingException
     * 返回值类型： void
     * @throws
     */
    @Override
    public void htmlSend(String to, String subject, String text) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        //第二个参数true，表示text的内容为html，然后注意<img/>标签，src='cid:file'，'cid'是contentId的缩写，'file'是一个标记，
        //需要在后面的代码中调用MimeMessageHelper的addInline方法替代成文件
        helper.setText(text);
        javaMailSender.send(msg);
    }
}
