package com.codingshuttle.Skillora.connections_service.repository;


import com.codingshuttle.Skillora.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person,Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (PersonA:Person) -[:CONNECTED_TO]- (PersonB:Person) " +
            "WHERE PersonA.userId=$userId " +
            "RETURN DISTINCT PersonB;")
    List<Person> getFirstDegreeConnections(Long userId);
}
