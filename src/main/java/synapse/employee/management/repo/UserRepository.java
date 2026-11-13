package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.User; // Capital U


public interface UserRepository extends MongoRepository<User, ObjectId> {


     User findByUserName(String userName);

     void deleteByUserName(String userName);
     void deleteByEmployeeId(ObjectId employeeId);

}
