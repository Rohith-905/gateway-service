package com.gatewayService;

import java.security.Key;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	  private final String SECRET = "hm1kq+MhgyH1WJ+pCPOyxrPMc/0WNeJOIXjwu09JliX7h3Fkni0aRzRrLFXlyZi/qPC23QJ2bL34XxXaQaDEG1lW67mTcEjsQZI4xOeTxdgyUmltQYy/Q2e6FvyiyvAO9VxlaDdGkAlRQOJdPZU3u7wXTaS+pu5Bv15/STaOGo9H2hcilAXh3ulFo4WhxuJ3p4YwGJIZ0+yneue/uQDRwCYNamIyUjGTaSbD5j3KWIPieWP4yM2KGOGQe6W/vXSeEOghy6RLc5zeKpvXHC2Y2IXI+oUgrxMyZFtjPWQX1obcCMrTHLiMfk0eaISUEa4sPM6OfGvkitS+8XcuIO85dLCZ45rJYmWkBQe3kdicDbM=";

	    // Generate the signing key from the secret
	    private Key getSignKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

	    // Validate the token against the user details
	    public void validateToken(String token) {
	    	Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	    }
	    private Claims getClaimsFromToken(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
	    public List<String> getRolesFromToken(String token) {
	        return getClaimsFromToken(token).get("roles", List.class);
	    }

}