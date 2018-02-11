package de.maeddes.ToDoCommandService;

import java.util.Set;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    private Queue queue;

    public void send(String message) {
        this.rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }

    @GetMapping("/test")
    public String test(){

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:9082/test/";
        
        Set<HttpMethod> optionsForAllow = template.optionsForAllow(url);
        System.out.println(optionsForAllow);

        ResponseEntity<String> response = template.getForEntity(url, String.class);
        System.out.println("get: "+ response.getBody());

        url = "http://localhost:9082/post/";

        response = template.postForEntity(url, null, String.class);
        System.out.println("post: "+ response.getBody());

        url = "http://localhost:9082/postParam/TestString";

        response = template.postForEntity(url, null, String.class);
        System.out.println("post: "+ response.getBody());
        
        return "done";
    }


    @RequestMapping(value = "/todo/{toDo}", method = RequestMethod.POST)
    public String addItem(@PathVariable String toDo){

        ToDoItem toDoItem = new ToDoItem(toDo, false);
        this.handleRequest(toDoItem);
        return toDoItem.toString();

    }

    @RequestMapping(value = "/done/{toDo}", method = RequestMethod.POST)
    public String setItemDone(@PathVariable String toDo){

        ToDoItem toDoItem = new ToDoItem(toDo, true);
        this.handleRequest(toDoItem);
        return toDoItem.toString();

    }

    private void handleRequest(ToDoItem toDoItem){

        System.out.println("In handleRequest: "+toDoItem);
        this.send(toDoItem.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        // MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        // HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        // ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
        // map.add("toDo", toDoItem.description);

        if(!toDoItem.done){
            String url = "http://localhost:9082/add/"+toDoItem.description;
            ResponseEntity<String> response = template.postForEntity(url, null, String.class);

            System.out.println(response);
        }
        else if(toDoItem.done){
            String url = "http://localhost:9082/setDone/"+toDoItem.description;
            ResponseEntity<String> response = template.postForEntity(url, null, String.class);

            System.out.println(response);
        }
    }

	public static void main(String[] args) {
		SpringApplication.run(ToDoCommandServiceApplication.class, args);
	}
}

/* @RabbitListener(queues = "my-queue")
class ToDoListCommandReceiver {

    @RabbitHandler
    public void receive(String in) {
        System.out.println("Received '" + in + "'");
        if (in.startsWith("done:")) {

            // unsafe hack
            String id = in.split(":")[1];
            if (id != null) {

                try {
                    //this.toDoItemRepository.delete(new Integer(id));
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }
            return;

        } else {

            try {
                //this.toDoItemRepository.save(new ToDoItem(in));
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
    }

} */

