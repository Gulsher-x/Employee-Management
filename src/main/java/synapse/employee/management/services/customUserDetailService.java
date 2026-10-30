package synapse.employee.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import synapse.employee.management.entites.user;
import synapse.employee.management.repo.userRepo;

@Service
public class customUserDetailService implements UserDetailsService {
    @Autowired
    private userRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user = userRepo.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("user not found ..");
        }

        return User.withUsername(user.getUserName())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }


}
