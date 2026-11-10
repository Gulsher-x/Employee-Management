
package synapse.employee.management.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import synapse.employee.management.entites.JournalEntry;
import synapse.employee.management.entites.User;
import synapse.employee.management.repo.JournalRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalRepository JournalRepository;

    @Autowired
    private UserService UserService;

    @Transactional
    public void saveAll(JournalEntry entry, String username) {

        User currentUser = UserService.findbyuser(username);

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        entry.setId(null);
        entry.setDateTime(LocalDateTime.now());

        JournalEntry saved = JournalRepository.save(entry);

        currentUser.getJournalEntries().add(saved);

        UserService.saveUser(currentUser);
    }

    public void saveAll(JournalEntry entry) {
        JournalRepository.save(entry);
    }

    public List<JournalEntry> getUserJournalEntry() {
        return JournalRepository.findAll();
    }

    public JournalEntry findId(ObjectId id) {
        return JournalRepository.findById(id).orElse(null);
    }

    public boolean isOwner(ObjectId id, String username) {

        User currentUser = UserService.findbyuser(username);

        if (currentUser == null ||
                currentUser.getJournalEntries() == null) {
            return false;
        }

        return currentUser.getJournalEntries()
                .stream()
                .anyMatch(entry -> id.equals(entry.getId()));
    }

    @Transactional
    public boolean delete(ObjectId id, String username) {

        User currentUser = UserService.findbyuser(username);

        if (currentUser == null ||
                currentUser.getJournalEntries() == null) {
            return false;
        }

        boolean removed = currentUser.getJournalEntries()
                .removeIf(entry -> id.equals(entry.getId()));

        if (!removed) {
            return false;
        }

        UserService.saveUser(currentUser);
        JournalRepository.deleteById(id);

        return true;
    }
    public void deleteByAdmin(ObjectId id) {
        JournalRepository.deleteById(id);
    }
}
