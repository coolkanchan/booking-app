package edu.miu.cs.cs425.backend.presentation.controller;

import edu.miu.cs.cs425.backend.domain.entity.Airport;
import edu.miu.cs.cs425.backend.service.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportControllerTest {

    @Mock
    private AirportService airportService;

    @InjectMocks
    private AirportController airportController;

    private Airport testAirport;

    @BeforeEach
    void setUp() {
        testAirport = new Airport();
        testAirport.setIataCode("JFK");
        testAirport.setName("John F. Kennedy International Airport");
        testAirport.setCity("New York");
        testAirport.setCountry("USA");
    }

    @Test
    void testCreateAirport() {
        when(airportService.createAirport(any(Airport.class))).thenReturn(testAirport);
        ResponseEntity<Airport> response = airportController.createAirport(testAirport);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testAirport, response.getBody());
    }

    @Test
    void testFindAirportById_Found() {
        when(airportService.findAirportById("JFK")).thenReturn(Optional.of(testAirport));
        ResponseEntity<Airport> response = airportController.findAirportById("JFK");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testAirport, response.getBody());
    }

    @Test
    void testFindAirportById_NotFound() {
        when(airportService.findAirportById("JFK")).thenReturn(Optional.empty());
        ResponseEntity<Airport> response = airportController.findAirportById("JFK");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindAllAirports() {
        List<Airport> airportList = Arrays.asList(testAirport);
        when(airportService.findAllAirports()).thenReturn(airportList);
        ResponseEntity<List<Airport>> response = airportController.findAllAirports();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(airportList, response.getBody());
    }

    @Test
    void testUpdateAirport_Success() {
        when(airportService.updateAirport(eq("JFK"), any(Airport.class))).thenReturn(testAirport);
        ResponseEntity<Airport> response = airportController.updateAirport("JFK", testAirport);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testAirport, response.getBody());
    }

    @Test
    void testUpdateAirport_NotFound() {
        when(airportService.updateAirport(eq("JFK"), any(Airport.class))).thenThrow(new IllegalArgumentException("Airport not found"));
        ResponseEntity<Airport> response = airportController.updateAirport("JFK", testAirport);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteAirport_Success() {
        doNothing().when(airportService).deleteAirport("JFK");
        ResponseEntity<Void> response = airportController.deleteAirport("JFK");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteAirport_NotFound() {
        doThrow(new IllegalArgumentException("Airport not found"))
                .when(airportService).deleteAirport("JFK");
        ResponseEntity<Void> response = airportController.deleteAirport("JFK");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindAirportsByCity() {
        List<Airport> airports = Arrays.asList(testAirport);
        when(airportService.findAirportsByCity("New York")).thenReturn(airports);
        ResponseEntity<List<Airport>> response = airportController.findAirportsByCity("New York");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(airports, response.getBody());
    }

    @Test
    void testFindAirportsByCountry() {
        List<Airport> airports = Arrays.asList(testAirport);
        when(airportService.findAirportsByCountry("USA")).thenReturn(airports);
        ResponseEntity<List<Airport>> response = airportController.findAirportsByCountry("USA");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(airports, response.getBody());
    }
}
