package pl.stalkon.ad.extensions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

public class CaptchaValidator {

	@Autowired
	private CaptchaService captchaService;

	protected void validateCaptcha(String captcha,
			BindingResult result, String sessionId) {
		// If the captcha field is already rejected
		if (null != result.getFieldError("captcha"))
			return;
		boolean validCaptcha = false;
		try {
			validCaptcha = captchaService.validateResponseForID(sessionId,
					captcha);
		} catch (CaptchaServiceException e) {
			// should not happen, may be thrown if the id is not valid
		}
		if (!validCaptcha) {
			result.rejectValue("captcha", "{constraints.captcha}");
		}
	}
}
