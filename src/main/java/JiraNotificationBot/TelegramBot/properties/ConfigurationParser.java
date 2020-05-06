package JiraNotificationBot.TelegramBot.properties;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static JiraNotificationBot.TelegramBot.properties.BotConfiguration.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationParser {
	private static final String CONFIG_PATH = "src/main/resources/telegramBot.yml";

	public static void parseConfigurations() {
		Yaml yaml = new Yaml();

		InputStream input = null;
		try {
			input = new FileInputStream(new File(CONFIG_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Map config = yaml.load(input);
		Map proxyConfig = (Map) config.get(PROXY_KEY);
		Map botConfig = (Map) config.get(BOT_KEY);
		Map jiraConfig = (Map) config.get(JIRA_KEY);

		BotConfiguration botConfiguration = BotConfiguration.getInstance();
		botConfiguration.setProxyHost(String.valueOf(proxyConfig.get(PROXY_HOST_KEY)));
		botConfiguration.setProxyPort((Integer) proxyConfig.get(PROXY_PORT_KEY));
		botConfiguration.setProxyUser(String.valueOf(proxyConfig.get(PROXY_USER_KEY)));
		botConfiguration.setProxyPassword(String.valueOf(proxyConfig.get(PROXY_PASSWORD_KEY)));
		botConfiguration.setBotUsername(String.valueOf(botConfig.get(BOT_USERNAME)));
		botConfiguration.setBotToken(String.valueOf(botConfig.get(BOT_TOKEN)));
		botConfiguration.setJiraUsername(String.valueOf(jiraConfig.get(JIRA_USERNAME_KEY)));
		botConfiguration.setJiraPassword(String.valueOf(jiraConfig.get(JIRA_PASSWORD_KEY)));
		botConfiguration.setJiraUrl(String.valueOf(jiraConfig.get(JIRA_URL_KEY)));
	}
}
