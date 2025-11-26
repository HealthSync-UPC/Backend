package pe.healthsync.meditrack.accesscontrol.domain.model.aggregates;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneHumidityCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneTemperatureCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.entities.AccessLog;
import pe.healthsync.meditrack.accesscontrol.domain.model.entities.Alert;
import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.inventory.domain.model.entities.Item;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@NoArgsConstructor
@Getter
public class Zone extends AuditableAbstractAggregateRoot<Zone> {
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Device nfcDevice;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> members = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AccessLog> accessLogs = new ArrayList<>();

    private Double minTemperature = 0.0;
    private Double maxTemperature = 0.0;
    private Double minHumidity = 0.0;
    private Double maxHumidity = 0.0;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Alert> alerts = new ArrayList<>();

    public Zone(
            CreateZoneCommand command,
            User admin,
            Device nfcDevice,
            List<Device> devices,
            List<Item> items,
            List<User> members) {
        this.admin = admin;
        this.name = command.name();
        this.nfcDevice = nfcDevice;
        nfcDevice.setZone(this);
        this.devices = devices;
        devices.forEach(d -> d.setZone(this));
        this.items = items;
        items.forEach(i -> i.setZone(this));
        this.members = members;
    }

    public void addAccessLog(User user) {
        boolean accessGranted = members.stream().anyMatch(member -> member.equals(user)) || admin.equals(user);
        AccessLog accessLog = new AccessLog(user, accessGranted);
        this.accessLogs.add(accessLog);
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }

    public void addDevice(Device device) {
        this.devices.add(device);
        device.setZone(this);
    }

    public void removeDevice(Device device) {
        this.devices.remove(device);
        device.setZone(null);
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setZone(this);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
        item.setZone(null);
    }

    public Alert raiseAlert(Alert alert) {
        this.alerts.add(alert);
        return alert;
    }

    public void updateTemperatureBounds(UpdateZoneTemperatureCommand command) {
        this.minTemperature = command.minTemperature();
        this.maxTemperature = command.maxTemperature();
    }

    public void updateHumidityBounds(UpdateZoneHumidityCommand command) {
        this.minHumidity = command.minHumidity();
        this.maxHumidity = command.maxHumidity();
    }
}
