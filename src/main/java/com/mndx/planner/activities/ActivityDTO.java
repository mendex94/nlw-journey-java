package com.mndx.planner.activities;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO(UUID id, String title, LocalDateTime uccurs_at) {
}
