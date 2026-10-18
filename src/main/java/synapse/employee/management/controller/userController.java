package synapse.employee.management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.users;
import synapse.employee.management.services.userService;

import java.util.List;

@RestController
@RequestMapping("users")
public class userController {
    @Autowired
    private userService userService;
    @GetMapping
    public ResponseEntity<?> getUser(){
        List<users> user = userService.getUser();
        return user.isEmpty()?new ResponseEntity<>(HttpStatus.NOT_FOUND):new ResponseEntity<>(user,HttpStatus.FOUND);
    }
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody users users){
            return new ResponseEntity<>(userService.saveUser(users),HttpStatus.CREATED);
    }
    @GetMapping("find/{username}")
    public ResponseEntity<?> findUser(@PathVariable String username){
        users user = userService.findbyuser(username);
        return user ==null ? new ResponseEntity<>("User Not found",HttpStatus.NOT_FOUND):new ResponseEntity<>(user,HttpStatus.OK);
    }

}
