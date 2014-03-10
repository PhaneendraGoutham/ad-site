package pl.stalkon.ad.sitemap;

import org.springframework.batch.item.ItemWriterException;

public class PhantomJSFailResponseException extends ItemWriterException {

	public PhantomJSFailResponseException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 6931485985419553495L;

}
