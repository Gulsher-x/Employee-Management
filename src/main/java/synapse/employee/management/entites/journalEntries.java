package synapse.employee.management.entites;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class journalEntries {
    @Id
    private ObjectId id;
    private String phone;
    private String email;

}
