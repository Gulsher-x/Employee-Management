package synapse.employee.management.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import synapse.employee.management.entites.journalEntry;
import synapse.employee.management.entites.user;
import synapse.employee.management.repo.journalRepo;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class journalService {
    @Autowired
    private journalRepo journalRepo;
    @Autowired
    private  userService userService;
    @Transactional
    public void saveAll(journalEntry journalEntry, String username){
        try{
            user user= userService.findbyuser(username);
            journalEntry.setDateTime(LocalDateTime.now());
            journalEntry save = journalRepo.save(journalEntry);
            user.getJournalEntries().add(save);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("error occur while adding data...",e);
        }

    }
    public void saveAll(journalEntry journalEntry){
        journalRepo.save(journalEntry);
    }
    public List<journalEntry> getUserJournalEntry(){
        return journalRepo.findAll();
    }
    public journalEntry findId(ObjectId id){
        return journalRepo.findById(id).orElse(null);
    }
    @Transactional
    public void delete(ObjectId id, String username){
        try {
            user user = userService.findbyuser(username);
            boolean b = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(b){
                userService.saveUser(user);
                journalRepo.deleteById(id);
            }

        } catch (Exception e) {
            throw new RuntimeException("error occur while deleting...",e);
        }

    }
}
