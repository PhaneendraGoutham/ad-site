package pl.stalkon.ad.core.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.stalkon.social.singleconnection.interfaces.SocialUserDetailsService;


public interface MixUserDetailsService extends UserDetailsService, SocialUserDetailsService {

}
