package de.maeddes.ToDoCommandService;

import java.net.URI;
import java.util.List;
import java.util.Set;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ToDoCommandServiceApplication {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Queue queue;

    public void send(String message) {
        this.rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }

    @GetMapping("/test")
    public String test() {
        return "Ok";
    }

    @RequestMapping(value = "/todo/{toDo}", method = RequestMethod.POST)
    public String addItem(@PathVariable String toDo) {

        ToDoItem toDoItem = new ToDoItem(toDo, false);
        this.handleRequest(toDoItem);
        return toDoItem.toString();

    }

    @RequestMapping(value = "/done/{toDo}", method = RequestMethod.POST)
    public String setItemDone(@PathVariable String toDo) {

        ToDoItem toDoItem = new ToDoItem(toDo, true);
        this.handleRequest(toDoItem);
        return toDoItem.toString();

    }

    private void handleRequest(ToDoItem toDoItem) {

        System.out.println("In handleRequest: " + toDoItem);
        this.send(toDoItem.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("ToDoQueryService");

        URI uri = null;
        if (instances != null && instances.size() > 0) {
            uri = instances.get(0).getUri();
        }
        System.out.println("In handleRequest: URI from Eureka: " + uri.toString());

        if (uri != null) {
            if (!toDoItem.done) {
                String url = uri + "/add/" + toDoItem.description;
                ResponseEntity<String> response = template.postForEntity(url, null, String.class);

                System.out.println(response);
            } else if (toDoItem.done) {
                String url = uri + "/setDone/" + toDoItem.description;
                ResponseEntity<String> response = template.postForEntity(url, null, String.class);

                System.out.println(response);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ToDoCommandServiceApplication.class, args);
    }
}


