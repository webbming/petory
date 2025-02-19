package com.shoppingmall.user.service;

import com.shoppingmall.user.dto.PasswordGenerator;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    private UserRepository userRepository;
    private PasswordGenerator passwordGenerator;
    private BCryptPasswordEncoder passwordEncoder;

    public EmailService(JavaMailSender mailSender , UserRepository userRepository , PasswordGenerator passwordGenerator, BCryptPasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.passwordGenerator = passwordGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    public void findPassword(String userId , String email) throws MessagingException {
        User user = userRepository.findByUserIdAndEmail(userId , email);
        if(user == null) {
            throw new UsernameNotFoundException("회원 정보를 찾을 수 없습니다.");
        }
        String newPass = passwordGenerator.generateTemporaryPassword();
        sendEmail(email,newPass);
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        System.out.println("임시비밀번호가 전송 되었습니다.");
    }

    public void sendEmail(String email , String pass) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("(반려동물앱) 임시 비밀번호 발급");
        String content ="<html><head></head><body>" +
                        "<h2>반려동물 용품 주문형 웹서비스</h2>" +
                        "<p>임시 비밀번호로 로그인해주세요</p><br>" +
                        "<p><strong>임시 비밀번호 : </strong>"+ pass + "</p>" +
                        "</body></html>";

        helper.setText(content, true);
        mailSender.send(message);
    }
}
