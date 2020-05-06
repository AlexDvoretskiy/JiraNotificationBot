package JiraNotificationBot;

import JiraNotificationBot.TelegramBot.TelegramBot;
import JiraNotificationBot.TelegramBot.properties.BotConfiguration;
import JiraNotificationBot.TelegramBot.properties.ConfigurationParser;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;


public class Launcher {
	private static final Logger log = LogManager.getLogger(Launcher.class);

	public static void main(String[] args) {
		try {
			log.info("Инициализация API");
			ApiContextInitializer.init();
			TelegramBotsApi botsApi = new TelegramBotsApi();

			log.info("Создание конфигурационных настроек для http подключения");
			ConfigurationParser.parseConfigurations();
			DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			BotConfiguration botConfiguration = BotConfiguration.getInstance();
			credsProvider.setCredentials(
					new AuthScope(botConfiguration.getProxyHost(), botConfiguration.getProxyPort()),
					new UsernamePasswordCredentials(botConfiguration.getProxyUser(), botConfiguration.getProxyPassword()));

			HttpHost httpHost = new HttpHost(botConfiguration.getProxyHost(), botConfiguration.getProxyPort());
			RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(true).build();
			botOptions.setRequestConfig(requestConfig);
			botOptions.setCredentialsProvider(credsProvider);
			botOptions.setHttpProxy(httpHost);

			TelegramBot bot = new TelegramBot(botConfiguration, botOptions);

			log.info(String.format("Регистрация телеграм бота [%s]", botConfiguration.getBotUsername()));
			botsApi.registerBot(bot);
			log.info(String.format("Телеграм бот [%s] успешно запущен", botConfiguration.getBotUsername()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

