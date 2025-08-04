package api.v2.travel_social_network_server.components;

import api.v2.travel_social_network_server.entities.User;
import api.v2.travel_social_network_server.services.user.UserService;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value("${api.base-url}")
    private String apiBaseURL;

    private final JwtGenerator jwtGenerator;
    private final UserService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (nonAuthRequest(request)) {
            filterChain.doFilter(request, response);
            log.info("Non-auth request");
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization header: {}", authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is missing or invalid.");
            return;
        }

        final String token = authorizationHeader.substring(7);

        try {
            if (jwtGenerator.isValidToken(token)) {
                final String email = jwtGenerator.extractEmail(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = (User) userDetailsService.getUserByEmail(email);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );

                    authenticationToken.setDetails(user);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token.");
                return;
            }
        } catch (SignatureVerificationException e) {
            log.error("Signature verification failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token signature.");
            return;
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication error: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean nonAuthRequest(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        final List<Pair<String, String>> nonAuthEndpoints = List.of(
                // Swagger endpoints
                Pair.of("/swagger-ui", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET"),

                // Authentication endpoints
                Pair.of(String.format("%s/auth/login", apiBaseURL), "POST"),
                Pair.of(String.format("%s/auth/register", apiBaseURL), "POST"),
                Pair.of(String.format("%s/auth/admin/login", apiBaseURL), "POST"),
                Pair.of(String.format("%s/auth/forgot-password", apiBaseURL), "POST"),
                Pair.of(String.format("%s/auth/reset-password", apiBaseURL), "POST")


//                // Chat endpoints
//                Pair.of("/ws", "GET"),
//                Pair.of("/ws/**", "GET"),
//                Pair.of("/socket.io/**", "GET"),
//                Pair.of("/user", "GET"),
//                Pair.of("/user/**", "GET"),
//                Pair.of("/info", "GET"),
//                Pair.of("/info/**", "GET"),
//                Pair.of("/socket.io", "GET"),
//
//                // Get user endpoint
//                Pair.of(String.format("%s/user", apiBaseURL), "GET"),
//
//                // OAuth and login endpoints
//                Pair.of("/login", "GET"),
//                Pair.of("/login/oauth2/code/**", "GET"),
//                Pair.of("/login/oauth2/code/**", "POST"),
//                Pair.of(String.format("%s/oauth/**", apiBaseURL), "GET"),
//                Pair.of(String.format("%s/oauth/**", apiBaseURL), "POST"),
//
//                // Post endpoints
//                Pair.of(String.format("%s/posts", apiBaseURL), "GET"),
//                Pair.of(String.format("%s/posts/{postId:[0-9]+}", apiBaseURL), "GET"),
//                Pair.of(String.format("%s/users/doctors", apiBaseURL), "POST"),
//                Pair.of(String.format("%s/users/doctors/**", apiBaseURL), "GET"),
//                Pair.of(String.format("%s/comments/**", apiBaseURL), "GET")

        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        return nonAuthEndpoints.stream()
                .anyMatch(pair -> pathMatcher.match(pair.getFirst(), requestPath)
                        && requestMethod.equalsIgnoreCase(pair.getSecond()));
    }
}