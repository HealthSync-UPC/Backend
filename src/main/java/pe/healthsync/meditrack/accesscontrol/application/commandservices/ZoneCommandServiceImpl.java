package pe.healthsync.meditrack.accesscontrol.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.TryAccessCommand;
import pe.healthsync.meditrack.accesscontrol.domain.services.ZoneCommandService;
import pe.healthsync.meditrack.accesscontrol.infrastructure.persistence.jpa.respositories.ZoneRepository;
import pe.healthsync.meditrack.devices.infrastructure.persistence.jpa.respositories.DeviceRepository;
import pe.healthsync.meditrack.iam.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class ZoneCommandServiceImpl implements ZoneCommandService {
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Zone handle(CreateZoneCommand command) {
        var admin = userRepository.findById(command.adminId())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        var members = userRepository.findAllById(command.memberIds());

        var zone = new Zone(command, admin, device, members);

        return zoneRepository.save(zone);
    }

    @Override
    public Zone handle(AddMemberCommand command) {
        var zone = zoneRepository.findById(command.zoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

        if (!zone.getAdmin().getId().equals(command.adminId())) {
            throw new IllegalArgumentException("Only the admin can add members");
        }

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        zone.addMember(user);

        return zoneRepository.save(zone);
    }

    @Override
    public Zone handle(RemoveMemberCommand command) {
        var zone = zoneRepository.findById(command.zoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

        if (!zone.getAdmin().getId().equals(command.adminId())) {
            throw new IllegalArgumentException("Only the admin can remove members");
        }

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        zone.removeMember(user);

        return zoneRepository.save(zone);
    }

    @Override
    public Zone handle(TryAccessCommand command) {
        var zone = zoneRepository.findById(command.zoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        zone.addAccessLog(user);

        return zoneRepository.save(zone);
    }
}