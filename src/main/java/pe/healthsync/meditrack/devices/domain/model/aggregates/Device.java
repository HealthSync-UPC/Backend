package pe.healthsync.meditrack.devices.domain.model.aggregates;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceCommand;
import pe.healthsync.meditrack.devices.domain.model.entities.DeviceReading;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.DeviceType;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.StatusType;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Device extends AuditableAbstractAggregateRoot<Device> {
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    private String name;

    private String serialNumber;

    private String unit;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    private String location;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DeviceReading> readings = new ArrayList<>();

    public Device(CreateDeviceCommand command, User admin, DeviceType type, StatusType status) {
        this.admin = admin;
        this.name = command.name();
        this.serialNumber = command.serialNumber();
        this.type = type;
        this.location = command.location();
        this.status = status;
        this.unit = command.unit();
    }

    public void addReading(DeviceReading reading) {
        this.readings.add(reading);
        reading.setDevice(this);
    }
}
