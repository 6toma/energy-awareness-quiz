package server.notifications;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class NotificationController {

    @MessageMapping("/notification")
    @SendTo("/topic/notifications")
    public ResponseNotification getNotification(final Notification notification) {
        return new ResponseNotification(HtmlUtils.htmlEscape(notification.getMessageContent()));
    }
}

