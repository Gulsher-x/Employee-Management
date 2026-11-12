package synapse.employee.management.controller;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import synapse.employee.management.entites.User;
import synapse.employee.management.repo.UserRepository;
import synapse.employee.management.repo.JournalRepository;
import synapse.employee.management.entites.JournalEntry;
import synapse.employee.management.services.JournalService;
import synapse.employee.management.services.UserService;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {
    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalRepository journalRepository;

    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addEmployee(
            @RequestBody JournalEntry entry) {

        entry.setId(null);
        entry.setDateTime(LocalDateTime.now());

        journalService.saveAll(entry);

        return ResponseEntity.status(201)
                .body(entry);
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public List<JournalEntry> getAllEmployees() {

        return journalRepository.findAll();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getDashboard() {

        // Total registered users
        long totalUsers = userRepository.count();

        // Total employees
        long totalEmployees = journalRepository.count();

        // Get all employees
        List<JournalEntry> employees = journalRepository.findAll();

        // Count unique departments
        long totalDepartments = employees.stream()
                .map(JournalEntry::getDepartment)
                .filter(department -> department != null && !department.isBlank())
                .distinct()
                .count();

        return Map.of(
                "totalUsers", totalUsers,
                "totalEmployees", totalEmployees,
                "totalDepartments", totalDepartments
        );
    }
    @PutMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEmployee(
            @PathVariable String id,
            @RequestBody JournalEntry newEntry) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().body("Invalid Employee ID");
        }

        ObjectId objectId = new ObjectId(id);

        JournalEntry old = journalService.findId(objectId);

        if (old == null) {
            return ResponseEntity.notFound().build();
        }

        if (newEntry.getEmployeeName() != null)
            old.setEmployeeName(newEntry.getEmployeeName());

        if (newEntry.getPhone() != null)
            old.setPhone(newEntry.getPhone());

        if (newEntry.getEmail() != null)
            old.setEmail(newEntry.getEmail());

        if (newEntry.getDepartment() != null)
            old.setDepartment(newEntry.getDepartment());

        if (newEntry.getRole() != null)
            old.setRole(newEntry.getRole());

        journalService.saveAll(old);

        return ResponseEntity.ok("Employee updated successfully by Admin");
    }
    @DeleteMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().body("Invalid Employee ID");
        }

        ObjectId objectId = new ObjectId(id);

        JournalEntry employee = journalService.findId(objectId);

        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        // Remove employee reference from its owner
        for (User user : userService.getUser()) {

            if (user.getJournalEntries() != null) {

                boolean removed = user.getJournalEntries()
                        .removeIf(entry -> objectId.equals(entry.getId()));

                if (removed) {
                    userService.saveUser(user);
                    break;
                }
            }
        }

        journalService.deleteByAdmin(objectId);

        return ResponseEntity.ok("Employee deleted successfully by Admin");
    }
}