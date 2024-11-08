package com.quiz.g4.service;

import com.quiz.g4.entity.Contact;
import org.springframework.stereotype.Service;

@Service
public interface ContactService {

    void saveContact(Contact contact);
}
