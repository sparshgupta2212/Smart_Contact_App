package com.spring.contact.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
public class ApplicationException {
	
	@ExceptionHandler(EmptyInputException.class)
	public ResponseEntity<String> handleEmptyInput(EmptyInputException emptyinputException){
		return new ResponseEntity<String>("Input field ,",HttpStatus.BAD_REQUEST);
	}
	

//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public Map<String,String> handleInvalidArgument(MethodArgunmentNotValidException ex){
//		Map<String,String> errorMap=new HashMap<>();
//		ex.getBindingResult().getFieldErrors().forEach(error->{
//			errorMap.put(error.getField(),error.getDefaultMessage());
//		});
//		return errorMap;
//	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(EmptyInputException emptyInputException){
		return new ResponseEntity<String>("no such element",HttpStatus.BAD_REQUEST);
	}
	
	
}
