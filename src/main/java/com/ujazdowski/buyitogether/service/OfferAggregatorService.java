package com.ujazdowski.buyitogether.service;

import com.ujazdowski.buyitogether.domain.Chat;
import com.ujazdowski.buyitogether.domain.UserOffer;
import com.ujazdowski.buyitogether.domain.UserOfferChat;
import com.ujazdowski.buyitogether.repository.UserOfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OfferAggregatorService {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
    private final Logger log = LoggerFactory.getLogger(OfferAggregatorService.class);
    private final UserOfferRepository userOfferRepository;
    private final ChatService chatService;

    public OfferAggregatorService(UserOfferRepository userOfferRepository, ChatService chatService) {
        this.userOfferRepository = userOfferRepository;
        this.chatService = chatService;
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void aggregate() {
        log.info("Start aggregating offers - {}", new Date());

        Map<String, Set<Set<UserOffer>>> proposedGroups = proposeGroups(getUsersToAggregate());
        chatService.saveChats(createInvitations(proposedGroups));

        log.info("Stop aggregating offers - {}", new Date());
    }

    private List<Chat> createInvitations(Map<String, Set<Set<UserOffer>>> proposedGroups) {
        List<Chat> chats = new LinkedList<>();
        for (Map.Entry<String, Set<Set<UserOffer>>> groups : proposedGroups.entrySet()) {
            for (Set<UserOffer> group : groups.getValue()) {
                chats.add(new Chat()
                    .addUsers(group.stream().map(userOffer -> new UserOfferChat().accepted(false).userOffer(userOffer))
                        .collect(Collectors.toList())));
            }
        }
        return chats;
    }

    private Map<String, Set<Set<UserOffer>>> proposeGroups(Map<String, List<UserOffer>> userOffersMap) {
        Map<String, Set<Set<UserOffer>>> proposedGroups = new LinkedHashMap<>();
        for (Map.Entry<String, List<UserOffer>> entry : userOffersMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                proposedGroups.put(entry.getKey(), findGroups(entry.getValue()));
            }
        }
        return proposedGroups;
    }

    private Set<Set<UserOffer>> findGroups(List<UserOffer> offers) {
        Set<Set<UserOffer>> groups = new HashSet<>();
        for (Iterator<UserOffer> iterator = offers.iterator(); iterator.hasNext(); ) {
            Set<UserOffer> group = createGroup(iterator.next(), offers);
            if (!group.isEmpty()) {
                groups.add(group);
            }
        }
        return groups;
    }

    private Set<UserOffer> createGroup(UserOffer candidate, List<UserOffer> offers) {
        Set<UserOffer> group = new HashSet<>();

        for (UserOffer userOffer : offers) {
            if (userOffer != candidate && distance(candidate.getLatitude(), candidate.getLongitude(),
                userOffer.getLatitude(), userOffer.getLongitude()) <= (candidate.getDistance() + userOffer.getDistance())) {
                group.add(userOffer);
            }
        }

        if (!group.isEmpty()) {
            group.add(candidate);
        }

        return group;
    }

    /**
     * Haversine formula - calculating distance between two points(Latitude, Longitude)
     *
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     * @return the distance between points
     */
    private double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double dLat = Math.toRadians((latitude2 - latitude1));
        double dLong = Math.toRadians((longitude2 - longitude1));

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        double a = haversin(dLat) + Math.cos(latitude1) * Math.cos(latitude2) * haversin(dLong);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return EARTH_RADIUS * c;
    }

    private double haversin(double val) {
        return Math.pow(Math.sin(val / 2.0), 2.0);
    }

    private Map<String, List<UserOffer>> getUsersToAggregate() {
        Map<String, List<UserOffer>> userOffersMap = new LinkedHashMap<>();
        userOfferRepository.findUserOffersWithNotAcceptedChat().stream().forEach(userOffer -> {
            if (!userOffersMap.containsKey(userOffer.getLink())) {
                userOffersMap.put(userOffer.getLink(), new ArrayList<>());
            }
            userOffersMap.get(userOffer.getLink()).add(userOffer);
        });
        return userOffersMap;
    }
}
