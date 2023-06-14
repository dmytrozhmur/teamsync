package ua.nure.teamsync.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.mapping.*;
import ua.nure.teamsync.entities.udts.TaskUDT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ua.nure.teamsync.encryption.EncryptionUtil.decrypt;
import static ua.nure.teamsync.encryption.EncryptionUtil.encrypt;

@Slf4j
@Data
@Table("team_members")
@NoArgsConstructor
public class Performer {
    private static final String encryptionAlgorithm = "AES/CBC/PKCS5Padding";

    @PrimaryKey
    private PerformerId performerId = new PerformerId();
    private String name;
    private String phoneNumber;
    private String healthCondition = Condition.NORMAL.name();
    private String environmentCondition = Condition.NORMAL.name();
    private boolean managerAccount;
    private LocalDateTime registeredAt = LocalDateTime.now();
    @Column("tasks")
    private List<TaskUDT> tasks = new ArrayList<>();

    public boolean isHelpNeeded() {
        return Condition.CRITICAL.equals(Condition.valueOf(healthCondition))
                || Condition.CRITICAL.equals(Condition.valueOf(environmentCondition))
                || (Condition.BAD.equals(Condition.valueOf(healthCondition)) && Condition.BAD.equals(Condition.valueOf(environmentCondition)));
    }

    public String getName() {
        if (this.name == null) {
            return null;
        }
        try {
            return decrypt(encryptionAlgorithm, name);
        } catch (Exception e) {
            log.error(e.getClass().getSimpleName());
            log.error(e.getMessage());
            return null;
        }
    }

    public String getPhoneNumber() {
        if (this.phoneNumber == null) return null;
        try {
            return decrypt(encryptionAlgorithm, phoneNumber);
        } catch (Exception e) {
            log.error(e.getClass().getSimpleName());
            log.error(e.getMessage());
            return null;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            this.phoneNumber = null;
            return;
        }
        try {
            this.phoneNumber = encrypt(encryptionAlgorithm, phoneNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name) {
        if (name == null) {
            this.name = null;
            return;
        }
        try {
            this.name = encrypt(encryptionAlgorithm, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum Condition {
        CRITICAL, BAD, NORMAL, GOOD
    }
}
