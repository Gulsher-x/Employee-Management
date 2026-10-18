package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.users;

public interface userRepo extends MongoRepository<users, ObjectId> {
     users findByUserName(String userName);
}
