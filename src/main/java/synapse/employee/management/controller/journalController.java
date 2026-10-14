package synapse.employee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import synapse.employee.management.entites.journalEnteries;
import synapse.employee.management.services.journalServices;

import java.util.List;

@RestController
@RequestMapping("journalenteries")
public class journalController {
    @Autowired
    private journalServices journalServices;

    @GetMapping
    public ResponseEntity<?> getall(){
        List<journalEnteries> all = journalServices.getAll();
        return all.isEmpty()?new ResponseEntity<>(HttpStatus.NOT_FOUND):new ResponseEntity<>(all,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> saveall(@RequestBody journalEnteries journalEnteries){
        try {
            journalServices.saveAll(journalEnteries);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("find/{id}")
    public ResponseEntity<?> findId(@PathVariable Long id){
        journalEnteries findid = journalServices.findid(id);
        return findid !=null ?new ResponseEntity<>(findid,HttpStatus.FOUND):new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        journalEnteries findid = journalServices.findid(id);
        if(findid !=null){
            journalServices.delete(id);
            return new ResponseEntity<>("Deletion Complete...",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Not Fount Id...",HttpStatus.NOT_FOUND);
    }

}
