package de.maeddes.ToDoQueryService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ToDoQueryServiceApplication {

	@Autowired
    ToDoItemRepository toDoItemRepository;

    @RequestMapping("/test")
    public String test(){
        return "Ok";
	}
	
	@PostMapping("/post")
	public @ResponseBody ResponseEntity<String> post() {
		return new ResponseEntity<String>("POST Response", HttpStatus.OK);
	}

	@PostMapping("/postParam/{name}")
	public @ResponseBody ResponseEntity<String> postParam(@PathVariable("name") String name) {
		System.out.println("Received: "+name);
		return new ResponseEntity<String>("POST Response", HttpStatus.OK);
	}

    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public List<String> getItems(){

        System.out.println("In getItems: ");
		List<ToDoItem> items = toDoItemRepository.findAll();
		List<String> toDos = new ArrayList<String>();
		for(int i = 0; i < items.size(); i++){

			boolean add = toDos.add(items.get(i).getDescription());

		}
        return toDos;

	}
	
	@RequestMapping(value = "/setDone/{toDo}", method = RequestMethod.POST)
    public String setDone(@PathVariable String toDo){

        System.out.println("In setDone: "+toDo);
        toDoItemRepository.delete(toDo);
        return toDo+" done!";

	}
	
	@RequestMapping(value = "/add/{toDo}", method = RequestMethod.POST, headers = "Accept=application/json")
    public String add(@PathVariable String toDo){

        System.out.println("In add: "+toDo);
        toDoItemRepository.save(new ToDoItem(toDo));
        return toDo+" added!";

    }

	public static void main(String[] args) {
		SpringApplication.run(ToDoQueryServiceApplication.class, args);
	}
}
