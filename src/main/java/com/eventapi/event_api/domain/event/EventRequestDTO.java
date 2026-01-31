package com.eventapi.event_api.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record EventRequestDTO(
        String title,
        String description,
        long date,
        String city,
        String uf,
        Boolean remote,
        String eventUrl,
        MultipartFile image
        ) {
}
