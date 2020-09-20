package ru.jurfed.presentssystem.Dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.jurfed.presentssystem.domain.Message;

import java.util.List;

public class MessageDto {

    private List<Message> messages;

    public MessageDto() {
    }

    public MessageDto(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

}
