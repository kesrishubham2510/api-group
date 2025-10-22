//package com.myreflectionthoughts.group.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//
//public class JwtUtility {
//
//    private final int jwtTokenExpiryMinutes = 1;
//    private final int refreshTokenExpiryDays = 7;
//    private final String keyString = "f8c1a74d5e2b4b6c9120e34f8a7c1b2d93f66d4c9ab27fe10a8b3e29dff82a1c";
//    private final Key key = Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));
//
//    public String extractToken(String tokenHeader){
//        return tokenHeader.split("Bearer ")[1];
//    }
//
//    public String extractUsername(String token){
//        return extractAllClaims(token).getSubject();
//    }
//
//    public String extractClaim(String claimKey, String token){
//
//        Claims claims = extractAllClaims(token);
//        return claims.get("role", String.class);
//    }
//
//
//    public Claims extractAllClaims(String token){
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getPayload();
//    }
//}
