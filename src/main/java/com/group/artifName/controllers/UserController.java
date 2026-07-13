package com.group.artifName.controllers;
import com.group.artifName.services.*;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.group.artifName.dtos.AssignTicketDto;
import com.group.artifName.dtos.CreateTicketMessageRequest;
import com.group.artifName.dtos.TicketDto;
import com.group.artifName.dtos.UpdateTicketStatusDto;
import com.group.artifName.entities.Ticket;
import com.group.artifName.entities.TicketMessage;
import com.group.artifName.entities.User;
import com.group.artifName.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController  {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final JwtService jwtService; // 1. Inyectamos JwtService
    private final AuthService authService;
    private final TicketMessageService ticketMessageService ;
    private final UserService userService ;

    public UserController(TicketService ticketService,
                          UserRepository userRepository,
                          JwtService jwtService,
                          AuthService authService,
                          TicketMessageService ticketMessageService,
                          UserService userService
    ) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authService = authService;
        this.ticketMessageService = ticketMessageService;
        this.userService = userService;
    }

    // GEt TICKET MESSAGES
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            Map<String,String> res = new HashMap<>();
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id_req}/makeadmin")
    public ResponseEntity<?> makeAdmin(@PathVariable String id_req, HttpServletRequest request) {
        try {
            Long id = Long.parseLong(id_req);
            User loggedUser = authService.getAuthenticatedUser(request); // Autenticación automática por cookie
            if (!userService.isAdmin(loggedUser)){
                Map<String,String> err = new HashMap<>();
                err.put("error","not admin");
                err.put("message","Solo administradores pueden registrar usuarios");
                return ResponseEntity.badRequest().body(err);
            }

            return ResponseEntity.ok(userService.makeAdmin(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}/quitadmin")
    public ResponseEntity<?> quitAdmin(@PathVariable Long id, HttpServletRequest request) {
        try {
            User loggedUser = authService.getAuthenticatedUser(request); // Autenticación automática por cookie
            if (!userService.isAdmin(loggedUser)){
                Map<String,String> err = new HashMap<>();
                err.put("error","not admin");
                err.put("message","Solo administradores pueden registrar usuarios");
                return ResponseEntity.badRequest().body(err);
            } else if (loggedUser.getId()==id) {
                Map<String,String> err = new HashMap<>();
                err.put("message","No puedes quitarte permisos a ti mismo");
                return ResponseEntity.badRequest().body(err);
            }
            return ResponseEntity.ok(userService.quitAdmin(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

}