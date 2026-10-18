package synapse.employee.management.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import synapse.employee.management.entites.users;
import synapse.employee.management.repo.userRepo;

import java.util.List;

@Component
public class userService {
    @Autowired
    private userRepo userRepo;
    public List<users> getUser(){
        return userRepo.findAll();
    }
    public users saveUser(users users){
        return userRepo.save(users);
    }
    public  users findbyuser(String userName){
        return userRepo.findByUserName(userName);
    }
    public void deleteUser(ObjectId id){
        userRepo.deleteById(id);
    }
}
