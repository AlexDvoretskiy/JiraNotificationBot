package JiraNotificationBot.TelegramBot;


import JiraNotificationBot.NotificationService.NotificationService;
import JiraNotificationBot.TelegramBot.properties.BotConfiguration;
import lombok.Getter;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;


public class TelegramBot extends AbilityBot {
	@Getter
	private Long chatId;
	private boolean isNotAuthorized = true;

	public TelegramBot(BotConfiguration botConfiguration, DefaultBotOptions botOptions) {
		super(botConfiguration.getBotToken(), botConfiguration.getBotUsername(), botOptions);
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && isNotAuthorized) {
			Message message = update.getMessage();

			if (message.getText().equals("/start")) {
				chatId = message.getChatId();
				isNotAuthorized = false;

				NotificationService notificationService = new NotificationService(this);
				notificationService.run();
			}
		}

	}

	@Override
	public int creatorId() {
		return 0;
	}
}
