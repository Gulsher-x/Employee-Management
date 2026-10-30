package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.journalEntry;

public interface journalRepo extends MongoRepository<journalEntry, ObjectId> {
}
