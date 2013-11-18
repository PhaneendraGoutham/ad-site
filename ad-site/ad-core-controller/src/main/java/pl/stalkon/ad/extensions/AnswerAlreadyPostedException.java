package pl.stalkon.ad.extensions;

import pl.styall.library.core.ext.HttpException;


public class AnswerAlreadyPostedException extends HttpException {

	public AnswerAlreadyPostedException() {
		super(440);
	}

	private static final long serialVersionUID = 1487746731884665468L;
	
	


}
