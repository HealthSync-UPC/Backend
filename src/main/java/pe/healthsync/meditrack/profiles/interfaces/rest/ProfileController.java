package pe.healthsync.meditrack.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfileByUserId;
import pe.healthsync.meditrack.profiles.domain.services.ProfileCommandService;
import pe.healthsync.meditrack.profiles.domain.services.ProfileQueryService;
import pe.healthsync.meditrack.profiles.interfaces.rest.requests.CreateProfileRequest;
import pe.healthsync.meditrack.profiles.interfaces.rest.responses.ProfileResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profiles Endpoints")
public class ProfileController {
    @Autowired
    private ProfileCommandService profileCommandService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<ProfileResource> getProfile() {
        var userId = (Long) this.request.getAttribute("userId");

        var query = new GetProfileByUserId(userId);
        var profile = profileQueryService.handle(query);

        var profileResponse = ProfileResource.fromEntity(profile._1, profile._2, profile._3);

        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }

    @PostMapping()
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileRequest request) {
        var adminId = (Long) this.request.getAttribute("userId");

        var command = request.toCommand(adminId);

        var profile = profileCommandService.handle(command);

        var profileResponse = ProfileResource.fromEntity(profile._1, profile._2, profile._3);

        return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
    }

}
