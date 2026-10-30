package synapse.employee.management.entites;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journalEntries")
@Data
@NoArgsConstructor
public class journalEntry {
    @Id
    private ObjectId id;
    private String employeeName ;
    private String phone;
    private String email;
    private String department;
    private String role;
    private LocalDateTime dateTime;

}
