package pl.stalkon.social.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import pl.styall.library.core.security.authentication.LoggedUser;
import pl.styall.library.core.security.authentication.LoggedUser.LoggedType;



@Component
public class SimpleSignInAdapter implements SignInAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
	
	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		LoggedUser user = (LoggedUser) userDetailsService.loadUserByUsername(userId);
		if(user == null){
			return null;
		}
		user.setType(LoggedType.SOCIAL);
		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		persistentTokenBasedRememberMeServices.loginSuccess((HttpServletRequest) request.getNativeRequest(),
		        (HttpServletResponse) request.getNativeResponse(),
		        auth);
		return "user/login";
	}
	
}
