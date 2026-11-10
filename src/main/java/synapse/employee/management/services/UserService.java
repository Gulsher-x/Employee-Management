
package synapse.employee.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import synapse.employee.management.entites.User;

import synapse.employee.management.repo.UserRepository;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUser() {
        return UserRepository.findAll();
    }

    // New user registration
    public User saveNewUser(User user) {

        if (UserRepository.findByUserName(user.getUserName()) != null) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role for every registered user
        user.setRole(List.of("USER"));

        return UserRepository.save(user);
    }

    // Existing user update
    public User saveUser(User user) {
        return UserRepository.save(user);
    }

    public User findbyuser(String userName) {
        return UserRepository.findByUserName(userName);
    }

    public void deleteUser(String username) {
        UserRepository.deleteByUserName(username);
    }

    // Password update with encoding
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        UserRepository.save(user);
    }
}
