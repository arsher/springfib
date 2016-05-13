package net.serfozo.fibonacci.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.List;

public class CustomJdbcDaoImpl extends JdbcUserDetailsManager {
    private static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password,enabled,maxnumber "
            + "from users " + "where username = ?";
    private final String usersByUsernameQuery = DEF_USERS_BY_USERNAME_QUERY;

    @Override
    protected UserDetails createUserDetails(final String username, final UserDetails userFromUserQuery, final List<GrantedAuthority> combinedAuthorities) {
        final CustomUser secUser = (CustomUser)userFromUserQuery;
        final UserDetails userDetails = super.createUserDetails(username, userFromUserQuery, combinedAuthorities);
        return new CustomUser(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.isEnabled(),
                userDetails.isAccountNonExpired(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isAccountNonLocked(),
                userDetails.getAuthorities(),
                secUser.getMaxnumber());
    }

    @Override
    protected List<UserDetails> loadUsersByUsername(final String username) {
        return getJdbcTemplate().query(usersByUsernameQuery, new String[] { username },
                (rs, rowNum) -> {
                    final String username1 = rs.getString(1);
                    final String password = rs.getString(2);
                    final boolean enabled = rs.getBoolean(3);
                    return new CustomUser(username1, password, enabled, true, true, true,
                            AuthorityUtils.NO_AUTHORITIES, rs.getInt(4));
                });
    }
}
