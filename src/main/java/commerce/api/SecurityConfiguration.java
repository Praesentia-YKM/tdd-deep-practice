package commerce.api;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
public class SecurityConfiguration {

    @Bean
    Pbkdf2PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        return http
                // csrf 보호 기능을 비활성화합니다.
                .csrf(AbstractHttpConfigurer::disable)
                // HTTP 요청에 대한 인가 규칙을 설정합니다.
                .authorizeHttpRequests(requests ->
                        requests
                                // "/seller/signUp" 경로에 대한 요청은 모두에게 허용합니다.
                                .requestMatchers("/seller/signUp").permitAll()
                )
                .build();
    }
}
