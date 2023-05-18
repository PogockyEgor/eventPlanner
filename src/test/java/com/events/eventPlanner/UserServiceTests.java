package com.events.eventPlanner;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.PlaceDbDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.EventRepository;
import com.events.eventPlanner.repository.UserRepository;
import com.events.eventPlanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

    @Mock
    private UserRepository MockUserRepository;
    @Mock
    private UserService MockUserService;
    @Mock
    private EventRepository MockEventRepository;
    @Mock
    private DtoMapper dtoMapper;
    @InjectMocks
    private UserService userService;
    private int id;
    private float fl;
    private User user;
    private Event event;
    private Place place;
    private EventDbDto eventDbDto;
    private PlaceDbDto placeDbDto;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<EventDbDto> eventsDbDto = new ArrayList<>();
    private UserResponseDto userResponseDto;
    private ArrayList<UserResponseDto> userResponseDtoList = new ArrayList<>();

    @BeforeEach
    public void init() {
        id = 1;
        fl = 1;
        String string = "test";
        Date date = new Date(11 - 12 - 2013);
        user = new User(id, string, string, string, date, date, false, date, string, "admin");
        users.add(user);
        place = new Place(id, string, string, string, string, fl, null);
        placeDbDto = new PlaceDbDto(id, string, string, string, string, fl, null);
        event = new Event(id, string, date, string, place);
        events.add(event);
        eventDbDto = new EventDbDto(id, string, date, string, placeDbDto);
        eventsDbDto.add(eventDbDto);
        userResponseDto = new UserResponseDto(string, date, date, string);
        userResponseDtoList.add(userResponseDto);
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "admin")
    public void getUserByIdTest() {
        when(MockUserRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        when(MockUserRepository.findByLogin(any())).thenReturn(Optional.ofNullable(user));
        UserResponseDto returnedUser = userService.getUserById(id);
        verify(MockUserRepository).findById(id);
        verify(MockUserRepository).findByLogin(any());
        assertEquals(userResponseDto, returnedUser);
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "admin")
    public void getAllUsers() {
        when(MockUserRepository.findAll()).thenReturn(users);
        ArrayList<UserResponseDto> returnedUsers = userService.getAllUsers();
        assertEquals(userResponseDtoList, returnedUsers);
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "admin")
    public void getEventsForUser() {
        when(MockUserRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        when(MockUserRepository.findByLogin(any())).thenReturn(Optional.ofNullable(user));
        when(MockEventRepository.getAllEventsForUser(id)).thenReturn(eventsDbDto);
        ArrayList<Event> returnedEvents = userService.getAllEventsForUser(id);
        assertEquals(returnedEvents, events);
    }
}