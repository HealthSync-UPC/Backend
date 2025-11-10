package pe.healthsync.meditrack.accesscontrol.domain.model.aggregates;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.entities.AccessLog;
import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@NoArgsConstructor
@Getter
public class Zone extends AuditableAbstractAggregateRoot<Zone> {
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Device device;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> members = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AccessLog> accessLogs = new ArrayList<>();

    public Zone(CreateZoneCommand command, User admin, Device device, List<User> members) {
        this.admin = admin;
        this.name = command.name();
        this.device = device;
        this.members = members;
    }

    public void addAccessLog(User user) {
        boolean accessGranted = members.stream().anyMatch(member -> member.equals(user));
        AccessLog accessLog = new AccessLog(user, accessGranted);
        this.accessLogs.add(accessLog);
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }
}
