package ua.nure.teamsync.entities.udts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import ua.nure.teamsync.entities.Performer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ua.nure.teamsync.encryption.EncryptionUtil.decrypt;
import static ua.nure.teamsync.encryption.EncryptionUtil.encrypt;

@Data
@UserDefinedType("team_member")
@NoArgsConstructor
@AllArgsConstructor
public class PerformerUDT {
    private static final String encryptionAlgorithm = "AES/CBC/PKCS5Padding";

    private String login;
    private String name;
    private String phoneNumber;
    private String healthCondition = Performer.Condition.NORMAL.name();
    private String environmentCondition = Performer.Condition.NORMAL.name();
    private boolean managerAccount;
    private LocalDateTime registeredAt = LocalDateTime.now();

    public PerformerUDT(String login) {
        this.login = login;
    }

    public String getName() {
        if (this.name == null) return null;
        try {
            return decrypt(encryptionAlgorithm, name);
        } catch (Exception e) {
            return null;
        }
    }

    public String getPhoneNumber() {
        if (this.phoneNumber == null) return null;
        try {
            return decrypt(encryptionAlgorithm, phoneNumber);
        } catch (Exception e) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformerUDT that = (PerformerUDT) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return login.length();
    }
}
