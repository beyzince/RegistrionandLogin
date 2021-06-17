package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
//role bazlı user ile ilgili repository aracılıgıyla kayıt işlemi yapabilmek için service katmanı oluşturmamız gerkeir
//UserdetailsService interface ile servis katmanı oluştur.
// userdetailsservice kimlik doğrulama ve doğrulama için kullanabileceği user details object döndürür.
//kullanıcı giriş yapmak istedinde yetkilendirme işleminin gerçekleşeceği class
// yetkilendirme ve kullanıcı yonetim işlemlerini spring securitye veriyoruz.

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    // kullanıcı sorgulayıcı metodunda findbyusername ile kullanıcıyı bulur
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username ile login
        //email ile login içinUser user=repository findByEmail(email)
        User user=userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found with username"+username));
        //if(user==null)
        //throw new usernamenotfoundException("user not found")
        // nesne alıyoruz statik build yöntemi kullanarak userdetails nesnesi oluşturuyoruz.
        return UserDetailsImpl.build(user);

    }
}


