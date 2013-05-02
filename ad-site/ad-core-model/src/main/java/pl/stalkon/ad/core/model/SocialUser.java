package pl.stalkon.ad.core.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;

import pl.stalkon.social.singleconnection.interfaces.RemoteUser;

@Entity
// @org.hibernate.annotations.Table(appliesTo = "SocialUser", indexes = {
// @org.hibernate.annotations.Index(name = "idxSocialProviderUser", columnNames
// = {
// "users_id", "providerId", "providerUserId" }),
// @org.hibernate.annotations.Index(name = "idxSocialProvider", columnNames = {
// "providerId", "providerUserId" }) })
public class SocialUser implements RemoteUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7724831297472842816L;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "socialUserId", nullable = true, insertable = false, updatable = false)
	private Long id;
	
	@Column
	private Long expireTime;
	
	@Column(length = 25, nullable = false)
	private String providerUserId;
	
	@Column(length = 25, nullable = false)
	@Index(name = "idxSocialProviderId")
	private String providerId;
	
	@Column(length = 55)
	private String secret;
	
	@Column(length = 55)
	private String displayName;
	
	@Column
	private String profileUrl;
	
	private String accessToken;
	
	
	@Column(length = 70)
	private String refreshToken;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name="userId")
	private User user;

	public SocialUser() {
	}

	public SocialUser(User user, String providerId, String providerUserId,
			String displayName, String profileUrl, String accessToken,
			String secret, String refreshToken, Long expireTime) {
		super();
		this.expireTime = expireTime;
		this.providerUserId = providerUserId;
		this.providerId = providerId;
		this.secret = secret;
		this.displayName = displayName;
		this.profileUrl = profileUrl;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = user;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String provider) {
		this.providerUserId = provider;
	}


	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}


	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}


	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	@Transient
	public Long getUserId() {
		if (user != null) {
			return user.getId();
		}
		return null;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((providerUserId == null) ? 0 : providerUserId.hashCode());
		result = prime * result
				+ ((providerId == null) ? 0 : providerId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;
		SocialUser other = (SocialUser) obj;
		if (providerUserId == null) {
			if (other.providerUserId != null)
				return false;
		} else if (!providerUserId.equals(other.providerUserId))
			return false;
		if (providerId == null) {
			if (other.providerId != null)
				return false;
		} else if (!providerId.equals(other.providerId))
			return false;
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
