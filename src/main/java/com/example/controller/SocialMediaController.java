package com.example.controller;

import java.io.ObjectInputFilter.Config;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@Component
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }



    //GET all messages
    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages(){
        //get all from the message service
        List<Message> listMessage = messageService.getAllMessages();
        return ResponseEntity.status(200).body(listMessage);
    }

    //GET messages from ID
    @GetMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessagesFromID(@PathVariable int message_id){
        //get message from ID from message service
        Message message = messageService.getMessageById(message_id);

        return ResponseEntity.status(200)
        .body(message);
        
    }

    //GET messages from account ID
    @GetMapping("/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessagesFromAccount(@PathVariable int account_id){
        //get message from ID from message service
        List<Message> listMessage = messageService.getMessagesByAccount(account_id);
        return ResponseEntity.status(200).body(listMessage);
    }

    //POST new account
    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<Account> registerAccount(@RequestBody Account newAccount){
        //account service will send to database and check if username already exists and password is acceptable
        //account service will also create a new Account object if successful and add to the database
        //409 if duplicate username ----DONE-----
        //400 if anything else ----DONE----
        //200 if successful ----DONE----
        if(newAccount.getUsername().length() > 0 && newAccount.getPassword().length() > 4)
        {
            if(accountService.isThereMatchingAccountUsername(newAccount.getUsername()))
            {
                return ResponseEntity.status(409).body(null);
            }
            else
            {
                return ResponseEntity.status(200).body(accountService.registerAccount(newAccount));
            }
            
        }
        else return ResponseEntity.status(400).body(null);
    }

    //POST login
    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<Account> loginAccount(@RequestBody Account newAccount){
        //message service will send to database and check if username and password matches with the database
        //message service will return the matching account
        //200 if successful
        //401 if not due to matching account
        Account loginAccount = accountService.loginAccount(newAccount);
        if(loginAccount != null)
        {
            return ResponseEntity.status(200).body(loginAccount);
        }
        else return ResponseEntity.status(401).body(null);

    }

    //POST new message
    @PostMapping(value = "/messages")
    public @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message newMessage){
        //message service will check if message 0 < length > 255
        //add to database if meets requirements
        //200 if successful
        //401 if not
        //return newly made message object
        Message newMes = messageService.postMessage(newMessage);

        if(newMes != null)
        {
            return ResponseEntity.status(200).body(newMes);
        }
        else return ResponseEntity.status(400).body(null);
    }

    //DELETE message by id
    @DeleteMapping(value = "/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable int message_id){
        //delete from database
        //200 
        //return number of rows affected (1)
        //return nothing if not deleted
        int body = messageService.deleteMessage(message_id);

        return ResponseEntity.status(200).body(body);
        
    }

    //PATCH message
    @PatchMapping(value = "/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> patchMessage(@PathVariable int message_id, @RequestBody Message updatedMessage){
        //if 0 < message length < 255 and message in database exists update the database
        //200 if successful
        //400 if not
        //return number of rows affected (1)
        //return nothing if not updated
        int body = messageService.updateMessage(message_id, updatedMessage.getMessage_text());
        if(body > 0)
        {
            return ResponseEntity.status(200).body(body);
        }
        else return ResponseEntity.status(400).body(null);
    }
}
