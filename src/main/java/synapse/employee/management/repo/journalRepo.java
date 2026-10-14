package synapse.employee.management.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import synapse.employee.management.entites.journalEnteries;

public interface journalRepo extends MongoRepository<journalEnteries,Long> {
}
