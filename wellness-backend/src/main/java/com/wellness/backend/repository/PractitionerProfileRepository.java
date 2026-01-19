package com.wellness.backend.repository;

import com.wellness.backend.model.PractitionerProfile;
import com.wellness.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
@Repository
public interface PractitionerProfileRepository
        extends JpaRepository<PractitionerProfile, Long> {

    Optional<PractitionerProfile> findByUser(User user);

    @Query(value = """
        SELECT * FROM practitioner_profiles p
        WHERE (
            6371 * acos(
                cos(radians(:lat)) * cos(radians(p.latitude)) *
                cos(radians(p.longitude) - radians(:lng)) +
                sin(radians(:lat)) * sin(radians(p.latitude))
            )
        ) <= :radius
        """, nativeQuery = true)
    List<PractitionerProfile> findNearby(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius
    );
}
