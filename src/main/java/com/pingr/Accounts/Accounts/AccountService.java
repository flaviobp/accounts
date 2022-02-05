package com.pingr.Accounts.Accounts;

import com.pingr.Accounts.Accounts.exceptions.EntityNotFoundException;
import com.pingr.Accounts.Accounts.exceptions.EntityValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository repo;
    private final ProducerService producer;

    @Autowired
    public AccountService(AccountRepository repo, ProducerService producer) {
        this.repo = repo;
        this.producer = producer;
    }

    //   tem ID, está salva no banco
    //     |
    //     |         não tem ID, não está no banco ainda
    //     |                                  |
    //     v                                  v
    public Account createAccount(Account account) throws IllegalArgumentException {
        Optional<Account> oneByName = this.repo.findOneByUsername(account.getUsername());
        if (oneByName.isPresent()) {
            throw new EntityValidationException("name");
        }

        try {
            Account acc = this.repo.save(account);
            this.producer.emitAccountCreatedEvent(acc);
            return acc;
        } catch(Exception e) {
            throw new IllegalArgumentException("Account creation violates restrictions" + "[account: " + account + "]");
        }
    }

    public Account updateAccount(Long id, Account account) throws IllegalArgumentException {
        try {
            Optional<Account> oneById = this.repo.findById(id);
            if (oneById.isEmpty()) {
                throw new EntityNotFoundException("account", id);
            }

            account.setId(id);
            Account acc = this.repo.save(account);
            this.producer.emitAccountUpdatedEvent(acc);
            return acc;
        } catch(EntityNotFoundException enf) {
                throw enf;
        } catch(Exception e) {
            throw new IllegalArgumentException("Account creation violates restrictions" + "[account: " + account + "]");
        }
    }

    public void deleteAccount(Long id) throws IllegalArgumentException {
        Optional<Account> oneById = this.repo.findById(id);
        if (oneById.isEmpty()) {
            throw new EntityNotFoundException("account", id);
        }

        try {
            this.repo.deleteById(id);
            this.producer.emitAccountDeletedEvent(id);
        } catch(Exception e) {
            throw new IllegalArgumentException("Account creation violates restrictions" + "[account: " + id + "]");
        }
    }

}
