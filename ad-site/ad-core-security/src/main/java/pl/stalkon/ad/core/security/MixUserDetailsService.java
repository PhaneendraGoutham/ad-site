package pl.stalkon.ad.core.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetailsService;

public interface MixUserDetailsService extends UserDetailsService, SocialUserDetailsService {

}
