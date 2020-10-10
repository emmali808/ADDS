package com.java.adds.utils;

import com.java.adds.config.JwtConfig;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Describe: JWT(JSON Web Token) Util: generate / verify token
 * Create Date: Mar. 15th 2020
 * @author QXL
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtProperties;

    /**
     * Describe: Sign TOKEN
     * Create Date: Mar. 16th 2020
     * @param account Usr(Sys) account
     * @param authorities Usr(Sys) permissions
     * @return [String] TOKEN
     */
    public String sign(String account, Collection<? extends GrantedAuthority> authorities) {
        Supplier<String> createToken = () -> this.generateToken(account, authorities);
        return createToken.get();
    }

    /**
     * Describe: Generate TOKEN
     * Create Date: Mar. 16th 2020
     * @param account Usr(Sys) account
     * @param grantedAuthorities Usr(Sys) permissions
     * @return [String] TOKEN
     */
    private String generateToken(String account, Collection<? extends GrantedAuthority> grantedAuthorities) {

        // Format grantedAuthorities to a String like "a,b,..."
        String authorities = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        // Get TOKEN period of validity
        Duration periodOfValidity = this.jwtProperties.getExpireTime();
        // Figure out TOKEN expire date
        Date expireDate = new Date(System.currentTimeMillis() + periodOfValidity.toMillis());

//        System.out.println("[QXL's LOG: JwtUtil -> generateToken] Account: " + account);
//        System.out.println("[QXL's LOG: JwtUtil -> generateToken] Authorities: " + authorities);
//        System.out.println("[QXL's LOG: JwtUtil -> generateToken] Expire Date: " + expireDate);

        // Token type like "Bearer "
        String prefix = this.jwtProperties.getTokenType() + " ";
        String token = Jwts
                .builder()
                // Set account
                .setSubject(account)
                // Add authorities
                .claim(this.jwtProperties.getClaimKeyAuth(), authorities)
                // Set expire date
                .setExpiration(expireDate)
                // Set secret key
                .signWith(SignatureAlgorithm.HS256, this.jwtProperties.getSecretKey())
                // Done!
                .compact();

        return prefix + token;
    }

    /**
     * Describe: Get Usr(Sys) account from TOKEN
     * Create Date: Mar. 16th 2020
     * @param token TOKEN
     * @return [String] Usr(Sys) account
     */
    public String getAccount(String token) {
        Claims claims = this.getClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * Describe: Validate TOKEN
     * Create Date: Mar. 16th 2020
     * @param token TOKEN
     * @return [boolean] The TOKEN is valid(true) or not(false)
     */
    public boolean validateToken(String token) {
        return this.parseToken(token) != null;
    }

    /**
     * Describe: Get UsernamePasswordAuthenticationToken from TOKEN
     * Create Date: Mar. 16th 2020
     * @param account Usr(Sys) account
     * @param token TOKEN
     * @return [UsernamePasswordAuthenticationToken] A UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String account, String token) {
        // Parse token payload
        Claims claims = this.getClaims(token);
        // What's this?
        // 因为 JwtAuthenticationFilter 拦截器已经检查过 token 有效，所以可以忽略 get 空指针提示
        assert claims != null;
        String claimKeyAuth = this.jwtProperties.getClaimKeyAuth();
        // A list of Usr(Sys) permissions
        List<String> authorityList = Arrays.asList(claims.get(claimKeyAuth).toString().split(","));
        // Format the list to a GrantedAuthority Collection
        Collection<? extends GrantedAuthority> authorities = authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

//        System.out.println("[QXL's LOG: JwtUtil -> getAuthentication] Authorities: ");
//        for (GrantedAuthority grantedAuthority : authorities) {
//            System.out.println("[QXL's LOG: JwtUtil -> getAuthentication] " + grantedAuthority);
//        }

        User user = new User(account, "", authorities);
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    /**
     * Describe: Get Claims from TOKEN
     * Create Date: Mar. 16th 2020
     * @param token TOKEN
     * @return [Claims] Claims
     */
    private Claims getClaims(String token) {
        Jws<Claims> jws = this.parseToken(token);
        return jws == null ? null : jws.getBody();
    }

    /**
     * Describe: Parse TOKEN
     * Create Date: Mar. 16th 2020
     * @param token TOKEN
     * @return [Claims-Jws] A Claims-Jws result
     */
    private Jws<Claims> parseToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(this.jwtProperties.getSecretKey())
                    .parseClaimsJws(token.replace(this.jwtProperties.getTokenType() + " ", ""));
        } catch (SignatureException e) {
            // Invalid JWT signature
            // 签名异常
            System.out.println("[QXL's LOG: JwtUtil -> parseToken] SignatureException: Invalid JWT signature. ");
//            e.printStackTrace();
        } catch (MalformedJwtException e) {
            // Invalid JWT signature
            // 格式错误
            System.out.println("[QXL's LOG: JwtUtil -> parseToken] MalformedJwtException: Invalid JWT signature. ");
            System.out.println(token);
//            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            // Expired JWT token
            // 过期
            System.out.println("[QXL's LOG: JwtUtil -> parseToken] ExpiredJwtException: Expired JWT token. ");
//            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            // Unsupported JWT token
            // 不支持该 JWT
            System.out.println("[QXL's LOG: JwtUtil -> parseToken] UnsupportedJwtException: Unsupported JWT token. ");
//            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // JWT token compact of handler are invalid
            // 参数错误异常
            System.out.println("[QXL's LOG: JwtUtil -> parseToken] IllegalArgumentException: JWT token compact of handler are invalid. ");
//            e.printStackTrace();
        }
        return null;
    }
}
