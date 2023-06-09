package ru.practicum.events.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.events.event.dto.EventFullDto;
import ru.practicum.events.event.dto.EventShortDto;
import ru.practicum.events.event.dto.NewEventDto;
import ru.practicum.events.event.dto.stateDto.EventStateDto;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.model.EventState;
import ru.practicum.exception.BadRequestException;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.util.util.DateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class EventMapper {

    public EventShortDto eventToeventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .location(LocationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState().name())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event newEventDtoToCreateEvent(NewEventDto newEventDto, User user, Category category, Long views,
                                                 Long confirmedRequests) {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .createdOn(dateTime)
                .description(newEventDto.getDescription())
                .eventDate(DateFormatter.formatDate(newEventDto.getEventDate()))
                .initiator(user)
                .location(LocationMapper.locationDtoToLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid() == null ? false : newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration() == null ? true : newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(views)
                .build();
    }

    public static EventState eventStateDtoToEventState(EventStateDto state) {
        if (state.name().equals(EventStateDto.PENDING.name())) {
            return EventState.PENDING;
        }
        if (state.name().equals(EventStateDto.CANCELED.name())) {
            return EventState.CANCELED;
        }
        if (state.name().equals(EventStateDto.PUBLISHED.name())) {
            return EventState.PUBLISHED;
        }
        throw new BadRequestException("Нет такого статуса" + state.name());
    }
}
