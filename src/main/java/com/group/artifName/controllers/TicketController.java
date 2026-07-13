package com.group.artifName.controllers;

import com.group.artifName.dtos.AssignTicketDto;
import com.group.artifName.dtos.CreateTicketMessageRequest;
import com.group.artifName.dtos.TicketDto;
import com.group.artifName.dtos.UpdateTicketStatusDto;
import com.group.artifName.entities.Ticket;
import com.group.artifName.entities.TicketMessage;
import com.group.artifName.entities.User;
import com.group.artifName.repositories.UserRepository;
import com.group.artifName.services.AuthService;
import com.group.artifName.services.JwtService;
import com.group.artifName.services.TicketMessageService;
import com.group.artifName.services.TicketService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final JwtService jwtService; // 1. Inyectamos JwtService
    private final AuthService authService;
    private final TicketMessageService ticketMessageService ;

    public TicketController(TicketService ticketService,
                            UserRepository userRepository,
                            JwtService jwtService,
                            AuthService authService,
                            TicketMessageService ticketMessageService) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authService = authService;
        this.ticketMessageService = ticketMessageService;
    }



    // 1. CREAR TICKET
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TicketDto request, HttpServletRequest httpRequest) {
        try {
            User user = authService.getAuthenticatedUser(httpRequest); // Autenticación automática por cookie
            Ticket newTicket = ticketService.createTicket(request, user);
            return ResponseEntity.ok(newTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // 2. LISTAR TODOS LOS TICKETS SEGÚN USUARIO
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpRequest) {
        try {
            User user = authService.getAuthenticatedUser(httpRequest);
            List<Ticket> tickets = ticketService.getTicketsForUser(user);
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            Map<String,String> res = new HashMap<>();
            res.put("message",e.getMessage());

            return ResponseEntity.status(401).body(res);
        }
    }

    // 3. CAMBIAR ETAPA - SOLO ADMIN
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @Valid @RequestBody UpdateTicketStatusDto request,
                                          HttpServletRequest httpRequest) {
        try {
            User user = authService.getAuthenticatedUser(httpRequest);
            Ticket updatedTicket = ticketService.updateTicketStatus(id, request.getStatus(), user);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    //ASSIGN TICKET TO
    @PutMapping("/{id}/assigned")
    public ResponseEntity<?> assignticket(@PathVariable Long id,
                                          @Valid @RequestBody AssignTicketDto request,
                                          HttpServletRequest httpRequest) {
        try {
            User user = authService.getAuthenticatedUser(httpRequest);
            Ticket updatedTicket = ticketService.assignTicket(id, request.getId(), user);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            Map<String,String> res = new HashMap<>();
            res.put("message",e.getMessage());

            return ResponseEntity.status(401).body(res);
        }
    }

    //IN PROCESS TICKET
    @PutMapping("/{id}/inprocess")
    public ResponseEntity<?> assignticket(@PathVariable Long id,
                                          HttpServletRequest httpRequest) {
        try {
            User user = authService.getAuthenticatedUser(httpRequest);
            Ticket updatedTicket = ticketService.inProgressTicket(id, user);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            Map<String,String> res = new HashMap<>();
            res.put("message",e.getMessage());

            return ResponseEntity.status(401).body(res);
        }
    }
    // create message on ticket
    @PostMapping("/{ticketId}/messages")
    public ResponseEntity<?> createMessage(
            @PathVariable Long ticketId,
            @Valid @RequestBody CreateTicketMessageRequest request,
            HttpServletRequest httpRequest
    ) {
        try {
            User loggedUser = authService.getAuthenticatedUser(httpRequest);
            TicketMessage message = ticketMessageService.createMessage(
                    ticketId,
                    loggedUser,
                    request
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );

        }
    }
    // GEt TICKET MESSAGES
    @GetMapping("/{ticketId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable Long ticketId
    ) {
        try {
            return ResponseEntity.ok(
                    ticketMessageService.getMessagesByTicket(ticketId)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}