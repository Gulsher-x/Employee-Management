package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.dto.RegistrationRequest;
import synapse.employee.management.entites.User;
import synapse.employee.management.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public String health() {
        return "working...";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> saveUser(
            @RequestBody RegistrationRequest request) {

        try {
            User savedUser = userService.registerUser(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "User and Employee created successfully",
                            "userName", savedUser.getUserName(),
                            "employeeId", savedUser.getEmployeeId()
                    ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
