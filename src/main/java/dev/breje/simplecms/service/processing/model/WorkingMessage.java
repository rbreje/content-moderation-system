package dev.breje.simplecms.service.processing.model;

import java.util.Objects;

public class WorkingMessage {

    private String userId;
    private String originalMessage;
    private String translatedMessage;
    private float score;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public String getTranslatedMessage() {
        return translatedMessage;
    }

    public void setTranslatedMessage(String translatedMessage) {
        this.translatedMessage = translatedMessage;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WorkingMessage that = (WorkingMessage) o;
        return Float.compare(score, that.score) == 0 && Objects.equals(userId, that.userId) && Objects.equals(originalMessage, that.originalMessage) && Objects.equals(translatedMessage, that.translatedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, originalMessage, translatedMessage, score);
    }
}
