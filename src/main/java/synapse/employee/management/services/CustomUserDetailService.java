package synapse.employee.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import synapse.employee.management.entites.User; // Hamari custom User entity (Capital U)
import synapse.employee.management.repo.UserRepository; // Updated Repository name

@Service
public class CustomUserDetailService implements UserDetailsService { // Capital C किया

    @Autowired
    private UserRepository userRepository; // Updated to UserRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Hamari custom User class mein data fetch kiya
        User myUser = userRepository.findByUserName(username);

        if (myUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Spring Security ka internal User builder use kiya (Full package path ke sath conflict avoid karne ke liye)
        return org.springframework.security.core.userdetails.User.withUsername(myUser.getUserName())
                .password(myUser.getPassword())
                .roles(myUser.getRole() != null ? myUser.getRole().toArray(new String[0]) : new String[]{"USER"})
                // Agar user ke pass roles hain to wo use honge, nahi to default "USER" role mil jayega
                .build();
    }
}
