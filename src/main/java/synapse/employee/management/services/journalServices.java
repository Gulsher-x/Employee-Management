package synapse.employee.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import synapse.employee.management.entites.journalEnteries;
import synapse.employee.management.repo.journalRepo;

import java.util.List;
import java.util.Optional;

@Component
public class journalServices {
    @Autowired
    private journalRepo journalRepo;

    public String saveAll(journalEnteries journalEnteries){
        journalRepo.save(journalEnteries);
        return "saved...";
    }

    public List<journalEnteries> getAll(){
        return journalRepo.findAll();
    }
    public journalEnteries findid(Long id){
        return journalRepo.findById(id).orElse(null);
    }
    public void delete(Long id){
         journalRepo.deleteById(id);
    }
}
