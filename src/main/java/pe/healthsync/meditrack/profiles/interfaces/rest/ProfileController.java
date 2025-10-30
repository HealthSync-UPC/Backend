package pe.healthsync.meditrack.profiles.interfaces.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfileByUserIdQuery;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfilesByAdminIQuery;
import pe.healthsync.meditrack.profiles.domain.services.ProfileCommandService;
import pe.healthsync.meditrack.profiles.domain.services.ProfileQueryService;
import pe.healthsync.meditrack.profiles.interfaces.rest.requests.CreateProfileRequest;
import pe.healthsync.meditrack.profiles.interfaces.rest.responses.ProfileResource;
import pe.healthsync.meditrack.shared.infrastructure.security.AuthenticatedUserProvider;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profiles Endpoints")
public class ProfileController {
    @Autowired
    private ProfileCommandService profileCommandService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping()
    public ResponseEntity<ProfileResource> getProfile() {
        var user = authenticatedUserProvider.getCurrentUser();

        var query = new GetProfileByUserIdQuery(user.getId());
        var profile = profileQueryService.handle(query);

        var profileResponse = ProfileResource.fromEntity(profile._1, profile._2, profile._3);

        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProfileResource>> getAllProfilesByAdmin() {
        var admin = authenticatedUserProvider.getCurrentUser();

        var query = new GetProfilesByAdminIQuery(admin.getId());
        var profiles = profileQueryService.handle(query);

        var profileResponses = profiles.stream()
                .map(profile -> ProfileResource.fromEntity(profile._1, profile._2, profile._3))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(profileResponses);
    }

    @PostMapping()
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileRequest request) {
        var admin = authenticatedUserProvider.getCurrentUser();

        var command = request.toCommand(admin.getId());

        var profile = profileCommandService.handle(command);

        var profileResponse = ProfileResource.fromEntity(profile._1, profile._2, profile._3);

        return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
    }

}
