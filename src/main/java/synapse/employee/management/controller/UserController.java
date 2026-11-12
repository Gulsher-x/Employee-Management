package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import synapse.employee.management.entites.User; // Capital U
import synapse.employee.management.repo.EmployeeRepository;
import synapse.employee.management.services.UserService; // Capital U

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeRepository employeeRepository;

    // Get logged-in user's profile
    @GetMapping
    public ResponseEntity<?> getUser(Authentication authentication) {

        User currentUser = userService.findbyuser(authentication.getName());

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        if (currentUser.getEmployeeId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee profile not linked");
        }

        return employeeRepository.findById(currentUser.getEmployeeId())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update logged-in user's password
    // Change logged-in user's password
    @PutMapping("/change-password")
    public ResponseEntity<?> update(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (currentPassword == null || newPassword == null ||
                currentPassword.isBlank() || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Both passwords are required");
        }

        if (newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body("New password must be at least 8 characters");
        }

        boolean updated = userService.changePassword(
                authentication.getName(),
                currentPassword,
                newPassword
        );

        if (!updated) {
            return ResponseEntity.badRequest()
                    .body("Current password is incorrect");
        }

        return ResponseEntity.ok("Password changed successfully");
    }

    // Delete logged-in user
    @DeleteMapping
    public ResponseEntity<?> delete(Authentication authentication) {
        User currentUser = userService.findbyuser(authentication.getName());

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
