package cn.bwqsh.neu.homework.web.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
    }
}


@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Autowired
    private val userDetailsService: UserDetailsService? = null


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        //generate the default security filter chain
        http
            .csrf()
            .disable()
            .authorizeHttpRequests { authorize ->
                //permit all the static resources,but other requests need to be authenticated
                authorize.requestMatchers("/static/**","/js/**","/css/**","/static/img/**","/webjars/**").permitAll()
                    .requestMatchers(HttpMethod.POST).permitAll()
                    .requestMatchers("/img/**").permitAll()
                    .anyRequest().authenticated()
            }.formLogin {
                it.loginPage("/login")
                    .defaultSuccessUrl("/",true)
                    .failureUrl("/login?error")
                    .permitAll()
            }.logout {
                it.logoutUrl("/api/logout").permitAll()
            }
        return http.build()
    }


    //use to config authentication manager and use userService and passwordEncoder
    @Primary
    @Bean
    fun config(auth: AuthenticationManagerBuilder): AuthenticationManagerBuilder? {
        println("authenticationManagerBuilder")
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
        return auth
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}

