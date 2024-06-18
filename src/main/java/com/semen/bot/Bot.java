package com.semen.bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class Bot extends ListenerAdapter{
    @Autowired
    private YandexGpt yandexGpt;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()){
            return;
        }
        if (!event.getMessage().getContentDisplay().startsWith("!ask")){
            return;
        }

        String response = yandexGpt.getResponse(event.getMessage().getContentDisplay());
        event.getChannel().sendMessage(response).queue();
    }
}
