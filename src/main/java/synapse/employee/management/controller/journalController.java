package synapse.employee.management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.journalEntry;
import synapse.employee.management.entites.user;
import synapse.employee.management.services.journalService;
import synapse.employee.management.services.userService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("journalEntry")
public class journalController {
    @Autowired
    private journalService journalService;
    @Autowired
    private userService userService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        user user = userService.findbyuser(name);
        List<journalEntry> all = user.getJournalEntries();
        return all.isEmpty()?new ResponseEntity<>("user data is empty...",HttpStatus.NOT_FOUND):new ResponseEntity<>(all,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> saveAll(@RequestBody journalEntry journalEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
           journalService.saveAll(journalEntry, name);
           return new ResponseEntity<>(HttpStatus.CREATED);
       }
       catch (Exception e){
           return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }
    @GetMapping ("id/{id}")
    public ResponseEntity<?> findId(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        user user = userService.findbyuser(name);
        boolean b = user.getJournalEntries().stream().anyMatch(x -> x.getId().equals(id));
        if(b){

            return new ResponseEntity<>(journalService.findId(id),HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("id/{id}")
    public ResponseEntity<?> delete(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        journalService.delete(id,name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id ,@RequestBody journalEntry newjournalEntry ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        user user = userService.findbyuser(name);
        boolean contains = user.getJournalEntries().stream().anyMatch(x->x.getId().equals(id));
        if(!contains){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        journalEntry old = journalService.findId(id);
        if(old !=null){
            old.setRole(newjournalEntry.getRole() !=null && !newjournalEntry.getRole().equals("") ? newjournalEntry.getRole() : old.getRole());
            old.setEmployeeName(newjournalEntry.getEmployeeName() != null && !newjournalEntry.getEmployeeName().equals("")?  newjournalEntry.getEmployeeName() : old.getEmployeeName());
            old.setDepartment( newjournalEntry.getDepartment() != null && !newjournalEntry.getDepartment().equals("")?  newjournalEntry.getDepartment() : old.getDepartment()); ;
            old.setPhone( newjournalEntry.getPhone() != null && !newjournalEntry.getPhone().equals("")?  newjournalEntry.getPhone() : old.getPhone()) ;
            old.setEmail( newjournalEntry.getEmail() !=null && !newjournalEntry.getEmail().equals("")?  newjournalEntry.getEmail(): old.getEmail());
            journalService.saveAll(old);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
