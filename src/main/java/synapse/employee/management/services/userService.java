package synapse.employee.management.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import synapse.employee.management.entites.user;
import synapse.employee.management.repo.userRepo;

import java.util.List;

@Component
public class userService {
    @Autowired
    private userRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<user> getUser(){
        return userRepo.findAll();
    }
    public user saveNewUser(user user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
    public user saveUser(user user) {
        return userRepo.save(user);
    }
    public user findbyuser(String userName){
        return userRepo.findByUserName(userName);
    }
    public void deleteUser(String username){
        userRepo.deleteByUserName(username);
    }
}
