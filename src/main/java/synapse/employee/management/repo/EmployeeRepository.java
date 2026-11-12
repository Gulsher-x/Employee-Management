
package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, ObjectId> {

}
