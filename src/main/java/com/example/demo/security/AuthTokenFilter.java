package com.example.demo.security;

import com.example.demo.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    private static final Logger logger= LoggerFactory.getLogger(AuthTokenFilter.class);
//JWTYİ al, eğer istek jwt ye sahipse onu doğrula kullancı adını ayrıştır.
    //kullanıcı adından kimlik doğrulama nesnesi oluşturmak için userdetails al
    // setauthontication yöntemini kullanarak securitycontextte geçerli kullanıcı ayrıntılarını ayarla.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     try {
         String jwt=parseJwt(request);
         if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
             String username=jwtUtils.getUserNameFromJwtToken(jwt);
             //kullanıcı adından kimlik d. nesnesi oluturmak için userdetails al
             UserDetails userDetails = userDetailsService.loadUserByUsername(username);

             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                     userDetails.getAuthorities());
             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
             //setauthentication ile securitycontext geçerli  kullanıcı ayrıntılarını ayarla

             SecurityContextHolder.getContext().setAuthentication(authentication);

         }
     }catch (Exception e) {
         logger.error("Cannot set user authentication: {}", e);
     }
        filterChain.doFilter(request, response);
    }
    //**
    // bearer 123hab2355
    //iki parçaya bölüp token kısmı ile ilgileniyoruz.

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        // headerAuth!=null && headerAuth.contains(bearer)
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }


}
