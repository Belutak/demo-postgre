package com.example.demopostgre;


import exception.MyResourceNotFoundException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@Transactional
public class WebController {

    @Autowired
    ContactRepository contactRepository;


    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> getContacts() {

        List<Contact> all = (List<Contact>) contactRepository.findAll();
        return ResponseEntity.ok(all);


    }


    @RequestMapping(method = RequestMethod.POST, value = "/contacts")
    public ResponseEntity<Contact> addContact(@RequestBody @Valid Contact contact) {


        Contact contact1 = contactRepository.save(contact);

        return ResponseEntity.ok(contact1);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updateContact(@RequestBody @Valid Contact contact) {

        contactRepository.deleteById(contact.getId());
        contactRepository.save(contact);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deleteContact(@RequestBody Contact contact) {

        contactRepository.deleteById(contact.getId());

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/contacts/{id}")
    public void deleteById(@PathVariable long id) {

        contactRepository.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/contacts/{id}")
    public void updateById(@PathVariable long id, @RequestBody @Valid Contact contact) throws NotFoundException {

        Optional<Contact> existing = contactRepository.findById(id);
        if (!existing.isPresent()) {
            throw new MyResourceNotFoundException("Mistake!");
        }

        existing.get().setName(contact.getName());
        existing.get().setAddress(contact.getAddress());
        existing.get().setNumber(contact.getNumber());
        contactRepository.save(existing.get());
    }

}
