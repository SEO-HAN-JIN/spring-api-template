package com.framework.template.global.security.context;

import com.framework.template.global.security.dto.AuthenticationDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {

    private final AuthenticationDto authenticationDto;

    public CustomUser(AuthenticationDto authenticationDto, Collection<? extends GrantedAuthority> authorities) {
        super(authenticationDto.getLoginId(), authenticationDto.getPassword(), authorities);
        this.authenticationDto = authenticationDto;
    }
}
