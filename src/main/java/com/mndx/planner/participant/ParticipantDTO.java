package com.mndx.planner.participant;

import java.util.UUID;

public record ParticipantDTO(UUID id, String email, String name, Boolean isConfirmed) {
}
