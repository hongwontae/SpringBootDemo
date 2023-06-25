package org.zerock.b52.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    // DB에 쿠키값을 넣기 위해서 datasource 설정
    private final DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        log.info("filte chain----------------------------------------");

        // /login 경로 로그인 페이지 띄우기
        // 람다식 설정이므로 config란 변수로선언
        http.formLogin(config -> {
            // login 경로 지정 오타 주의
            config.loginPage("/member/signin");
        });
        // rememberMe => 자동로그인
        // 쿠키값이 들어감
        // tokenValiditySeconds -> 쿠기의 유효시간 설정 초단위임
        // 지금 새창으로 열때는 아예 작동이 안된다 왜냐면
        // 지금 저장할방법을 안정해놨기때문
        // network response 영역에 set rememberme 해줌
        http.rememberMe(config -> {
            config.tokenRepository(persistentTokenRepository());
            config.tokenValiditySeconds(60 * 60 * 24 * 7);
        });

        // default()에는 _csrf 이름의 input 태그가 hidden으로 숨어있다.
        // disable()을쓰면 csrf 태그가 사라진다.
        http.csrf(config -> {
            config.disable();
        });

        return http.build();

    }
}
