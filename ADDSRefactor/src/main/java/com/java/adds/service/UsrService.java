package com.java.adds.service;

import com.java.adds.dao.UsrDao;
import com.java.adds.entity.Usr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security: Permission management Usr Service
 * @author QXL
 */
@Service
public class UsrService implements UserDetailsService {

    @Autowired
    private UsrDao usrDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usr usr = usrDao.getUsrByUsername(username);
        if (usr == null) {
            throw new UsernameNotFoundException("Username \"" + username + "\" is not exist. ");
        }

        usr.setPassword("");
        return usr;
    }
}
