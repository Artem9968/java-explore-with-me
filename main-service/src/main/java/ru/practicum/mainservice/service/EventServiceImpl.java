package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import ru.practicum.mainservice.dto.*;
import ru.practicum.mainservice.model.enums.EventModerationAction;
import ru.practicum.mainservice.model.enums.EventStatus;
import ru.practicum.mainservice.model.enums.UserEventAction;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.exception.DataConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.EventApprovalStats;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.EventSpecification;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.statsclient.StatsClient;
import ru.practicum.statsdto.StatsDto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Integer HOURS_EVENT_DELAY = 2;

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatsClient statsClient;


    @Override
    public EventFullDto createEvent(EventCreateRequest eventCreateRequest, Integer userId) {
        if (eventCreateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(HOURS_EVENT_DELAY))) {
            throw new ValidationException(
                    "Field: eventDate. Error: не может быть раньше, чем через "
                            + HOURS_EVENT_DELAY + " часа от текущего момента. Value: "
                            + eventCreateRequest.getEventDate().format(DATA_TIME_FORMATTER)
            );
        }
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(eventCreateRequest.getCategoryId());
        Event event = EventMapper.toEvent(eventCreateRequest);
        event.setOrganizer(user);
        event.setCategory(category);
        Event savedEvent = eventRepository.save(event);
        return EventMapper.toFullDto(savedEvent);
    }


    @Override
    public EventFullDto getEventById(Integer eventId, Integer userId) {
        Event event = findEventById(eventId);
        if (!event.getOrganizer().getId().equals(userId)) {
            throw new ValidationException("Пользователь id=" + userId
                    + " не является инициатором события id=" + eventId);
        }
        event.setApprovedParticipants(requestRepository.getCountConfirmedRequestsByEventId(eventId));
        event.setViewCount(statsClient.getEventViews(eventId, true));
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Integer userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findEventsByInitiator_Id(userId);
        updateViwesAndRequests(events);
        return events.stream()
                .skip(from)
                .limit(size)
                .map(EventMapper::toShortDto)
                .toList();
    }

    private void updateViwesAndRequests(List<Event> events) {
        TreeMap<Integer, Event> eventMap = new TreeMap<>();
        List<String> eventUris = new ArrayList<>();
        for (Event event : events) {
            eventMap.put(event.getId(), event);
            eventUris.add(String.format("/events/%d", event.getId()));
        }

        List<EventApprovalStats> counts =
                requestRepository.getCountConfirmedRequests(eventMap.keySet().stream().toList());
        for (EventApprovalStats count : counts) {
            Integer eventId = count.getEventId();
            eventMap.get(eventId).setApprovedParticipants(count.getApprovedParticipantsCount().intValue());
        }

        List<StatsDto> statsDtos = statsClient.getEventViewsByUris(eventUris, true);
        for (StatsDto dto : statsDtos) {
            Integer eventId = Integer.parseInt(dto.getUri().split("/")[2]);
            eventMap.get(eventId).setViewCount(dto.getHits());
        }
    }


    @Override
    public EventFullDto patchEvent(Integer eventId, @Validated EventUserUpdateRequest eventDto, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("Не найдено событие id=" + eventId));
        if (!event.getOrganizer().getId().equals(userId)) {
            throw new DataConflictException("Пользователь id=" + userId
                    + " не является инициатором события id=" + eventId);
        }
        if (event.getScheduledTime().isBefore(LocalDateTime.now().plusHours(HOURS_EVENT_DELAY))) {
            throw new ValidationException(
                    "Field: eventDate. Error: не может быть раньше, чем через "
                            + HOURS_EVENT_DELAY + " часа от текущего момента. Value: "
                            + event.getScheduledTime().format(DATA_TIME_FORMATTER)
            );
        }
        if (event.getState().equals(EventStatus.ACTIVE)) {
            throw new DataConflictException(
                    "Field: event.state. Error: Недопустимый статус события для изменения."
                            + " Value: " + event.getState());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategoryId() != null) {
            event.setCategory(categoryService.getCategoryById(eventDto.getCategoryId()));
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(HOURS_EVENT_DELAY))) {
                throw new ValidationException(
                        "Field: eventDate. Error: новое значение не может быть раньше, чем через "
                                + HOURS_EVENT_DELAY + " часа от текущего момента. Value: "
                                + eventDto.getEventDate().format(DATA_TIME_FORMATTER)
                );
            }
            event.setScheduledTime(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            event.setLatitude(eventDto.getLocation().getLatitude());
            event.setLongitude(eventDto.getLocation().getLongitude());
        }
        if (eventDto.getIsPaid() != null) {
            event.setIsPaid(eventDto.getIsPaid());
        }
        if (eventDto.getMaxAttendees() != null) {
            event.setMaxAttendees(eventDto.getMaxAttendees());
        }
        if (eventDto.getRequiresApproval() != null) {
            event.setRequiresApproval(eventDto.getRequiresApproval());
        }
        if (eventDto.getUserAction() != null) {
            if (eventDto.getUserAction().equals(UserEventAction.CANCEL_SUBMISSION)) {
                event.setState(EventStatus.CANCELLED);
            } else if (eventDto.getUserAction().equals(UserEventAction.SEND_FOR_MODERATION)) {
                event.setState(EventStatus.IN_MODERATION);
            }
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        Event savedEvent = eventRepository.save(event);
        savedEvent.setApprovedParticipants(requestRepository.getCountConfirmedRequestsByEventId(eventId));
        savedEvent.setViewCount(statsClient.getEventViews(eventId, true));
        return EventMapper.toFullDto(savedEvent);
    }

    @Override
    public EventFullDto adminUpdateEvent(Integer eventId, EventAdminUpdateRequest eventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("Не найдено событие id=" + eventId));
        if (event.getScheduledTime() != null) {
            if (event.getScheduledTime().isBefore(LocalDateTime.now().plusHours(HOURS_EVENT_DELAY))) {
                throw new ValidationException(
                        "Field: eventDate. Error: не может быть раньше, чем через "
                                + HOURS_EVENT_DELAY + " часа от текущего момента. Value: "
                                + event.getScheduledTime().format(DATA_TIME_FORMATTER)
                );
            }
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategoryId() != null) {
            event.setCategory(categoryService.getCategoryById(eventDto.getCategoryId()));
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(HOURS_EVENT_DELAY))) {
                throw new ValidationException(
                        "Field: eventDate. Error: новое значение не может быть раньше, чем через "
                                + HOURS_EVENT_DELAY + " часа от текущего момента. Value: "
                                + eventDto.getEventDate().format(DATA_TIME_FORMATTER)
                );
            }
            event.setScheduledTime(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            event.setLatitude(eventDto.getLocation().getLatitude());
            event.setLongitude(eventDto.getLocation().getLongitude());
        }
        if (eventDto.getIsPaid() != null) {
            event.setIsPaid(eventDto.getIsPaid());
        }
        if (eventDto.getMaxAttendees() != null) {
            event.setMaxAttendees(eventDto.getMaxAttendees());
        }
        if (eventDto.getRequiresApproval() != null) {
            event.setRequiresApproval(eventDto.getRequiresApproval());
        }
        if (eventDto.getModerationAction() != null) {
            if (eventDto.getModerationAction().equals(EventModerationAction.APPROVE_PUBLICATION)) {
                if (!event.getState().equals(EventStatus.IN_MODERATION)) {
                    throw new DataConflictException(
                            "Field: stateAction. Error: " +
                                    "Событие id=" + eventId + " должно быть в состоянии ожидания публикации." +
                                    " Value: " + eventDto.getModerationAction()
                    );
                }
                event.setState(EventStatus.ACTIVE);
                event.setPublicationTime(LocalDateTime.now());
            } else if (eventDto.getModerationAction().equals(EventModerationAction.CANCEL_PUBLICATION)) {
                if (event.getState().equals(EventStatus.ACTIVE)) {
                    throw new DataConflictException(
                            "Field: stateAction. Error: " +
                                    "Нельзя удалить опубликованное событие id=" + eventId +
                                    " Value: " + eventDto.getModerationAction()
                    );
                }
                event.setState(EventStatus.DECLINED);
            } else {
                throw new ValidationException(
                        "Field: stateAction. Error: " +
                                "Указано непредусмотренное действие. " +
                                " Value: " + eventDto.getModerationAction());
            }
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        Event savedEvent = eventRepository.save(event);
        savedEvent.setApprovedParticipants(requestRepository.getCountConfirmedRequestsByEventId(eventId));
        savedEvent.setViewCount(statsClient.getEventViews(eventId, true));
        return EventMapper.toFullDto(savedEvent);
    }

    @Override
    public Event findEventById(Integer eventId) {
        log.info("🔍🔍🔍 EventServiceImpl.findEventById id={}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("❌❌❌ Event not found id={}", eventId);
                    return new NotFoundException("Не найдено событие id=" + eventId);
                });

        log.info("📊📊📊 Before setting confirmedRequests: {}", event.getViewCount());
        event.setApprovedParticipants(requestRepository.getCountConfirmedRequestsByEventId(eventId));
        log.info("📊📊📊 After confirmedRequests: {}", event.getViewCount());

        log.info("📡📡📡 Calling statsClient.getEventViews...");
        Integer views = statsClient.getEventViews(eventId, true);
        log.info("👀👀👀 Stats returned views: {}", views);

        event.setViewCount(views);
        log.info("📊📊📊 Final views set: {}", event.getViewCount());

        return event;
    }

    @Override
    public List<EventShortDto> findEventsByParametrs(String text, List<Integer> categories,
                                                     Boolean paid, String rangeStart, String rangeEnd,
                                                     Boolean onlyAvailable, String sort, Integer from, Integer size) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (rangeStart != null && !rangeStart.isEmpty()) {
            try {
                startDate = LocalDateTime.parse(rangeStart, DATA_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Некорректный формат времени. " + e.getMessage());
            }
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            try {
                endDate = LocalDateTime.parse(rangeEnd, DATA_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Некорректный формат времени. " + e.getMessage());
            }
        }
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new BadRequestException(
                        "Parameter: rangeStart, rangeEnd. " +
                                "Error: Введен некорректный интервал времени." +
                                ". Value: " + startDate.format(DATA_TIME_FORMATTER) +
                                ", " + endDate.format(DATA_TIME_FORMATTER)
                );
            }
        }

        if (startDate == null && endDate == null) {
            startDate = LocalDateTime.now();
        }

        Specification<Event> spec = Specification.where(null);

        if (text != null) {
            spec = spec.and(EventSpecification.annotetionContains(text));
            spec = spec.or(EventSpecification.descriptionContains(text));
        }

        if (categories != null) {
            spec = spec.and(EventSpecification.categoryIn(categories));
        }

        if (paid != null) {
            spec = spec.and(EventSpecification.paidEqual(paid));
        }

        if (startDate != null) {
            spec = spec.and(EventSpecification.eventDateAfter(startDate));
        }
        if (endDate != null) {
            spec = spec.and(EventSpecification.eventDateBefore(endDate));
        }

        List<Event> events = eventRepository.findAll(spec,
                Sort.by("eventDate"));
        if (events.isEmpty()) {
            return List.of();
        }
        updateViwesAndRequests(events);
        List<EventShortDto> eventDtos;
        if (onlyAvailable) {

            eventDtos = events.stream()
                    .filter(event -> event.getMaxAttendees() != 0
                            && event.getApprovedParticipants() < event.getMaxAttendees())
                    .map(EventMapper::toShortDto)
                    .toList();
        } else {
            eventDtos = events.stream().map(EventMapper::toShortDto).toList();
        }

        if (sort.equalsIgnoreCase("VIEWS")) {
            return eventDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViewCount).reversed())
                    .skip(from).limit(size).toList();
        }
        return eventDtos.stream().skip(from).limit(size).toList();
    }

    @Override
    public List<EventFullDto> findEventsByAdmin(List<String> states,
                                                List<Integer> users,
                                                List<Integer> categories,
                                                String rangeStart,
                                                String rangeEnd,
                                                Integer from,
                                                Integer size) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        try {
            if (rangeStart != null && !rangeStart.isEmpty()) {
                startDate = LocalDateTime.parse(rangeStart, DATA_TIME_FORMATTER);
            }
            if (rangeEnd != null && !rangeEnd.isEmpty()) {
                endDate = LocalDateTime.parse(rangeEnd, DATA_TIME_FORMATTER);
            }
            if (startDate != null && endDate != null) {
                startDate = LocalDateTime.now();
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException("Некорректный формат времени. " + e.getMessage());
        }

        Specification<Event> spec = Specification.where(null);

        if (users != null) {
            spec = spec.and(EventSpecification.eventInitiatorIdIn(users));
        }

        if (categories != null) {
            spec = spec.and(EventSpecification.categoryIn(categories));
        }

        if (states != null) {
            spec = spec.and(EventSpecification.eventStateIn(states));
        }

        if (startDate != null) {
            spec = spec.and(EventSpecification.eventDateAfter(startDate));
        }
        if (endDate != null) {
            spec = spec.and(EventSpecification.eventDateBefore(endDate));
        }

        List<Event> events = eventRepository.findAll(spec,
                Sort.by("eventDate"));
        if (events.isEmpty()) {
            return List.of();
        }
        updateViwesAndRequests(events);
        List<EventFullDto> eventDtos = events.stream()
                .map(EventMapper::toFullDto)
                .toList();
        return eventDtos.stream().skip(from).limit(size).toList();
    }


    @Override
    public List<Event> findEventsByIdIn(List<Integer> eventIds) {
        List<Event> events = eventRepository.findEventsByIdIn(eventIds);
        if (events.isEmpty()) {
            return List.of();
        }
        updateViwesAndRequests(events);
        return events;
    }

}
