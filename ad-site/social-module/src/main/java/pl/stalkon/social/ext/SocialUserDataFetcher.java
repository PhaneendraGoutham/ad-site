package pl.stalkon.social.ext;

import pl.styall.library.core.model.defaultimpl.User;

public interface SocialUserDataFetcher<A> {
	User fetchData(A api);
}
