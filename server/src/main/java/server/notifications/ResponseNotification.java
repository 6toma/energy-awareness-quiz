package server.notifications;

/**
 * Response message class that store the message to be sent to the user
 */
public class ResponseNotification {
    private String messageContent;

    public ResponseNotification() {
    }

    public ResponseNotification(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
