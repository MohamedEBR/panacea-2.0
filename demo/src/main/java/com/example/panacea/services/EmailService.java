package com.example.panacea.services;

import com.example.panacea.models.BillingRecord;
import com.example.panacea.models.Member;
import com.example.panacea.models.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(Member member) {
        if (member == null || member.getEmail() == null) return;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("Welcome to Panacea Karate Academy");
        String students = member.getStudents() == null ? "" : member.getStudents().stream()
                .map(Student::getName)
                .collect(Collectors.joining(", "));
        message.setText("Hello " + member.getName() + ",\n\n" +
                "Your account was created successfully. You can now sign in to your dashboard.\n" +
                (students.isBlank() ? "" : ("Registered students: " + students + "\n")) +
                "\nThanks,\nPanacea Karate Academy Team");
        mailSender.send(message);
    }

    public void sendBillingReceiptEmail(Member member, BillingRecord record) {
        if (member == null || member.getEmail() == null || record == null) return;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("Panacea Karate Academy - Payment Receipt");
        String dateStr = record.getDate() != null ? record.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
        String students = record.getBilledStudents() == null ? "" : record.getBilledStudents().stream()
                .map(Student::getName)
                .collect(Collectors.joining(", "));
        message.setText(
                "Hello " + member.getName() + ",\n\n" +
                "Thank you for your payment. Here are your receipt details:\n" +
                "Amount: $" + record.getAmount() + "\n" +
                (students.isBlank() ? "" : ("Students: " + students + "\n")) +
                (record.getStripeTransactionId() != null ? ("Transaction Id: " + record.getStripeTransactionId() + "\n") : "") +
                (!dateStr.isBlank() ? ("Date: " + dateStr + "\n") : "") +
                "\nThis receipt has been added to your account.\n\n" +
                "Thanks,\nPanacea Karate Academy Team"
        );
        mailSender.send(message);
    }
}
