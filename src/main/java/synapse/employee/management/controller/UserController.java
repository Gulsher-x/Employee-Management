package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import synapse.employee.management.entites.User; // Capital U
import synapse.employee.management.services.UserService; // Capital U

import java.util.Map;

@RestController
@RequestMapping("/u")
public class UserController { // Capital U

    @Autowired
    private UserService userService; // Capital U

    // Get logged-in user's profile
    @GetMapping("/me")
    public ResponseEntity<?> getUser(Authentication authentication) {
        User currentUser = userService.findbyuser(authentication.getName());

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok(
                Map.of("userName", currentUser.getUserName())
        );
    }

    // Update logged-in user's password
    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String newPassword = request.get("password");

        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Password is required");
        }

        User currentUser = userService.findbyuser(authentication.getName());

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        userService.updatePassword(currentUser, newPassword);
        return ResponseEntity.ok("Password updated successfully");
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
