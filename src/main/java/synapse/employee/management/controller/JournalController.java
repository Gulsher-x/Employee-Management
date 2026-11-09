package synapse.employee.management.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import synapse.employee.management.entites.JournalEntry; // Capital J
import synapse.employee.management.entites.User;         // Capital U
import synapse.employee.management.services.JournalService; // Capital J
import synapse.employee.management.services.UserService;    // Capital U

import java.util.List;

@RestController
@RequestMapping("/journalEntry")
public class JournalController { // Capital J

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    // Get all entries of logged-in user
    @GetMapping
    public ResponseEntity<?> getAll(Authentication authentication) {
        User currentUser = userService.findbyuser(authentication.getName());

        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<JournalEntry> all = currentUser.getJournalEntries();

        // Redundant check ko hata kar simple return kiya
        return ResponseEntity.ok(all);
    }

    // Create new employee entry
    @PostMapping
    public ResponseEntity<?> saveAll(
            @RequestBody JournalEntry entry,
            Authentication authentication) {
        try {
            journalService.saveAll(entry, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Employee added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Unable to add employee: " + e.getMessage());
        }
    }

    // Find entry by ID
    @GetMapping("/id/{id}")
    public ResponseEntity<?> findId(
            @PathVariable String id,
            Authentication authentication) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().body("Invalid ID");
        }

        ObjectId objectId = new ObjectId(id);

        if (!journalService.isOwner(objectId, authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view this entry");
            // 404 ki jagah 403 Forbidden dena zyada sahi hai agar data exist karta hai par user uska owner nahi hai
        }

        JournalEntry entry = journalService.findId(objectId);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entry);
    }

    // Delete entry
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id,
            Authentication authentication) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().body("Invalid ID");
        }

        boolean deleted = journalService.delete(new ObjectId(id), authentication.getName());
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // Update entry
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalEntry(
            @PathVariable String id,
            @RequestBody JournalEntry newEntry,
            Authentication authentication) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().body("Invalid ID");
        }

        ObjectId objectId = new ObjectId(id);

        if (!journalService.isOwner(objectId, authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to update this entry");
        }

        JournalEntry old = journalService.findId(objectId);
        if (old == null) {
            return ResponseEntity.notFound().build();
        }

        // Field checks
        if (newEntry.getRole() != null && !newEntry.getRole().isBlank()) {
            old.setRole(newEntry.getRole());
        }
        if (newEntry.getEmployeeName() != null && !newEntry.getEmployeeName().isBlank()) {
            old.setEmployeeName(newEntry.getEmployeeName());
        }
        if (newEntry.getDepartment() != null && !newEntry.getDepartment().isBlank()) {
            old.setDepartment(newEntry.getDepartment());
        }
        if (newEntry.getPhone() != null && !newEntry.getPhone().isBlank()) {
            old.setPhone(newEntry.getPhone());
        }
        if (newEntry.getEmail() != null && !newEntry.getEmail().isBlank()) {
            old.setEmail(newEntry.getEmail());
        }

        journalService.saveAll(old);
        return ResponseEntity.ok("Employee updated successfully");
    }
}
