package com.team10.ojbattle.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * @author: 陈健航
 * @description: 因为要读取邮箱，不能做成static，所以要注入
 * @since: 2020/5/17 16:01
 * @version: 1.0
 */
@Component
public class EmailUtils {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String to, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(sender);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
    }

    public String readContent(String file) throws IOException {
        InputStream is = new ClassPathResource("/" + file).getInputStream();
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (; ; ) {
            int rsz = 0;
            try {
                rsz = in.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取错误");
            }
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        is.close();
        return out.toString();
    }
}
