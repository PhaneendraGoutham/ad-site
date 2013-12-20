package pl.stalkon.ad.extensions;

import org.springframework.http.HttpStatus;

import pl.styall.library.core.ext.HttpException;

public class NotFoundException extends HttpException {

	public NotFoundException() {
		super(404);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


}
