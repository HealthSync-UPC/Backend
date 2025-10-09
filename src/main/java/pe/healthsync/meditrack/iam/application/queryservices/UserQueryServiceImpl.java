package pe.healthsync.meditrack.iam.application.queryservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.iam.domain.model.queries.GetUserByIdQuery;
import pe.healthsync.meditrack.iam.domain.services.UserQueryService;
import pe.healthsync.meditrack.iam.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
