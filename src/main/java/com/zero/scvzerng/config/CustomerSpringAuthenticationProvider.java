package com.zero.scvzerng.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by scvzerng on 2017/7/7.
 */
public class CustomerSpringAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
