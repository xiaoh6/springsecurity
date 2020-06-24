package com.example.springsecuritydemo2;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //(1)从数据库获取用户
        User user = userRepository.findByUserName(username);
        if (user==null)//用户不存在
            throw new RuntimeException("用户"+username+"不存在!");
        //(2)将数据库中的roles解析为UserDetail的权限集
        String roles = user.getRoles();
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        user.setAuthentications(grantedAuthorities);
        return user;
    }

    List<GrantedAuthority> getGrantedAuthorities(String roles){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] split = StringUtils.split(roles, ";");
        for (int i = 0;i<split.length;i++){
            if (!StringUtils.isEmpty(split[i])){
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(split[i]);
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return grantedAuthorities;
    }
}

