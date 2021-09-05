package com.example.demo.Security;

import com.example.demo.Repositories.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailPrincipalService implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;
    public void UserPrincipalDetailsService(UserAccountRepository userAccountRepository){
        this.userAccountRepository = userAccountRepository;
    }
    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount=this.userAccountRepository.getById(username);
        log.info("////////////////"+userAccount.getUserName()+userAccount.getPassword()+userAccount.getRoles());
        UserPrincipal userPrincipal= new UserPrincipal(userAccount);
        System.out.println("****************"+userPrincipal.getUsername()+userPrincipal.getPassword()+userPrincipal.getAuthorities());
        return userPrincipal;
    }
}
