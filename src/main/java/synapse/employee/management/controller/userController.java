package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.user;
import synapse.employee.management.services.userService;

import java.util.List;

@RestController
@RequestMapping("u")
public class userController {
    @Autowired
    private userService userService;
    @GetMapping
    public ResponseEntity<?> getUser(){
        List<user> user = userService.getUser();
        return user.isEmpty()?new ResponseEntity<>(HttpStatus.NOT_FOUND):new ResponseEntity<>(user,HttpStatus.OK);
    }
   @PutMapping()
    public ResponseEntity<?> update(@RequestBody user updateUser){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userName = authentication.getName();
       user userInDb = userService.findbyuser(userName);
       if(userInDb !=null){
           userInDb.setUserName(updateUser.getUserName());
           userInDb.setPassword(updateUser.getPassword());
           userService.saveNewUser(userInDb);
           return new ResponseEntity<>("user Update...",HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);

   }
   @DeleteMapping
    public ResponseEntity<?> delete(){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String name = authentication.getName();
       userService.deleteUser(name);
       return new ResponseEntity<>("User Deleted....",HttpStatus.NO_CONTENT);

   }


}
