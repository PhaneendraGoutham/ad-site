package pl.stalkon.ad.core.model.service;

import java.util.List;

import pl.stalkon.ad.core.model.UserInfo;
import pl.stalkon.ad.core.model.UserInfo.Type;
import pl.styall.library.core.security.filter.UserMessagesCountProvider;

public interface UserInfoService extends UserMessagesCountProvider {

	public List<UserInfo> getMessages(Long userId, Boolean handled);
	public UserInfo getWithContest(Long userInfoId);
	public boolean setHandled(Long userInfoId, boolean handled, Type type);
}
