package com.hoangtien2k3.notificationservice.api;

import com.hoangtien2k3.notificationservice.entity.Notification;
import com.hoangtien2k3.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "NotificationController", description = "Operations related to notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get all notifications", description = "Retrieve a list of all notifications")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of notifications",
            content = @Content(schema = @Schema(implementation = Notification.class)))
    })
    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @Operation(summary = "Get notification by ID", description = "Retrieve detailed information of a specific notification")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved notification",
            content = @Content(schema = @Schema(implementation = Notification.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new notification", description = "Create a new notification with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Notification created successfully",
            content = @Content(schema = @Schema(implementation = Notification.class))),
        @ApiResponse(responseCode = "400", description = "Invalid notification data")
    })
    @PostMapping
    public ResponseEntity<Notification> saveNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.saveNotification(notification);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete notification", description = "Delete a notification by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification deleted successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "Invalid notification ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotificationById(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}