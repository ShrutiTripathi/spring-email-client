package com.shruti.springemailclient.controller;

import com.shruti.springemailclient.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/email")
public class EmailSenderController {

    @Autowired
    private EmailSenderService service;


    @GetMapping("/sendWithoutAttachment")
    public String sendWithoutAttachment() {
        return service.sendWithoutAttachment();
    }

    @GetMapping("/sendWithAttachment")
    public String sendwithAttachment() throws MessagingException {
        return service.sendWithAttachment();
    }

    @GetMapping("/sendWithCustomData")
    public String sendEmailWithCustomAttachment() throws MessagingException {
        return service.sendEmailWithCustomAttachment();
    }

}
