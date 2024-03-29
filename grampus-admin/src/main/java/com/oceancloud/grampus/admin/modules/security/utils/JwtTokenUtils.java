package com.oceancloud.grampus.admin.modules.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * Jwt token工具
 *
 * @author Beck
 * @since 2020-12-5
 */
@Deprecated
@Slf4j
@Component
public class JwtTokenUtils {

	private static final String AUTHORITIES_KEY = "auth";

	/**
	 * minimum SHA_256 secretKey string length.
	 */
	private static final int SHA_256_SECRET_CHAR_SIZE = 256 / 8;

	/**
	 * default SHA_256 secretKey flag.
	 */
	private static final String DEFAULT_SECRET_FLAG = "default";

	/**
	 * custom SHA_256 secretKey from config property.
	 */
	@Value("${grampus.security.token.secret-key:default}")
	private String customSecretKeyStr;

	/**
	 * secret key.
	 */
	private SecretKey secretKey;

	/**
	 * Token validity time(ms).
	 */
	private long tokenValidityInMilliseconds;

	/**
	 * Init.
	 */
	@PostConstruct
	public void init() {
		//use default secretKey for SHA-256
		if (customSecretKeyStr == null || DEFAULT_SECRET_FLAG.equals(customSecretKeyStr)) {
			this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		} else {
			//use custom secretKey
			int size = customSecretKeyStr.length();
			int left = SHA_256_SECRET_CHAR_SIZE - size;
			if (left > 0) {
				//character for padding
				StringBuilder stringBuilder = new StringBuilder(customSecretKeyStr);
				for (int i = 0; i < left; i++) {
					stringBuilder.append(i % 10);
				}
				this.secretKey = Keys.hmacShaKeyFor(stringBuilder.toString().getBytes());
			} else {
				this.secretKey = Keys.hmacShaKeyFor(customSecretKeyStr.getBytes());
			}
		}
		this.tokenValidityInMilliseconds = 1000 * 60 * 30L;
	}

	/**
	 * Create token.
	 *
	 * @param authentication auth info
	 * @return token
	 */
	public String createToken(Authentication authentication) {

		long now = System.currentTimeMillis();

		Date validity = new Date(now + this.tokenValidityInMilliseconds);

		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, "").setExpiration(validity)
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Get auth Info.
	 *
	 * @param token token
	 * @return auth info
	 */
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		List<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	/**
	 * validate token.
	 *
	 * @param token token
	 * @return whether valid
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace:", e);
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace:", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace:", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace:", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace:", e);
		}
		return false;
	}
}
