package com.mndx.planner.link;

import com.mndx.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip){
        Link newLink = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }
    public List<LinkDTO> getAllLinksFromTrip(UUID tripId){
        return this.linkRepository.findByTripId(tripId).stream().map(link -> new LinkDTO(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
