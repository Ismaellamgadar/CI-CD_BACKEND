package com.example.demo.core.security;

import com.example.demo.core.context.LocalContext;
import com.example.demo.core.logger.AutomaticLoggerProcessor;
import com.example.demo.core.logger.banneduser.BannedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class UserCooldownAuthorizationFilter extends GenericFilterBean {
    private final LocalContext localContext;

    public UserCooldownAuthorizationFilter(LocalContext localContext) {
        this.localContext = localContext;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Check if the current user is banned
        if (AutomaticLoggerProcessor
                .getBannedUsers()
                .stream()
                .filter(bannedUser -> !bannedUser.isBanned())
                .map(BannedUser::getUserId)
                .toList()
                .contains(localContext
                        .getCurrentUser()
                        .getId())) {

            // Clear the security context if the user is banned
            SecurityContextHolder.clearContext();
        } else {

            // Otherwise, pass the request and response to the next filter in the chain
            chain.doFilter(request,response);
        }
    }

}
