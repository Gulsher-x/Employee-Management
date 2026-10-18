package synapse.employee.management.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import synapse.employee.management.entites.journalEntries;
import synapse.employee.management.entites.users;
import synapse.employee.management.repo.journalRepo;

import java.util.List;

@Component
public class journalServices {
    @Autowired
    private journalRepo journalRepo;
    @Autowired
    private  userService userService;
    public void saveAll(journalEntries journalEnteries, String username){
        users user= userService.findbyuser(username);
        journalEntries save = journalRepo.save(journalEnteries);
        user.getJournalEnteries().add(save);
        userService.saveUser(user);
    }
    public void saveall(journalEntries journalEntries){
        journalRepo.save(journalEntries);
    }
    public List<journalEntries> getUserjounralentry(){
        return journalRepo.findAll();
    }
    public journalEntries findid(ObjectId id){
        return journalRepo.findById(id).orElse(null);
    }
    public void delete(ObjectId id, String username){
        users user = userService.findbyuser(username);
        user.getJournalEnteries().removeIf(x-> x.getId().equals(id));
        userService.saveUser(user);
        journalRepo.deleteById(id);
    }
}
