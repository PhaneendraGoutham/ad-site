package pl.stalkon.ad.core.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import pl.stalkon.ad.core.model.UserRoleDef;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.spring.DatabasePopulator;

public class UserRolePopulator implements DatabasePopulator {


	@Override
	public void populate(Session currentSession) {
		List<UserRole> userRoles = (List<UserRole>) currentSession.createQuery("from UserRole").list();
		List<UserRole> userRolesToAdd = new ArrayList<UserRole>();
		for(UserRoleDef userRoleDef: UserRoleDef.values()){
			UserRole ur = new UserRole(userRoleDef.value());
			if(!userRoles.contains(ur)){
				userRolesToAdd.add(ur);
			}
		}
		
		for(UserRole userRole: userRolesToAdd){
			currentSession.save(userRole);
		}
	}



}
