package de.maeddes.ToDoUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
@SpringBootApplication
@EnableDiscoveryClient
public class ToDoUiApplication {

	@GetMapping("/test")
    public String test(){

	    return "Ok";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getItems(Model model){

		System.out.println("In getItems: "+model);

		RestTemplate template = new RestTemplate();
        String url = "http://localhost:9082/todos/";
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

		System.out.println("UI.addItem: "+toDo);
		if(toDo == null || toDo.equals(""))  return "redirect:/";
		
		RestTemplate template = new RestTemplate();
        String url = "http://localhost:9081/todo/"+toDo;

        ResponseEntity<String> response = template.postForEntity(url, null, String.class);
        System.out.println("UI.addItem - POST Response: "+ response.getBody());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/";

    }

    @RequestMapping(value = "/done/{toDo}", method = RequestMethod.POST)
    public String setItemDone(@PathVariable String toDo){
	   
		System.out.println("UI.setItemDone: "+toDo);
		if(toDo == null || toDo.equals(""))  return "redirect:/";

		RestTemplate template = new RestTemplate();
        String url = "http://localhost:9081/done/"+toDo;

        ResponseEntity<String> response = template.postForEntity(url, null, String.class);
        System.out.println("UI.setItemDone - POST Response: "+ response.getBody());

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
