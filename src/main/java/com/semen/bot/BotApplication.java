package com.semen.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BotApplication {

	@Value("${discord.bot.token}")
	private String token;

	@Autowired
	private Bot bot;

	public static void main(String[] args) {SpringApplication.run(BotApplication.class, args);}

	@Bean
	public JDA jda() throws Exception {
		return JDABuilder.createDefault(token)
				.addEventListeners(bot)
				.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.watching("on birds"))
				.build();
	}
}


