package de.maeddes.ToDoQueryService;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "todoitems")
interface ToDoItemRepository extends PagingAndSortingRepository<ToDoItem,String> {

    List<ToDoItem> findAll();

}
