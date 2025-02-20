package com.shoppingmall.user.service;

import com.shoppingmall.user.dto.PasswordGenerator;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void findPassword(String userId , String email) throws MessagingException {
        User user = userRepository.findByUserIdAndEmail(userId , email);
        if(user == null) {
            throw new UsernameNotFoundException("회원 정보를 찾을 수 없습니다.");
        }
        // 랜덤 임시비밀번호 생성기
        String newPass = passwordGenerator.generateTemporaryPassword();
        //이메일 전송
        sendEmail(email,newPass);
        // 임시 비밀번호 전송 후 유저의 비밀번호도 임시비밀번호로 변경
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
