package pe.healthsync.meditrack.devices.domain.model.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceReadingCommand;
import pe.healthsync.meditrack.shared.domain.model.entities.AuditableModel;

@Entity
@Getter
@NoArgsConstructor
public class DeviceReading extends AuditableModel {
    @ManyToOne(fetch = FetchType.LAZY)
    private Device device;

    private Instant readingAt;

    private String value;

    private Double numericValue;

    /*
     * @ElementCollection
     * 
     * @CollectionTable(name = "device_readings_metadata", joinColumns
     * = @JoinColumn(name = "reading_id"))
     * 
     * @MapKeyColumn(name = "meta_key")
     * 
     * @Column(name = "meta_value")
     * private Map<String, String> metadata = new HashMap<>();
     */

    public DeviceReading(CreateDeviceReadingCommand command) {
        this.readingAt = Instant.now();
        this.value = command.value();
        this.numericValue = command.numericValue();
    }
}