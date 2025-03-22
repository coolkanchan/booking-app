package edu.miu.cs.cs425.backend.presentation.controller;

import edu.miu.cs.cs425.backend.domain.entity.Flight;
import edu.miu.cs.cs425.backend.domain.entity.FlightSearchResult;
import edu.miu.cs.cs425.backend.application.query.FlightRouteSearchQuery;
import edu.miu.cs.cs425.backend.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {
    
    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setId("F123");
        flight.setFlightNumber("FL1001");
    }

    @Test
    void testCreateFlight() {
        when(flightService.createFlight(any(Flight.class))).thenReturn(flight);
        ResponseEntity<Flight> response = flightController.createFlight(flight);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(flight.getId(), response.getBody().getId());
    }

    @Test
    void testFindFlightById_Found() {
        when(flightService.findFlightById("F123")).thenReturn(Optional.of(flight));
        ResponseEntity<Flight> response = flightController.findFlightById("F123");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("F123", response.getBody().getId());
    }

    @Test
    void testFindFlightById_NotFound() {
        when(flightService.findFlightById("F999")).thenReturn(Optional.empty());
        ResponseEntity<Flight> response = flightController.findFlightById("F999");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindAllFlights() {
        List<Flight> flights = Arrays.asList(flight);
        when(flightService.findAllFlights()).thenReturn(flights);
        ResponseEntity<List<Flight>> response = flightController.findAllFlights();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdateFlight_Success() {
        when(flightService.updateFlight(eq("F123"), any(Flight.class))).thenReturn(flight);
        ResponseEntity<Flight> response = flightController.updateFlight("F123", flight);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateFlight_NotFound() {
        when(flightService.updateFlight(eq("F999"), any(Flight.class))).thenThrow(new IllegalArgumentException());
        ResponseEntity<Flight> response = flightController.updateFlight("F999", flight);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteFlight_Success() {
        doNothing().when(flightService).deleteFlight("F123");
        ResponseEntity<Void> response = flightController.deleteFlight("F123");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteFlight_NotFound() {
        doThrow(new IllegalArgumentException()).when(flightService).deleteFlight("F999");
        ResponseEntity<Void> response = flightController.deleteFlight("F999");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindFlightsByRouteAndDate_Success() {
        FlightRouteSearchQuery query = new FlightRouteSearchQuery();
        FlightSearchResult result = new FlightSearchResult();
        when(flightService.findFlightsByRoute(query)).thenReturn(result);
        ResponseEntity<FlightSearchResult> response = flightController.findFlightsByRouteAndDate(query);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testFindFlightsByAirline() {
        List<Flight> flights = Arrays.asList(flight);
        when(flightService.findFlightsByAirline("AA", "cheapest")).thenReturn(flights);
        ResponseEntity<List<Flight>> response = flightController.findFlightsByAirline("AA", "cheapest");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}
