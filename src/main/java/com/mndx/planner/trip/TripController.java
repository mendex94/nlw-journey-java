package com.mndx.planner.trip;

import com.mndx.planner.activities.ActivityDTO;
import com.mndx.planner.activities.ActivityRequestPayload;
import com.mndx.planner.activities.ActivityResponse;
import com.mndx.planner.activities.ActivityService;
import com.mndx.planner.link.LinkDTO;
import com.mndx.planner.link.LinkRequestPayload;
import com.mndx.planner.link.LinkResponse;
import com.mndx.planner.link.LinkService;
import com.mndx.planner.participant.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")

public class TripController {
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;
    private final TripRepository tripRepository;

    public TripController(ParticipantService participantService, ActivityService activityService, LinkService linkService, TripRepository tripRepository) {
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;

        this.tripRepository = tripRepository;
    }

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip trip = new Trip(payload);
        this.tripRepository.save(trip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), trip);

        return ResponseEntity.ok(new TripCreateResponse(trip.getId()));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip rawTrip = trip.get();
        rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setDestination(payload.destination());

        this.tripRepository.save(rawTrip);

        return ResponseEntity.ok(rawTrip);
    }

    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip rawTrip = trip.get();
        rawTrip.setIsConfirmed(true);

        this.tripRepository.save(rawTrip);
        this.participantService.triggerConfirmationEmailsToParticipants(tripId);

        return ResponseEntity.ok(rawTrip);
    }

    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip rawTrip = trip.get();

        ParticipantCreateResponse participantCreateResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

        if(rawTrip.getIsConfirmed()) {
            this.participantService.triggerConfirmationEmailsToParticipant(payload.email());
        }

        return ResponseEntity.ok(participantCreateResponse);
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<ParticipantDTO>> getParticipants(@PathVariable UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ParticipantDTO> participants = this.participantService.getAllParticipantsFromEvent(tripId);

        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> addActivity(@PathVariable UUID tripId, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip rawTrip = trip.get();

        ActivityResponse activityResponse = this.activityService.createActivity(payload, rawTrip);

        return ResponseEntity.ok(activityResponse);
    }

    @GetMapping("/{tripId}/activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@PathVariable UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ActivityDTO> activities = this.activityService.getAllActivitiesFromId(tripId);

        return ResponseEntity.ok(activities);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {

        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();


            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);


            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkDTO>> getAllLinks(@PathVariable UUID id){
        List<LinkDTO> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkDataList);
    }
}
