package com.morosys.userscrud.configuration

import com.morosys.userscrud.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class Security {
    @Autowired
    lateinit var userRepository: UserRepository

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/*").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { it.disable() } // Disable CSRF for development

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }


    /* fun configure(auth: AuthenticationManagerBuilder) {
         auth.userDetailsService(userName ->
         userRepository.findByUserName(userName))
     }*/


}
