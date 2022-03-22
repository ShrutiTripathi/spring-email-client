package com.shruti.springemailclient.service;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.shruti.springemailclient.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${emailSendTo}")
    private String emailSendTo;

    @Value("${emailSendFrom}")
    private String emailSendFrom;

    @Value("${attachmentLocation}")
    private String attachmentLocation;

    public String sendWithoutAttachment(){
        sendSimpleEmail(emailSendTo,
                "This is the email body", "this is the email subject");
        return "Mail sent";

    }
    public String sendWithAttachment() throws MessagingException {
        sendEmailWithAttachment(emailSendTo,
                "This is Email Body with Attachment...",
                "This email has attachment",
                attachmentLocation+"/download.jpeg");
    return "Mail sent";
    }

    public void sendSimpleEmail(String toEmail,
                                String body,
                                String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailSendFrom);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String toEmail,
                                        String body,
                                        String subject,
                                        String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(emailSendFrom);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem
                = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                fileSystem);

        mailSender.send(mimeMessage);

    }

    public String sendEmailWithCustomAttachment() throws MessagingException {
        ArrayList<Employee> studentList = new ArrayList();
        Employee s1 = new Employee("Sachin","Sachine@gmail.com");
        Employee s2 = new Employee("Rahul","Rahul@gmail.com");
        studentList.add(s1);
        studentList.add(s2);

        try {
            File file = new File(getClass().getClassLoader().getResource("cert.pdf").getFile());
            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            Paragraph p1 = new Paragraph("This is Custom PDF generation");
            float[] columnWidth = {200f, 200f};
            Table  table = new Table(columnWidth);
            table.addCell(new Cell().add("Name"));
            table.addCell(new Cell().add("Email"));
            for(Employee stduentData: studentList){
                table.addCell(new Cell().add(String.valueOf(stduentData.getName())));
                table.addCell(new Cell().add(stduentData.getEmailId()));
            }
            document.add(p1);
            document.add(table);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("shrutimailing1989@gmail.com");
        mimeMessageHelper.setTo("shrutitripathi1019@gmail.com");
        mimeMessageHelper.setText("This is the body");
        mimeMessageHelper.setSubject("This is the Subject");
        File file = new File(getClass().getClassLoader().getResource("cert.pdf").getFile());
        FileSystemResource fileSystem = new FileSystemResource(file);
        mimeMessageHelper.addAttachment(fileSystem.getFilename(),fileSystem);
        mailSender.send(mimeMessage);
        return "Mail Sent...";

    }
}