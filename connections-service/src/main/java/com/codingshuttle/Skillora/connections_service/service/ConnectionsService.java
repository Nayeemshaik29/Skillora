package com.codingshuttle.Skillora.connections_service.service;


//import com.codingshuttle.Skillora.connections_service.auth.UserContextHolder;
import com.codingshuttle.Skillora.connections_service.entity.Person;
import com.codingshuttle.Skillora.connections_service.repository.PersonRepository;
import com.codingshuttle.skillora.posts_service.auth.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree  connections for userId {}", userId);
        return personRepository.getFirstDegreeConnections(1L);

    }
}
