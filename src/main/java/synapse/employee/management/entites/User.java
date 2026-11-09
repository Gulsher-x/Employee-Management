package synapse.employee.management.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor  // MongoDB ke internal instantiation ke liye zaroori hai
@AllArgsConstructor // Saari fields ke saath constructor banane ke liye
public class User { // Class name Capitalized kiya (User)

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String userName;

    @NonNull
    private String password;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>(); // Capital J kiya aur private lagaya

    private List<String> role; // Private keyword add kiya standard encapsulation ke liye
}
