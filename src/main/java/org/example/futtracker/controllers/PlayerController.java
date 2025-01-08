package org.example.futtracker.controllers;

import org.example.futtracker.models.Player;
import org.example.futtracker.services.PlayerService;
import org.example.futtracker.services.TeamService;
import org.example.futtracker.services.PositionService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PositionService positionService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listPlayers(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("players", playerService.getAllPlayers());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "players";
    }

    @GetMapping("/add")
    public String addPlayerForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("positions", positionService.getAllPositions());
        return "add-player";
    }

    @PostMapping("/add")
    public String addPlayer(@AuthenticationPrincipal UserDetails userDetails,
                            @Valid @ModelAttribute("player") Player player,
                            BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "add-player";
        }
        playerService.savePlayer(player);
        return "redirect:/players";
    }

    @GetMapping("/edit/{id}")
    public String editPlayerForm(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("id") Integer id,
                                 Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Player player = playerService.findPlayerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        model.addAttribute("player", player);
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("positions", positionService.getAllPositions());
        return "edit-player";
    }

    @PostMapping("/update/{id}")
    public String updatePlayer(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id,
                               @Valid @ModelAttribute("player") Player player,
                               BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "edit-player";
        }
        player.setId(id);
        playerService.updatePlayer(player);
        return "redirect:/players";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        playerService.deletePlayer(id);
        return "redirect:/players";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
