package org.example.futtracker.controllers;

import org.example.futtracker.models.Team;
import org.example.futtracker.services.TeamService;

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
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listTeams(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "teams";
    }

    @GetMapping("/add")
    public String addTeamForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("team", new Team());
        return "add-team";
    }

    @PostMapping("/add")
    public String addTeam(@AuthenticationPrincipal UserDetails userDetails,
                          @Valid @ModelAttribute("team") Team team,
                          BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "add-team";
        }
        teamService.saveTeam(team);
        return "redirect:/teams";
    }

    @GetMapping("/edit/{id}")
    public String editTeamForm(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id,
                               Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Team team = teamService.findTeamById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        model.addAttribute("team", team);
        return "edit-team";
    }

    @PostMapping("/update/{id}")
    public String updateTeam(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id,
                             @Valid @ModelAttribute("team") Team team,
                             BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "edit-team";
        }
        team.setId(id);
        teamService.updateTeam(team);
        return "redirect:/teams";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeam(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        teamService.deleteTeam(id);
        return "redirect:/teams";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
