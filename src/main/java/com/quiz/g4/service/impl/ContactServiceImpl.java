package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Contact;
import com.quiz.g4.repository.ContactRepository;
import com.quiz.g4.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void saveContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public List<Contact> findAllContacts(){
        return contactRepository.findAll();
    }

    @Override
    public Contact findById(Integer id){
        return contactRepository.findByContactId(id);
    }
}
