package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.user;

public interface userRepo extends MongoRepository<user, ObjectId> {
     user findByUserName(String userName);
     void deleteByUserName(String userName);
}
