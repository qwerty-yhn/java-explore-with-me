package ru.practicum.events.compilation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.events.compilation.dto.CompilationDto;
import ru.practicum.events.compilation.dto.NewCompilationDto;
import ru.practicum.events.compilation.dto.UpdateCompilationRequest;
import ru.practicum.events.compilation.mapper.CompilationMapper;
import ru.practicum.events.compilation.model.Compilation;
import ru.practicum.events.compilation.service.CompilationServiceAdmin;
import ru.practicum.events.compilation.storage.CompilationStorage;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.util.FindObjectInRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationStorage compilationStorage;
    private final FindObjectInRepository findObjectInRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceAdminImpl(CompilationStorage compilationStorage,
                                       FindObjectInRepository findObjectInRepository,
                                       EventRepository eventRepository) {
        this.compilationStorage = compilationStorage;
        this.findObjectInRepository = findObjectInRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = addEvents(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.newCompilationDtoToCompilationAndEvents(newCompilationDto, events);
        return CompilationMapper.compilationToCompilationDto(compilationStorage.save(compilation));
    }

    @Override
    public void deleteCompilationById(Long compId) {
        findObjectInRepository.getCompilationById(compId);
        compilationStorage.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation newCompilation = findObjectInRepository.getCompilationById(compId);
        Set<Event> events;
        if (updateCompilationRequest.getEvents() != null) {
            events = addEvents(updateCompilationRequest.getEvents());
            newCompilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            newCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && updateCompilationRequest.getTitle().isBlank()) {
            newCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        return CompilationMapper.compilationToCompilationDto(compilationStorage.save(newCompilation));
    }

    private Set<Event> addEvents(List<Long> eventsIds) {
        return eventRepository.findAllByIdIsIn(eventsIds);
    }
}
