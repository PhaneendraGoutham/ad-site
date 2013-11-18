package pl.stalkon.ad.core.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import pl.styall.library.core.security.rest.UserStatusMapper;

public class UserStatusMapperImpl implements UserStatusMapper {

	@Override
	public Map<String, Object> map(Object principal) {
		SocialLoggedUser user = (SocialLoggedUser) principal;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("adult", user.getAdult());
		map.put("displayName", user.getDisplayName());
		map.put("id", user.getId());
		map.put("imageUrl", user.getImageUrl());
		List<String> roles = new ArrayList<String>(user.getAuthorities().size());
		for(GrantedAuthority au: user.getAuthorities()){
			roles.add(au.getAuthority().replace("ROLE_", "").toLowerCase());
		}
		map.put("roles", roles);
		return map;
	}

}
