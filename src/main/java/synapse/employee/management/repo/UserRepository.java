package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.User; // Capital U

// Interface ka naam Capital kiya aur Repository suffix use kiya
public interface UserRepository extends MongoRepository<User, ObjectId> {

     // Method returns aur parameters mein Capital User use kiya
     User findByUserName(String userName);

     void deleteByUserName(String userName);
}
