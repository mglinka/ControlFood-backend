package pl.lodz.pl.it.security;

import pl.lodz.pl.it.config.ConfigurationProperties;
import pl.lodz.pl.it.config.KeyGenerator;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.JWTWhitelistToken;
import pl.lodz.pl.it.auth.repository.JWTWhitelistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    private final UserDetailsService userDetailsService;

    private final JWTWhitelistRepository jwtWhitelistRepository;
    private final String SECRET_KEY;

    private final ConfigurationProperties config;

    public JwtService(UserDetailsService userDetailsService,
                      JWTWhitelistRepository jwtWhitelistRepository,
                      ConfigurationProperties config) {
        this.userDetailsService = userDetailsService;
        this.SECRET_KEY = KeyGenerator.getSecretKey();
        this.jwtWhitelistRepository = jwtWhitelistRepository;
        this.config = config;
    }

    @PreAuthorize("permitAll()")
    @Transactional
    public String generateToken(Map<String, Object> claims, Account account) {
//        if (!account.getNonLocked()) {
//            throw new AccountLockedException(ExceptionMessages.ACCOUNT_LOCKED);
//        }

        var authorities = account.getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        String token = Jwts
                .builder()
                .setClaims(claims)
                .claim("role", roles)
                .setId(account.getId().toString())
                .setSubject(account.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + config.getJwtExpiration()))
                .signWith(getSecretKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
        jwtWhitelistRepository.saveAndFlush(new JWTWhitelistToken(token, extractExpiration(token, SECRET_KEY), account));
        return token;
    }

    @Transactional
    public void authenticate(String login, String token, HttpServletRequest request) {
        Account account = (Account) userDetailsService.loadUserByUsername(login);

        if (jwtWhitelistRepository.existsByToken(token) && isTokenValid(token, account, SECRET_KEY)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }


    public static boolean isTokenValid(String token, Account account, String secretKey) {
        final String login = extractLogin(token, secretKey);
        return (login.equals(account.getUsername())) && !isTokenExpired(token, secretKey);
    }

    private static boolean isTokenExpired(String token, String secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    public static Date extractExpiration(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    public static String extractLogin(String token, String secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token, String secretKey) {
        return Jwts
                .parser()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static SecretKey getSecretKey(String secretKeyString) {
        byte[] secretKey = Decoders.BASE64.decode(secretKeyString);
        return Keys.hmacShaKeyFor(secretKey);
    }

}
