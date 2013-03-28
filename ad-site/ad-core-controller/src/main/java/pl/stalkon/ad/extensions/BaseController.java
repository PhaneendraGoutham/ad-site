package pl.stalkon.ad.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class BaseController {

//	@ExceptionHandler(ValidationException.class)
//	public String handleNotUniqueMailException(Session session, HttpServletRequest request) {
//		return "user/login";
//	}
}
