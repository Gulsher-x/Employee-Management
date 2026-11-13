
package synapse.employee.management.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import synapse.employee.management.entites.Employee;
import synapse.employee.management.repo.EmployeeRepository;
import synapse.employee.management.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Add employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Optional<Employee> getEmployeeById(ObjectId id) {
        return employeeRepository.findById(id);
    }

    // Update employee
    public Employee updateEmployee(ObjectId id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(updatedEmployee.getName());
                    employee.setEmail(updatedEmployee.getEmail());
                    employee.setPhone(updatedEmployee.getPhone());
                    employee.setDepartment(updatedEmployee.getDepartment());
                    employee.setRole(updatedEmployee.getRole());

                    return employeeRepository.save(employee);
                })
                .orElse(null);
    }

    // Delete employee
    public boolean deleteEmployee(ObjectId id) {

        if (!employeeRepository.existsById(id)) {
            return false;
        }

        // Delete linked User account
        userRepository.deleteByEmployeeId(id);

        // Delete Employee
        employeeRepository.deleteById(id);

        return true;
    }
}
