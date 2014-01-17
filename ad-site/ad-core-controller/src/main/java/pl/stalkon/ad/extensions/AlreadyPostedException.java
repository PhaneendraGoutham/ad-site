package pl.stalkon.ad.extensions;

import pl.styall.library.core.ext.HttpException;


public class AlreadyPostedException extends HttpException {

	public AlreadyPostedException() {
		super(443);
	}

	private static final long serialVersionUID = 1487746731884665468L;
	
	


}
