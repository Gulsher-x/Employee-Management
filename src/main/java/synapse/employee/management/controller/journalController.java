package synapse.employee.management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.journalEntries;
import synapse.employee.management.entites.users;
import synapse.employee.management.services.journalServices;
import synapse.employee.management.services.userService;

import java.util.List;

@RestController
@RequestMapping("journalenteries")
public class journalController {
    @Autowired
    private journalServices journalServices;
    @Autowired
    private userService userService;

    @GetMapping("{username}")
    public ResponseEntity<?> getall(@PathVariable String username){
        users user = userService.findbyuser(username);
        List<journalEntries> all = user.getJournalEnteries();
        return all.isEmpty()?new ResponseEntity<>("user data is empty...",HttpStatus.NOT_FOUND):new ResponseEntity<>(all,HttpStatus.OK);
    }
    @PostMapping("{username}")
    public ResponseEntity<?> saveall(@RequestBody journalEntries journalEntries, @PathVariable String username){
       try {
           journalServices.saveAll(journalEntries, username);
           return new ResponseEntity<>(HttpStatus.CREATED);
       }
       catch (Exception e){
           return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }
//    @GetMapping("find/{id}")
//    public ResponseEntity<?> findId(@PathVariable ObjectId id){
//        journalEntries findid = journalServices.findid(id);
//        return findid !=null ?new ResponseEntity<>(findid,HttpStatus.FOUND):new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//    }
    @DeleteMapping("{username}/{id}")
    public ResponseEntity<?> delete(@PathVariable ObjectId id, @PathVariable String username){
        journalServices.delete(id,username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("{username}/{id}")
    public ResponseEntity<?> updatejournalentry(@RequestBody journalEntries newjournalEntry, @PathVariable String username , @PathVariable ObjectId id ){
        journalEntries old = journalServices.findid(id);
        if(old !=null){
            old.setPhone( newjournalEntry.getPhone() != null && !newjournalEntry.getPhone().equals("")?  newjournalEntry.getPhone() : old.getPhone()) ;
            old.setEmail( newjournalEntry.getEmail() !=null && !newjournalEntry.getEmail().equals("")?  newjournalEntry.getEmail(): old.getEmail());
            journalServices.saveall(old);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
