package synapse.employee.management.entites;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class journalEnteries {
    @Id
    private Long id;
    private String name;
    private String email;

}
