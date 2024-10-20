package com.avengers.yoribogo.notification.notification.contoller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.notification.domain.NotificationEntity;
import com.avengers.yoribogo.notification.notification.dto.NotificationDTO;
import com.avengers.yoribogo.notification.notification.dto.NotificationStatusUpdateRequestDTO;
import com.avengers.yoribogo.notification.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // SSE 연결 엔드포인트
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/sseConnect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToNotifications() {
        log.info("(controller)SSE 연결 지나갑니당 ~~!");
        return notificationService.subscribe();  // SSE 연결을 서비스로 위임
    }

    // 알림 저장 테스트 API
    @PostMapping("/insertTest")
    public ResponseEntity<NotificationEntity> createNotification() {
        // 필요한 데이터는 service에서 직접 설정
        NotificationEntity savedNotification = notificationService.createNotification();
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    // 사용자에게 알림을 전송하고, 전송된 알림 데이터를 Response로 반환
    @GetMapping("/send/{userId}")
    public ResponseEntity<ResponseDTO<List<NotificationDTO>>> sendUserNotifications(@PathVariable("userId") Long userId) {
        List<NotificationDTO> userNotifications = notificationService.sendNotificationsToUser(userId);
        if (userNotifications.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_NOTIFICATION);
        } else {
            return ResponseEntity.ok(ResponseDTO.ok(userNotifications));
        }
    }


    @PutMapping("/updateStatus/{notificationId}")
    public ResponseEntity<Void> updateNotificationStatus(
            @PathVariable(name = "notificationId") Long notificationId,
            @RequestBody NotificationDTO notificationDTO) {

        // Enum 타입으로 바로 Service 메서드에 전달
        notificationService.updateNotificationStatus(notificationId, notificationDTO.getNotificationStatus());

        return ResponseEntity.ok().build();
    }
}
