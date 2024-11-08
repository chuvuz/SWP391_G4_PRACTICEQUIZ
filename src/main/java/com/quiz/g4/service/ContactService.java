package com.quiz.g4.service;

import com.quiz.g4.entity.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {

    void saveContact(Contact contact);
    List<Contact> findAllContacts();
    Contact findById(Integer id);
}
