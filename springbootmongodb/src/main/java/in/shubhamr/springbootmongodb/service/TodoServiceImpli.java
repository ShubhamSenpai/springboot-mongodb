package in.shubhamr.springbootmongodb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.shubhamr.springbootmongodb.exception.TodoCollectionException;
import in.shubhamr.springbootmongodb.model.TodoDTO;
import in.shubhamr.springbootmongodb.repository.TodoRepository;

@Service
public class TodoServiceImpli implements TodoService {

	@Autowired
	private TodoRepository todoRepo;
	
	@Override
	public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
		
		Optional<TodoDTO> todoOptional=todoRepo.findByTodo(todo.getTodo());
		if(todoOptional.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExistis());
		}else {
			todo.setCreatedAt(new Date(System.currentTimeMillis()));
			todoRepo.save(todo);
		}
		
	}

	@Override
	public List<TodoDTO> getAllTodos() {
	List<TodoDTO> todos=todoRepo.findAll();
	if(todos.size()>0) {
		return todos;
	}else {
		return new ArrayList<TodoDTO>();
	}
	}

	@Override
	public TodoDTO getSingleTodo(String id) throws TodoCollectionException {
		Optional<TodoDTO> optionalTodo=todoRepo.findById(id);
		if(!optionalTodo.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}else {
			return optionalTodo.get();
		}
	}

	@Override
	public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
		Optional<TodoDTO>todoWithId=todoRepo.findById(id);
		Optional<TodoDTO>todoWithSameName=todoRepo.findByTodo(todo.getTodo());
		if(todoWithId.isPresent()) {
			if(todoWithSameName.isPresent()&&todoWithSameName.get().getId().equals(id)) {
				throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExistis());
				}
			TodoDTO todoToupdate=todoWithId.get();
			
			todoToupdate.setTodo(todo.getTodo());
			todoToupdate.setDescription(todo.getDescription());
			todoToupdate.setCompleted(todo.getCompleted());
			todoToupdate.setUpdatedAt(new Date(System.currentTimeMillis()));
			todoRepo.save(todoToupdate);
		}else
		throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
	}

	@Override
	public void deleteTodoByID(String id) throws TodoCollectionException {
		Optional<TodoDTO>todoOptional=todoRepo.findById(id);
		if(!todoOptional.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}else {
			todoRepo.deleteById(id);
		}
		
	}

	private String TodoCollectionException(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
