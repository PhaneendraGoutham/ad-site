package pl.stalkon.ad.core.model.search.ext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import pl.styall.library.spring.DatabasePopulator;

public class IndexRebuilder implements DatabasePopulator {


	private boolean rebuid = false;
	private SessionFactory sessionFactory;
	

	public IndexRebuilder(boolean rebuid, SessionFactory sessionFactory) {
		this.rebuid = rebuid;
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void populate(Session currentSession) {
		if (rebuid) {
			System.out.println("rebuild");
			FullTextSession fullTextSession = Search
					.getFullTextSession(sessionFactory.getCurrentSession());
			try {
				fullTextSession.createIndexer().startAndWait();
			} catch (InterruptedException e) {
				// TODO: what to do here
			}
		}
	}

}