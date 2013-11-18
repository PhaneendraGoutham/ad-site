package pl.stalkon.ad.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.stalkon.ad.core.model.service.UserService;
import pl.styall.library.core.validators.UserValidator;

@ControllerAdvice
public class CentralControllerHandler {

	@Autowired
	private UserService userService;
	
	@InitBinder("userRegForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator(userService));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleException(
			MethodArgumentNotValidException ex) {
		return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
	}
	

}
