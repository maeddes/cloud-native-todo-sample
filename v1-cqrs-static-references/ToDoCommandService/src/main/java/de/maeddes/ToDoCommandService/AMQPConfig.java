package de.maeddes.ToDoCommandService;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mhs on 24.05.17.
 */
@Configuration
public class AMQPConfig {

    @Bean
    public Queue hello() {
        return new Queue("my-queue");
    }

    // @Bean
    // public ToDoListCommandReceiver receiver() { return new ToDoListCommandReceiver(); }

}
