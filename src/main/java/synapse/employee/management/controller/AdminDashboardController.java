
package synapse.employee.management.controller;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import synapse.employee.management.entites.Employee;
import synapse.employee.management.repo.EmployeeRepository;
import synapse.employee.management.repo.UserRepository;
import synapse.employee.management.services.EmployeeService;

import java.util.HashMap;
import org.springframework.security.core.Authentication;
import synapse.employee.management.entites.User;
import synapse.employee.management.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminDashboardController(
            EmployeeService employeeService,
            EmployeeRepository employeeRepository,
            UserService userService,
            UserRepository userRepository) {

        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    // Admin Change Password
    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (currentPassword == null || newPassword == null ||
                currentPassword.isBlank() || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Password fields cannot be empty");
        }

        if (newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body("New password must be at least 8 characters");
        }

        boolean updated = userService.changePassword(
                authentication.getName(),
                currentPassword,
                newPassword
        );

        if (!updated) {
            return ResponseEntity.badRequest()
                    .body("Current password is incorrect");
        }

        return ResponseEntity.ok("Password changed successfully");
    }

    // Add Employee
    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Employee> addEmployee(
            @RequestBody Employee employee) {

        employee.setId(null);

        Employee savedEmployee =
                employeeService.saveEmployee(employee);

        return ResponseEntity.status(201).body(savedEmployee);
    }

    // Get All Employees
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Employee> getAllEmployees() {

        return employeeService.getAllEmployees();
    }

    // Get Employee By ID
    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEmployeeById(
            @PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest()
                    .body("Invalid Employee ID");
        }

        return employeeService
                .getEmployeeById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin Dashboard Statistics
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getDashboard() {

        long totalUsers = userRepository.count();

        long totalEmployees = employeeRepository.count();

        List<Employee> employees =
                employeeRepository.findAll();

        long totalDepartments = employees.stream()
                .map(Employee::getDepartment)
                .filter(department ->
                        department != null && !department.isBlank())
                .distinct()
                .count();

        return Map.of(
                "totalUsers", totalUsers,
                "totalEmployees", totalEmployees,
                "totalDepartments", totalDepartments
        );
    }

    // Update Employee
    @PutMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEmployee(
            @PathVariable String id,
            @RequestBody Employee employee) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest()
                    .body("Invalid Employee ID");
        }

        Employee updated = employeeService.updateEmployee(
                new ObjectId(id), employee);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }
    // Admin Profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminProfile(Authentication authentication) {

        User user = userRepository.findByUserName(authentication.getName());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> profile = new HashMap<>();

        profile.put("userName", user.getUserName());
        profile.put("role", user.getRole());

        if (user.getEmployeeId() != null) {
            employeeRepository.findById(user.getEmployeeId())
                    .ifPresent(employee -> profile.put("employee", employee));
        }

        return ResponseEntity.ok(profile);
    }

    // Delete Employee
    @DeleteMapping("/employees/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest()
                    .body("Invalid Employee ID");
        }

        boolean deleted = employeeService.deleteEmployee(
                new ObjectId(id));

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Employee deleted successfully by Admin");
    }
}
