package pe.healthsync.meditrack.accesscontrol.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddDeviceCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddItemCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveDeviceCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveItemCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.TryAccessCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneHumidityCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneTemperatureCommand;
import pe.healthsync.meditrack.accesscontrol.domain.services.ZoneCommandService;
import pe.healthsync.meditrack.accesscontrol.infrastructure.persistence.jpa.respositories.ZoneRepository;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses.AccessLogResponse;
import pe.healthsync.meditrack.devices.infrastructure.persistence.jpa.respositories.DeviceRepository;
import pe.healthsync.meditrack.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.healthsync.meditrack.inventory.infrastructure.persistence.jpa.respositories.ItemRepository;
import pe.healthsync.meditrack.shared.infrastructure.websocket.AppWebSocketHandler;
import pe.healthsync.meditrack.shared.infrastructure.websocket.WebSocketMessage;

@Service
public class ZoneCommandServiceImpl implements ZoneCommandService {
        @Autowired
        private ZoneRepository zoneRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private DeviceRepository deviceRepository;

        @Autowired
        private ItemRepository itemRepository;

        @Autowired
        private AppWebSocketHandler webSocketHandler;

        @Autowired
        private ObjectMapper objectMapper;

        @Override
        public Zone handle(CreateZoneCommand command) {
                var admin = userRepository.findById(command.adminId())
                                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

                var nfcDevice = deviceRepository.findById(command.deviceId())
                                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

                var devices = deviceRepository.findAllById(command.deviceIds());

                var items = itemRepository.findAllById(command.itemIds());

                var members = userRepository.findAllById(command.memberIds());

                var zone = new Zone(command, admin, nfcDevice, devices, items, members);

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

                var accessLog = zone.addAccessLog(user);

                var accessResponse = AccessLogResponse.fromEntity(accessLog);

                WebSocketMessage wsMessage = new WebSocketMessage(
                                "access",
                                accessResponse);

                var email = zone.getAdmin().getEmail();
                var emailDomain = email.substring(email.indexOf('@') + 1);

                try {
                        String payloadJson = objectMapper.writeValueAsString(wsMessage);
                        webSocketHandler.sendToDomain(emailDomain, payloadJson);
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }

                return zoneRepository.save(zone);
        }

        @Override
        public Zone handle(AddDeviceCommand command) {
                var zone = zoneRepository.findById(command.zoneId())
                                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                if (!zone.getAdmin().getId().equals(command.adminId())) {
                        throw new IllegalArgumentException("Only the admin can add devices");
                }

                var device = deviceRepository.findById(command.deviceId())
                                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

                zone.addDevice(device);

                return zoneRepository.save(zone);
        }

        @Override
        public Zone handle(RemoveDeviceCommand command) {
                var zone = zoneRepository.findById(command.zoneId())
                                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                if (!zone.getAdmin().getId().equals(command.adminId())) {
                        throw new IllegalArgumentException("Only the admin can remove devices");
                }

                var device = deviceRepository.findById(command.deviceId())
                                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

                zone.removeDevice(device);

                return zoneRepository.save(zone);
        }

        @Override
        public Zone handle(AddItemCommand command) {
                var zone = zoneRepository.findById(command.zoneId())
                                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                if (!zone.getAdmin().getId().equals(command.adminId())) {
                        throw new IllegalArgumentException("Only the admin can add items");
                }

                var item = itemRepository.findById(command.itemId())
                                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

                zone.addItem(item);

                return zoneRepository.save(zone);
        }

        @Override
        public Zone handle(RemoveItemCommand command) {
                var zone = zoneRepository.findById(command.zoneId())
                                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                if (!zone.getAdmin().getId().equals(command.adminId())) {
                        throw new IllegalArgumentException("Only the admin can add items");
                }

                var item = itemRepository.findById(command.itemId())
                                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

                zone.removeItem(item);

                return zoneRepository.save(zone);
        }

        @Override
        public Zone handle(UpdateZoneTemperatureCommand command) {
                var zone = zoneRepository.findById(command.zoneId())
                                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                if (!zone.getAdmin().getId().equals(command.adminId())) {
                        throw new IllegalArgumentException("Only the admin can update zone temperature bounds");
                }

                zone.updateTemperatureBounds(command);

                return zoneRepository.save(zone);
        }

        @Override
        public Zone handle(UpdateZoneHumidityCommand command) {
                var zone = zoneRepository.findById(command.zoneId())
                                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

                if (!zone.getAdmin().getId().equals(command.adminId())) {
                        throw new IllegalArgumentException("Only the admin can update zone humidity bounds");
                }

                zone.updateHumidityBounds(command);

                return zoneRepository.save(zone);
        }
}