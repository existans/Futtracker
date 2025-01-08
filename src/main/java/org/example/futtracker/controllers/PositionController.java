package org.example.futtracker.controllers;

import org.example.futtracker.models.Position;
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
@RequestMapping("/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listPositions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("positions", positionService.getAllPositions());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "positions";
    }

    @GetMapping("/add")
    public String addPositionForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("position", new Position());
        return "add-position";
    }

    @PostMapping("/add")
    public String addPosition(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @ModelAttribute("position") Position position,
                              BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "add-position";
        }
        positionService.savePosition(position);
        return "redirect:/positions";
    }

    @GetMapping("/edit/{id}")
    public String editPositionForm(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("id") Integer id,
                                   Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Position position = positionService.findPositionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
        model.addAttribute("position", position);
        return "edit-position";
    }

    @PostMapping("/update/{id}")
    public String updatePosition(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("position") Position position,
                                 BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "edit-position";
        }
        position.setId(id);
        positionService.updatePosition(position);
        return "redirect:/positions";
    }

    @GetMapping("/delete/{id}")
    public String deletePosition(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        positionService.deletePosition(id);
        return "redirect:/positions";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
