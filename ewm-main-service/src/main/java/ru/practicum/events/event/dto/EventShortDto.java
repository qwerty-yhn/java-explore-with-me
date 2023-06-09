package ru.practicum.events.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Value
@Builder
public class EventShortDto {
    String annotation; // example: Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории
    CategoryDto category;
    Long confirmedRequests; // Количество одобренных заявок на участие в данном событии
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    Long id;
    UserShortDto initiator; //Пользователь (краткая информация)
    boolean paid; // Нужно ли оплачивать участие
    String title; // example: Знаменитое шоу 'Летающая кукуруза'
    Long views; // Количество просмотрев события

}
