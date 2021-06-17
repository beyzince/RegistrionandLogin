package com.example.demo.services;

import com.example.demo.security.AuthEntryPointJwt;
import com.example.demo.security.AuthTokenFilter;
import com.example.demo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//Websecurityconfig cors,csrf,session management kuralları yapılandırmak için httpsecurity yapılandırması sağlar.
//xml ile de yapılabilen konfigürasyon ayarlarının java kodunda yapılabilmesini sağlayan notasyon
@Configuration
//web projemiz üzerinde güvenlik katmanını aktif hale getirir.
//@ComponentScan(basePackages = {"com.example.demo"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {

        return new AuthTokenFilter();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    // private AuthenticationManager authenticationManager; nesnesini
    // inject etmeye çalıştığımızda hata oluşacaktır. Bunun
    // nedeni bu tipte bir bean in olmamasıdır. webSecurityConfig dosyasına
    // aşağıdaki eklemeyi yap.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // hangi method ve hangi pathlere izin verildiğine bakılır.
    // corsa izin verip csrfi disable et,
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated();
        //authenticationjwttokenfilterden istekler geçer, gelen istekler içerisinde token olup olmadıgına kontrol eder
        //tokenden ilgili bilgi alıp spring securitye aktarır.
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
// httpsecurity http hangi url pathinin herkese açık olacağını hangisinin hangi yetki sahibi kişilerin erişimine
    //açık olacağını belirle.
    //authenticationmanagerbuilder auth  methodu user tanımlamalarını yapıyoruz.
    //inmemoryauthentication ifadesi userların runtime da tutuldugu ve bir yerlerden çekilip alınmadıgı anlamına gelir.
    //withuser methodu ise username password() parolayı roles() userin rolu
}