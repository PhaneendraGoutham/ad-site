package pl.stalkon.ad.extensions;

import pl.styall.library.core.ext.HttpException;

public class ContestFinishedException extends HttpException{

	public ContestFinishedException() {
		super(442);
	}

	private static final long serialVersionUID = -6937845626376935360L;


}
