// package com.coldchain.coldchain.Service.ServiceImpl;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     public void sendVerificationEmail(String email, String verificationUrl) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(email);
//         message.setSubject("Account Verification");
//         message.setText("Click the link to verify your account: " + verificationUrl);
//         mailSender.send(message);
//     }
// }
