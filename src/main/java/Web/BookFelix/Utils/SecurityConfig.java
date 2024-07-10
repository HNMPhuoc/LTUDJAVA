package Web.BookFelix.Utils;

import Web.BookFelix.Constants.Provider;
import Web.BookFelix.Services.OAuthService;
import Web.BookFelix.Services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuthService oAuthService;
    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull
                                                   HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/",
                                "/oauth/**", "/register", "/error","/registerSubmit","/books")
                        .permitAll()
                        .requestMatchers("/books/edit/**",
                                "/books/add", "/books/delete","/Admincategory","/Adminbook")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers("/books", "/cart", "/cart/**")
                        .hasAnyAuthority("USER")
                        .requestMatchers("/category/**")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers("/books/add-to-cart")
                        .hasAnyAuthority("USER","GOOGLE")
                        .requestMatchers("/books", "/cart", "/cart/**")
                        .hasAnyAuthority("ADMIN", "USER", "GOOGLE")
                        .requestMatchers("/api/**")
                        .hasAnyAuthority("ADMIN", "USER", "GOOGLE")
                        .anyRequest().authenticated()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                ).formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                ).oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuthService))
                        .successHandler((request, response, authentication) -> {
                            var oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                            String email = oidcUser.getEmail();
                            String name = oidcUser.getFullName();
                            String provider = Provider.GOOGLE.toString();

                            // Lưu thông tin người dùng vào cơ sở dữ liệu
                            userService.saveOauthUser(email, name, provider);

                            // Tải lại thông tin người dùng cập nhật
                            UserDetails userDetails = userService.loadUserByEmail(email);

                            // Cập nhật đối tượng xác thực với thông tin và quyền mới nhất
                            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(auth);

                            // Chuyển hướng về trang chủ
                            response.sendRedirect("/");
                        })
                        .permitAll()
                ).rememberMe(rememberMe -> rememberMe
                        .key("hutech")
                        .rememberMeCookieName("hutech")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService())
                ).exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/403")
                ).sessionManagement(sessionManagement ->
                        sessionManagement
                                .maximumSessions(1)
                                .expiredUrl("/login")
                ).httpBasic(httpBasic -> httpBasic
                        .realmName("hutech")
                ).build();
    }

}
