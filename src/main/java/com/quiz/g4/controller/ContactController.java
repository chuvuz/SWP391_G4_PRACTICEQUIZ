package com.quiz.g4.controller;

import com.quiz.g4.entity.Contact;
import com.quiz.g4.enums.CommentStatus;
import com.quiz.g4.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/form")
    public String showContactForm(Contact contact) {
        return "contact-form";
    }

    @PostMapping("/submit")
    public String submitContactForm(@Valid @ModelAttribute("contact") Contact contact,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "contact-form"; // Return to the form page with validation errors
        }

        contact.setStatus(CommentStatus.PENDING);
        contact.setCreatedAt(LocalDateTime.now());
        contactService.saveContact(contact);

        redirectAttributes.addFlashAttribute("message", "Your message has been received.");
        return "redirect:/contact/form"; // Redirect back to the form with a success message
    }

    // Method to show the contact list
    @GetMapping("/list")
    public String showContactList(Model model) {
        model.addAttribute("contacts", contactService.findAllContacts());
        return "contact-list";
    }

    // Method to update contact status to 'SEEN'
    @PostMapping("/markSeen/{id}")
    public String markContactAsSeen(@PathVariable("id") Integer contactId, RedirectAttributes redirectAttributes) {
        Contact contact = contactService.findById(contactId);
        if (contact != null) {
            contact.setStatus(CommentStatus.ACCEPTED);  // Update status to ACCEPTED
            contactService.saveContact(contact);   // Save changes
            redirectAttributes.addFlashAttribute("message", "Contact marked as seen.");
        }
        return "redirect:/contact/list";
    }

}
