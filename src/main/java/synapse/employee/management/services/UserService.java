
package synapse.employee.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import synapse.employee.management.entites.User;
import synapse.employee.management.entites.Employee;
import synapse.employee.management.repo.UserRepository;
import synapse.employee.management.repo.EmployeeRepository;
import synapse.employee.management.dto.RegistrationRequest;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUser() {
        return userRepository.findAll();
    }

    // Register User and Employee together
    public User registerUser(RegistrationRequest request) {

        if (userRepository.findByUserName(request.getUserName()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Create Employee
        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setRole(request.getDesignation());

        // Save Employee first
        Employee savedEmployee = employeeRepository.save(employee);

        // Create User
        User user = new User();

        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(List.of("USER"));

        // Link Employee with User
        user.setEmployeeId(savedEmployee.getId());

        return userRepository.save(user);
    }

    // Existing user update
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findbyuser(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUserName(username);
    }

    // Password update
    // Change Password
    public boolean changePassword(
            String username,
            String currentPassword,
            String newPassword) {

        User user = userRepository.findByUserName(username);

        if (user == null) {
            return false;
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }
}