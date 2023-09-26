package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account){
        return accountRepository.save(account);
    }

    public Account registerAccount(Account account)
    {
        //if account username doesnt exist and account username is not null and account password length > 4
        Account regAccount = accountRepository.save(account);
        //add account to the database
        return regAccount;
        //else throw exception
    }

    public Account loginAccount(Account account)
    {
        Optional<Account> loginAccount = accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(loginAccount.isPresent())
        {
            return loginAccount.get();
        }
        else
        {
            return null;
        }
        //if account password and username matches with account in database
        //return the account
    }

    public boolean isThereMatchingAccountUsername(String username)
    {
        Optional<Account> optAcc = accountRepository.findAccountByUsername(username);
        if(optAcc.isPresent())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
