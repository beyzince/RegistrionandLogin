package com.example.demo.services;

import com.example.demo.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
//kullancıı giriş yaptığında username role gibi bilgileri yönetebilmek
// ve elde edebilmek için userdetails paketini ekle
//db kullanıcı bilgileri kullanıcı formu tarafından gelecek olan bilgileri karşılaştır ve kayıt sayfasından
//gelecek bilgilerin ise kayıt edilmesini sağlar.

public class UserDetailsImpl implements UserDetails {
    //object serialization kullanılarak nesneler byte dizisi haline getirebilir nesneler program kapatıldıgında
    //dahi kalıcı olmuş olur.versiyon kontrolünü sağlamak için kullanılır.
    private static final long serialVersionUID=1L;
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    //getauthorities metodu ile kullanıcıya
        //ait rollerin veritabanından alınmasını sağlayarak springe verilmesini sağlar.

    public static UserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId( ),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public String getPassword() {
        return password;
        //return User.getPassword();
    }

    @Override
    public String getUsername() {
        return username;
        //return user.getEmail();

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
