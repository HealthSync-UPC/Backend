package pe.healthsync.meditrack.devices.domain.model.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceReadingCommand;
import pe.healthsync.meditrack.shared.domain.model.entities.AuditableModel;

@Entity
@Getter
@NoArgsConstructor
public class DeviceReading extends AuditableModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Device device;

    private Instant readingAt;

    private Double value;

    public DeviceReading(CreateDeviceReadingCommand command) {
        this.readingAt = Instant.now();
        this.value = command.value();
    }
}