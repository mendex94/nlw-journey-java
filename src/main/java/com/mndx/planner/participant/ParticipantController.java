package com.mndx.planner.participant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    private final ParticipantRepository participantRepository;
    private final ParticipantService participantService;

    public ParticipantController(ParticipantRepository participantRepository, ParticipantService participantService) {
        this.participantRepository = participantRepository;
        this.participantService = participantService;
    }

    @PostMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID participantId, ParticipantRequestPayload payload) {
        Optional<Participant> participant = this.participantRepository.findById(participantId);

        if (participant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Participant rawParticipant = participant.get();
        rawParticipant.setIsConfirmed(true);
        rawParticipant.setName(payload.name());

        this.participantRepository.save(rawParticipant);

        return ResponseEntity.ok(rawParticipant);
    }
}
