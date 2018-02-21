package de.maeddes.ToDoUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/")
@SpringBootApplication
@EnableDiscoveryClient
public class ToDoUiApplication {

    @Autowired
    DiscoveryClient discoveryClient;

	@GetMapping("/test")
    public String test(){

	    return "Ok";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getItems(Model model){

		System.out.println("In getItems: "+model);

		RestTemplate template = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("ToDoQueryService");

        URI uri = null;
        if (instances != null && instances.size() > 0) {
            uri = instances.get(0).getUri();
        }

        System.out.println("In handleRequest: URI from Eureka: " + uri.toString());

        String url = uri+"/todos/";
        ResponseEntity<String[]> response = template.getForEntity(url, String[].class);

        System.out.println("In getItems: "+response);

        if(response != null){
            model.addAttribute("items", response.getBody());
        }
        
        model.addAttribute("name","Mirna");
        return "items";

	}
	
    @RequestMapping(method = RequestMethod.POST)
    public String addItem(String toDo){

		return this.handleCommand(toDo, false);

    }

    @RequestMapping(value = "/done/{toDo}", method = RequestMethod.POST)
    public String setItemDone(@PathVariable String toDo){

        return this.handleCommand(toDo, true);

    }

    private String handleCommand(String toDo, boolean done){

        System.out.println("UI.handleCommand: "+toDo+" done: "+done);
        if(toDo == null || toDo.equals(""))  return "redirect:/";

        RestTemplate template = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("ToDoCommandService");

        URI uri = null;
        if (instances != null && instances.size() > 0) {
            uri = instances.get(0).getUri();
        }
        System.out.println("In handleRequest: URI from Eureka: " + uri.toString());

        String suffix = done ? "/done/"  :  "/todo/";
        String url = uri+suffix+toDo;

        System.out.println("In handleRequest: final URL: " + url);

        ResponseEntity<String> response = template.postForEntity(url, null, String.class);
        System.out.println("UI.addItem - POST Response: "+ response.getBody());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/";

    }

	public static void main(String[] args) {
		SpringApplication.run(ToDoUiApplication.class, args);
	}
}
