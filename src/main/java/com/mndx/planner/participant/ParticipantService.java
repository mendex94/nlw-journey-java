package com.mndx.planner.participant;

import com.mndx.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip)).toList();

        this.participantRepository.saveAll(participants);
    }

    public ParticipantCreateResponse registerParticipantToEvent(String participantToInvite, Trip trip) {
        Participant participant = new Participant(participantToInvite, trip);
        this.participantRepository.save(participant);

        return new ParticipantCreateResponse(participant.getId());
    }

    public void triggerConfirmationEmailsToParticipants(UUID tripId) {}

    public void triggerConfirmationEmailsToParticipant(String participantEmail) {
    }

    public List<ParticipantDTO> getAllParticipantsFromEvent(UUID tripId) {
        return this.participantRepository.findByTripId(tripId).stream().map(participant -> new ParticipantDTO(participant.getId(), participant.getEmail(), participant.getName(), participant.getIsConfirmed())).toList();
    }
}
