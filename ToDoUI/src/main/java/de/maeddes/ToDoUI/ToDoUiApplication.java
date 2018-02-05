package de.maeddes.ToDoUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
@SpringBootApplication
public class ToDoUiApplication {

	@RequestMapping("/test")
    public String test(){

        return "Ok";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getItems(Model model){

		RestTemplate template = new RestTemplate();
        String url = "http://localhost:9082/todos/";
        ResponseEntity<String[]> response = template.getForEntity(url, String[].class);

        System.out.println(response);
        System.out.println("In getItems: ");

        if(response != null){
            model.addAttribute("items", response.getBody());
        }
        System.out.println("In getItems: "+model);

        model.addAttribute("name","Mirna");
        return "items";

	}
	
    @RequestMapping(method = RequestMethod.POST)
    public String addItem(String toDo){

        RestTemplate template = new RestTemplate();
        String url = "http://localhost:9081/todo/"+toDo;
        
        Set<HttpMethod> optionsForAllow = template.optionsForAllow(url);
        System.out.println(optionsForAllow);

        ResponseEntity<String> response = template.getForEntity(url, String.class);
        System.out.println("get: "+ response.getBody());

        url = "http://localhost:9082/post/";

        response = template.postForEntity(url, null, String.class);
        System.out.println("post: "+ response.getBody());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/";

    }

    @RequestMapping(value = "/done/{id}", method = RequestMethod.POST)
    public String setItemDone(@PathVariable int id){

        System.out.println("In setItemDone: "+id);
        this.send("done:"+id);

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
