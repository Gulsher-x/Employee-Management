package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.User; // Capital U
import synapse.employee.management.services.UserService; // Capital U

import java.util.Map;

@RestController
@RequestMapping("/public") // Added leading slash
public class PublicController { // Capital P

    @Autowired
    private UserService userService; // Capital U

    @GetMapping("/health") // Added leading slash
    public String health() {
        return "working...";
    }

    @PostMapping("/create-user") // Added leading slash
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            // Unnecessary full package path hata kar clean code kiya
            User savedUser = userService.saveNewUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "User created successfully",
                            "userName", savedUser.getUserName()
                    ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
