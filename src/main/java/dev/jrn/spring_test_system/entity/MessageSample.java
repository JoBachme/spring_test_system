package dev.jrn.spring_test_system.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_sample")
public class MessageSample {

    @Id
    @Column(name = "notification_name")
    private String notificationName;

    @Column(name = "text_sample")
    private String textSample;

    public MessageSample() {
    }

    public MessageSample(String notificationName, String textSample) {
        this.notificationName = notificationName;
        this.textSample = textSample;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    @Override
    public String toString() {
        return "MessageSample [notificationName=" + notificationName + ", textSample=" + textSample + "]";
    }

    public String getTextSample() {
        return textSample;
    }

    public void setTextSample(String textSample) {
        this.textSample = textSample;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationName, textSample);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MessageSample other))
            return false;
        return Objects.equals(notificationName, other.notificationName)
                && Objects.equals(textSample, other.textSample);
    }
}
