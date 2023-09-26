package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Component
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }else{
            return null;
        }
    }

    public List<Message> getMessagesByAccount(int id){
        return messageRepository.findMessageByPostedBy(id);
    }

    public Message postMessage(Message newMessage)
    {
        if(newMessage.getMessage_text().length() > 0 && newMessage.getMessage_text().length() < 255)
        {
            Optional<Account> optAcc = accountRepository.findById(newMessage.getPosted_by());
            if(optAcc.isPresent())
            {
                return messageRepository.save(newMessage);
            }
            
        }
        return null;
        
    }

    public int deleteMessage(int message_id)
    {
        Optional<Message> optMess = messageRepository.findById(message_id);
        if(optMess.isPresent())
        {
            messageRepository.deleteById(message_id);
            return 1;
        }
        else return 0;
    }

    public int updateMessage(int message_id, String newMessage)
    {
        if(newMessage.length() > 0 && newMessage.length() < 255)
        {
            Optional<Message> optMess = messageRepository.findById(message_id);
            if(optMess.isPresent())
            {
                Message message = optMess.get();
                message.setMessage_text(newMessage);
                messageRepository.save(message);
                return 1;
            }
        }
        return 0;
        
    }
}
