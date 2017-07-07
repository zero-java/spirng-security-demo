package com.zero.scvzerng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Created by scvzerng on 2017/7/3.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    JdbcTemplate template;
    class DataBaseUserDetailsService implements UserDetailsManager{
        @Override
        public void createUser(UserDetails user) {
            template.execute("INSERT INTO users VALUES (?,?,false)", new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(0,user.getUsername());
                    ps.setString(1,user.getPassword());
                    return  ps.execute();
                }
            });
        }

        @Override
        public void updateUser(UserDetails user) {
            template.execute("UPDATE users SET username=?,password=?,enabled=?", new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(0,user.getUsername());
                    ps.setString(1,user.getPassword());
                    ps.setBoolean(2,true);
                    return  ps.execute();
                }
            });
        }

        @Override
        public void deleteUser(String username) {
            template.execute("delete FROM users where username=?", (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(0,username);
                return  ps.execute();
            });
        }

        @Override
        public void changePassword(String oldPassword, String newPassword) {
            template.execute("UPDATE users SET password=?", (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(0,newPassword);

                return  ps.execute();
            });
        }

        @Override
        public boolean userExists(String username) {
            return template.execute("SELECT username from users where username=?", (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(0,username);

                return  !ps.executeQuery().wasNull();
            });
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return template.execute("select * from users where username=?", new PreparedStatementCallback<UserDetails>() {
                @Override
                public UserDetails doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                   ps.setString(0,username);
                   ResultSet resultSet = ps.executeQuery();
                   String username = resultSet.getString(0);
                    String password = resultSet.getString(1);
                    return User.withUsername(username).password(password).build();
                }
            });
        }
    }

    @Override
    protected void configure(HttpSecurity  auth) throws Exception {
        auth    .userDetailsService(new DataBaseUserDetailsService())
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }


}
