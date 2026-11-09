package synapse.employee.management.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.JournalEntry; // Capital J

// Interface ka naam Capital kiya aur standard 'Repository' suffix use kiya
public interface JournalRepository extends MongoRepository<JournalEntry, ObjectId> {
    // Agar future mein kisi custom query ki zaroorat pade (jaise employeeName se search karna),
    // to wo methods yahan define honge.
}
