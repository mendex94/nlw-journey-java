package com.mndx.planner.activities;

import com.mndx.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public ActivityResponse createActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME), trip);

        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityDTO> getAllActivitiesFromId(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
