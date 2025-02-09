package com.pingr.Accounts.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

// forma de se comunicar com o Kakfa
// eventos que vamos publicar
// - accountCreated
// - accountUpdated
// - accountDeleted

@Service
public class ProducerService {

    @Value(value = "${topics.acc_created}")
    private String accountCreatedTopic;

    @Value(value = "${topics.acc_updated}")
    private String accountUpdatedTopic;

    @Value(value = "${topics.acc_deleted}")
    private String accountDeletedTopic;

    @Autowired // injeção de dependências
    private KafkaTemplate<String, Object> template;

    public void emitAccountCreatedEvent(Account account) {
        this.template.send(
                this.accountCreatedTopic,
                AccountCreatedEvent.of(account));
    }

    public void emitAccountUpdatedEvent(Account account) {
        this.template.send(
                this.accountUpdatedTopic,
                AccountCreatedEvent.of(this.accountUpdatedTopic, account));
    }

    public void emitAccountDeletedEvent(Long id) {
        this.template.send(
                this.accountDeletedTopic,
                AccountCreatedEvent.of(this.accountDeletedTopic, id));
    }
}
