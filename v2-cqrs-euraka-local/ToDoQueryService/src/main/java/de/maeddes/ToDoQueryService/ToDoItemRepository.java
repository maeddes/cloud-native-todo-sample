package de.maeddes.ToDoQueryService;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "todoitems")
interface ToDoItemRepository extends PagingAndSortingRepository<ToDoItem,String> {

    List<ToDoItem> findAll();

}

