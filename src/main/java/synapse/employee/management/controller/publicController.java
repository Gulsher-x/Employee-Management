package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.user;
import synapse.employee.management.services.userService;

@RestController
@RequestMapping("public")
public class publicController {
    @Autowired
    private userService userService;
    @GetMapping("health")
    public String health(){
        return "working...";
    }
    @PostMapping("create-user")
    public ResponseEntity<?> saveUser(@RequestBody user user){
        return new ResponseEntity<>(userService.saveNewUser(user), HttpStatus.CREATED);
    }
}
