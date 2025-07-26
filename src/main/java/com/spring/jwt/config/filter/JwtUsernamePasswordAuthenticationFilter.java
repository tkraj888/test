package com.spring.jwt.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.dto.LoginRequest;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.jwt.JwtConfig;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.security.UserDetailsCustom;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.HelperUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter implements Ordered {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager manager,
                                                   JwtConfig jwtConfig,
                                                   JwtService jwtService,
                                                   UserRepository userRepository){
        super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
        setAuthenticationManager(manager);
        this.objectMapper = new ObjectMapper();
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 130;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("Start attempt to authentication");
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        log.info("End attempt to authentication");

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        try {
            UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
            List<String> roles = userDetailsCustom.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String deviceFingerprint = jwtService.generateDeviceFingerprint(request);
            log.debug("Generated device fingerprint: {}", 
                    deviceFingerprint != null ? deviceFingerprint.substring(0, 8) + "..." : "none");

            try {
                User user = userRepository.findByEmail(userDetailsCustom.getUsername());
                if (user != null) {
                    user.setDeviceFingerprint(deviceFingerprint);
                    user.setLastLogin(LocalDateTime.now());
                    userRepository.save(user);
                    log.debug("Saved device fingerprint for user: {}", user.getEmail());
                }
            } catch (Exception e) {
                log.error("Error saving device fingerprint: {}", e.getMessage(), e);
            }

            String accessToken = jwtService.generateToken(userDetailsCustom, deviceFingerprint);
            String refreshToken = jwtService.generateRefreshToken(userDetailsCustom, deviceFingerprint);

            Cookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);
            response.addCookie(refreshTokenCookie);

            Cookie accessTokenCookie = new Cookie("access_token", accessToken);
            accessTokenCookie.setHttpOnly(false);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(jwtConfig.getExpiration());
            response.addCookie(accessTokenCookie);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("accessToken", accessToken);
            responseData.put("tokenType", "Bearer");
            responseData.put("expiresIn", jwtConfig.getExpiration());
            responseData.put("roles", roles);
            responseData.put("userId", userDetailsCustom.getUserId());
            
            String json = HelperUtils.JSON_WRITER.writeValueAsString(responseData);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(json);
            log.info("End successful authentication with token generation");
        } catch (BaseException ex) {
            log.error("Error during token generation: {}", ex.getMessage());
            unsuccessfulAuthentication(request, response, new BadCredentialsException(ex.getMessage()));
        }
    }
    
    /**
     * Create a secure HttpOnly cookie for the refresh token
     */
    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        cookie.setMaxAge(jwtConfig.getRefreshExpiration());

        cookie.setAttribute("SameSite", "Strict");
        
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        responseDTO.setMessage(failed.getMessage());

        String json = HelperUtils.JSON_WRITER.writeValueAsString(responseDTO);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
    }
}
