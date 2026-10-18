package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.journalEntries;

public interface journalRepo extends MongoRepository<journalEntries, ObjectId> {
}
