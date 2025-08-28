package ru.practicum.mainservice.service.event;

import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.event.UpdateEventAdminRequest;
import ru.practicum.mainservice.dto.event.UpdateEventUserRequest;
import ru.practicum.mainservice.service.user.UserService;
import ru.practicum.mainservice.service.category.CategoryService;
import ru.practicum.statsdto.StatsDto;
import ru.practicum.statsclient.StatsClient;
import ru.practicum.mainservice.storage.request.ParticipationRequestRepository;
import ru.practicum.mainservice.storage.event.EventSpecification;
import ru.practicum.mainservice.storage.event.EventRepository;
import ru.practicum.mainservice.model.user.User;
import ru.practicum.mainservice.model.event.EventConfirmedRequestCount;
import ru.practicum.mainservice.model.event.Event;
import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.model.enums.EventUserStateAction;
import ru.practicum.mainservice.model.enums.EventState;
import ru.practicum.mainservice.model.enums.EventAdminStateAction;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final StatsClient statsClient;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Integer hoursEventDelay = 2;   // дата и время на которые намечено событие не может быть раньше, чем
    // через два часа от текущего момента

    private Event findEventOrThrow(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие"));
    }

    private void validateEventDate(LocalDateTime eventDate, String fieldName) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(hoursEventDelay))) {
            throw new ValidationException(
                    fieldName + ". Ошибка: событие не может быть раньше, чем через "
                            + hoursEventDelay + " часа от текущего момента "
                            + eventDate.format(dateTimeFormatter)
            );
        }
    }

    private LocalDateTime parseDateTime(String dateTimeString, String fieldName) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Некорректный формат времени");
        }
    }

    private void validateDateTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException(
                    "Введен некорректный интервал времени."
                            + startDate.format(dateTimeFormatter)
                            + ", " + endDate.format(dateTimeFormatter)
            );
        }
    }

    private void getEventStats(Event event) {
        Integer confirmedRequests = participationRequestRepository.countConfirmedByEventId(event.getId());
        Integer views = statsClient.getEventViews(event.getId(), true);
        event.setCachedConfirmedRequests(confirmedRequests);
        event.setCachedViews(views);
    }

    private void getEventsStats(List<Event> events) {
        TreeMap<Integer, Event> eventMap = new TreeMap<>();
        List<String> eventUris = new ArrayList<>();

        for (Event event : events) {
            eventMap.put(event.getId(), event);
            eventUris.add("/events/" + event.getId());
        }

        List<EventConfirmedRequestCount> counts = participationRequestRepository.countConfirmedByEventIdIn(new ArrayList<>(eventMap.keySet()));
        for (EventConfirmedRequestCount count : counts) {
            Event event = eventMap.get(count.getEventId());
            if (event != null) {
                event.setCachedConfirmedRequests(count.getConfirmedRequestCount().intValue());
            }
        }

        List<StatsDto> stats = statsClient.getEventViewsByUris(eventUris, true);
        for (StatsDto stat : stats) {
            try {
                Integer eventId = Integer.parseInt(stat.getUri().split("/")[2]);
                Event event = eventMap.get(eventId);
                if (event != null) {
                    event.setCachedViews(stat.getHits());
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                log.warn("Некорректный URI: {}", stat.getUri());
            }
        }
    }

    private Specification<Event> buildSearchSpecification(String text, List<Integer> categories, Boolean paid,
                                                          LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Event> searchSpec = Specification.where(null);

        if (text != null) {
            searchSpec = searchSpec.and(EventSpecification.hasAnnotationWithText(text)
                    .or(EventSpecification.hasDescriptionWithText(text)));
        }
        if (categories != null) {
            searchSpec = searchSpec.and(EventSpecification.hasCategoryIn(categories));
        }
        if (paid != null) {
            searchSpec = searchSpec.and(EventSpecification.hasPaid(paid));
        }
        if (startDate != null) {
            searchSpec = searchSpec.and(EventSpecification.withEventDateAfter(startDate));
        }
        if (endDate != null) {
            searchSpec = searchSpec.and(EventSpecification.withEventDateBefore(endDate));
        }

        return searchSpec;
    }

    private Specification<Event> buildAdminSpecification(List<String> states, List<Integer> users,
                                                         List<Integer> categories, LocalDateTime startDate,
                                                         LocalDateTime endDate) {
        Specification<Event> spec = Specification.where(null);

        if (users != null) {
            spec = spec.and(EventSpecification.withInitiatorIdIn(users));
        }
        if (categories != null) {
            spec = spec.and(EventSpecification.hasCategoryIn(categories));
        }
        if (states != null) {
            spec = spec.and(EventSpecification.withStateIn(states));
        }
        if (startDate != null) {
            spec = spec.and(EventSpecification.withEventDateAfter(startDate));
        }
        if (endDate != null) {
            spec = spec.and(EventSpecification.withEventDateBefore(endDate));
        }

        return spec;
    }

    @Override
    public List<Event> findEventsByIdIn(List<Integer> eventIds) {
        List<Event> events = eventRepository.findByIdIn(eventIds);
        if (!events.isEmpty()) {
            getEventsStats(events);
        }
        return events;
    }

    @Override
    public List<EventFullDto> findEventsByAdmin(List<String> states, List<Integer> users, List<Integer> categories,
                                                String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime startDate = parseDateTime(rangeStart, "rangeStart");
        LocalDateTime endDate = parseDateTime(rangeEnd, "rangeEnd");
        validateDateTimeRange(startDate, endDate);

        Specification<Event> spec = buildAdminSpecification(states, users, categories, startDate, endDate);
        List<Event> events = eventRepository.findAll(spec, Sort.by("eventDate"));

        if (events.isEmpty()) {
            return List.of();
        }

        getEventsStats(events);
        return events.stream()
                .map(EventMapper::toFullDto)
                .skip(from)
                .limit(size)
                .toList();
    }

    @Override
    public List<EventShortDto> findEventsByParameters(String text, List<Integer> categories, Boolean paid,
                                                      String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                      String sort, Integer from, Integer size) {
        LocalDateTime startDate = parseDateTime(rangeStart, "rangeStart");
        LocalDateTime endDate = parseDateTime(rangeEnd, "rangeEnd");
        validateDateTimeRange(startDate, endDate);

        if (startDate == null && endDate == null) {
            startDate = LocalDateTime.now();
        }

        Specification<Event> spec = buildSearchSpecification(text, categories, paid, startDate, endDate);
        List<Event> events = eventRepository.findAll(spec, Sort.by("eventDate"));

        if (events.isEmpty()) {
            return List.of();
        }

        getEventsStats(events);

        List<EventShortDto> eventDtos = events.stream()
                .filter(event -> !onlyAvailable || event.getParticipantLimit() == 0 ||
                        event.getCachedConfirmedRequests() < event.getParticipantLimit())
                .map(EventMapper::toShortDto)
                .toList();

        if ("VIEWS".equalsIgnoreCase(sort)) {
            eventDtos.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        }

        return eventDtos.stream()
                .skip(from)
                .limit(size)
                .toList();
    }

    @Override
    public Event findEventById(Integer eventId) {
        log.info("Поиск события по ID: {}", eventId);
        Event event = findEventOrThrow(eventId);
        getEventStats(event);
        log.info("Событие найдено: ID={}, просмотры={}", eventId, event.getCachedViews());
        return event;
    }

    @Override
    public EventFullDto updateEventByAdmin(Integer eventId, UpdateEventAdminRequest eventDto) {
        Event event = findEventOrThrow(eventId);

        if (eventDto.getEventDate() != null) {
            validateEventDate(eventDto.getEventDate(), "eventDate");
        }

        updateEventFields(event, eventDto);
        handleAdminStateAction(event, eventDto.getStateAction(), eventId);

        Event savedEvent = eventRepository.save(event);
        getEventStats(savedEvent);
        return EventMapper.toFullDto(savedEvent);
    }

    @Override
    public EventFullDto updateEventByUser(Integer eventId, @Validated UpdateEventUserRequest eventDto, Integer userId) {
        Event event = findEventOrThrow(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Пользователь не является инициатором события");
        }

        validateEventDate(event.getEventDate(), "eventDate");

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Недопустимый статус события для изменения");
        }

        updateEventFields(event, eventDto);
        handleUserStateAction(event, eventDto.getStateAction());

        Event savedEvent = eventRepository.save(event);
        getEventStats(savedEvent);
        return EventMapper.toFullDto(savedEvent);
    }

    private void updateEventFields(Event event, Object eventDto) {
        if (eventDto instanceof UpdateEventUserRequest userDto) {
            updateEventFromUserDto(event, userDto);
        } else if (eventDto instanceof UpdateEventAdminRequest adminDto) {
            updateEventFromAdminDto(event, adminDto);
        }
    }

    private void updateEventFromUserDto(Event event, UpdateEventUserRequest dto) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategory() != null) event.setCategory(categoryService.findById(dto.getCategory()));
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) {
            validateEventDate(dto.getEventDate(), "eventDate");
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLat(dto.getLocation().getLat());
            event.setLon(dto.getLocation().getLon());
        }
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
    }

    private void updateEventFromAdminDto(Event event, UpdateEventAdminRequest dto) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategory() != null) event.setCategory(categoryService.findById(dto.getCategory()));
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) {
            validateEventDate(dto.getEventDate(), "eventDate");
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLat(dto.getLocation().getLat());
            event.setLon(dto.getLocation().getLon());
        }
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
    }

    private void handleUserStateAction(Event event, EventUserStateAction stateAction) {
        if (stateAction != null) {
            if (stateAction == EventUserStateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            } else if (stateAction == EventUserStateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            }
        }
    }

    private void handleAdminStateAction(Event event, EventAdminStateAction stateAction, Integer eventId) {
        if (stateAction != null) {
            if (stateAction == EventAdminStateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Событие должно быть в состоянии ожидания публикации");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (stateAction == EventAdminStateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Нельзя удалить опубликованное событие");
                }
                event.setState(EventState.REJECTED);
            } else {
                throw new ValidationException("Указано непредусмотренное действие");
            }
        }
    }

    @Override
    public List<EventShortDto> findUserEvents(Integer userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findByInitiatorId(userId);
        getEventsStats(events);
        return events.stream()
                .skip(from)
                .limit(size)
                .map(EventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto findUserEventById(Integer eventId, Integer userId) {
        Event event = findEventOrThrow(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Пользователь не является инициатором события");
        }
        getEventStats(event);
        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, Integer userId) {
        validateEventDate(newEventDto.getEventDate(), "eventDate");

        User user = userService.findUserById(userId);
        Category category = categoryService.findById(newEventDto.getCategory());

        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);

        Event savedEvent = eventRepository.save(event);
        return EventMapper.toFullDto(savedEvent);
    }
}