package com.store.security.service.impl;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.store.demo.exception.TokenException;
import com.store.security.entities.KeyEntity;
import com.store.security.entities.User;

import com.store.security.repository.KeyRepository;
import com.store.security.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

	private String publicKey;
	private String privateKey;
	@Autowired
	KeyRepository keyRepository;
	
	private Long accessExpirationMs=(long) (1000 * 60 * 60* 1);

	public JwtServiceImpl() {
		super();
	}
	
	@PostConstruct
    private void postConstruct() {

		KeyPairGenerator kpg;
		try {
			// the token is encrypted with RSA key pair and store in the repository
			// verify if it is already in the repository
			Optional<KeyEntity> keysP = keyRepository.findByStatus("ACT");
			if(keysP.isPresent()) {
				KeyEntity keys = keysP.get();
				publicKey= keys.getPublicKey();
				privateKey= keys.getPrivateKey();
			}else {
				// other case created key pair and save in the repository
				kpg = KeyPairGenerator.getInstance("RSA");

				kpg.initialize(2048);
				KeyPair kp = kpg.generateKeyPair();

				publicKey = Base64.encodeBase64String(kp.getPublic().getEncoded());
				privateKey = Base64.encodeBase64String(kp.getPrivate().getEncoded());
				KeyEntity keys =KeyEntity.builder().publicKey(publicKey).privateKey(privateKey).status("ACT").build();
			    keyRepository.save(keys);
			}
			
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
    }

	@Override
	public String extractUserName(String token) {
		
		try {
			return      Jwts.parserBuilder().setSigningKey(generateJwtKeyDecryption(publicKey)).build().parseClaimsJws(token).getBody().getSubject();
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
				| IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String generateToken(User userDetails) {
		try {
			
			return generateAccessToken(userDetails.getUsername(),
					List.of(new String(userDetails.getRole().name()))
					,privateKey
					);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isTokenValid(String token) {
		return validateJwtToken(token);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token).get();
		return claimsResolvers.apply(claims);
	}



	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Optional<Claims> extractAllClaims(String token) {
		
		 try {
			Claims claims = Jwts.parserBuilder().setSigningKey(generateJwtKeyDecryption(publicKey)).build().parseClaimsJws(token).getBody();
	           return  Optional.ofNullable(claims) ;
	           
	          
	        } catch (SignatureException e) {
	            log.error("Invalid JWT signature: {}"+ e.getMessage());
	        } catch (MalformedJwtException e) {
	        	log.error("Invalid JWT token: {}"+ e.getMessage());
	        } catch (ExpiredJwtException e) {
	            log.error("JWT token is expired: {}"+ e.getMessage());
	        } catch (UnsupportedJwtException e) {
	            log.error("JWT token is unsupported: {}"+ e.getMessage());
	        } catch (IllegalArgumentException e) {
	            log.error("JWT claims string is empty: {}"+ e.getMessage());
	        } catch (NoSuchAlgorithmException e) {
	            log.error("no such algorithm exception");
	        } catch (InvalidKeySpecException e) {
	            log.error("invalid key exception");
	        }	
		 
		 return Optional.empty();
		
	}



	
	  public String generateAccessToken(String userName,  List<String> roleArray,String jwtPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
	        return Jwts.builder()
	                .setSubject(userName)
	                .claim("roles", roleArray)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date((new Date()).getTime() + accessExpirationMs))
	                .signWith(generateJwtKeyEncryption(jwtPrivateKey),SignatureAlgorithm.RS256)
	                .compact();
	    }

	    public PublicKey generateJwtKeyDecryption(String jwtPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] keyBytes = Base64.decodeBase64(jwtPublicKey);
	        X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(keyBytes);
	        return keyFactory.generatePublic(x509EncodedKeySpec);
	    }

	    public PrivateKey generateJwtKeyEncryption(String jwtPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] keyBytes = Base64.decodeBase64(jwtPrivateKey);
	        PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(keyBytes);
	        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
	    }

	    public boolean validateJwtToken(String authToken) {
	        try {
	           Claims body = Jwts.parserBuilder().setSigningKey(generateJwtKeyDecryption(publicKey)).build().parseClaimsJws(authToken).getBody();
	           
	            return true;
	        } catch (SignatureException e) {
	            log.error("Invalid JWT signature: {}"+ e.getMessage());
	            throw new TokenException("Invalid JWT signature: {}");
	        } catch (MalformedJwtException e) {
	            log.error("Invalid JWT token: {}"+ e.getMessage());
	            throw new TokenException("Invalid JWT token: {}");
	        } catch (ExpiredJwtException e) {
	            log.error("JWT token is expired: {}"+ e.getMessage());
	            throw new TokenException("JWT token is expired: {}");
	        } catch (UnsupportedJwtException e) {
	            log.error("JWT token is unsupported: {}"+ e.getMessage());
	            throw new TokenException("JWT token is unsupported: {}");
	        } catch (IllegalArgumentException e) {
	            log.error("JWT claims string is empty: {}"+ e.getMessage());
	            throw new TokenException("JWT claims string is empty: {}");
	        } catch (NoSuchAlgorithmException e) {
	            log.error("no such algorithm exception");
	            throw new TokenException("no such algorithm exception");
	        } catch (InvalidKeySpecException e) {
	            log.error("invalid key exception");
	            throw new TokenException("invalid key exception");
	        }

	     
	    }
}
