package JiraNotificationBot.TelegramBot.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotConfiguration {

	public static final String PROXY_KEY = "proxy";
	public static final String BOT_KEY = "telegramBot";
	public static final String JIRA_KEY = "jira";

	public static final String PROXY_HOST_KEY = "host";
	public static final String PROXY_PORT_KEY = "port";
	public static final String PROXY_USER_KEY = "user";
	public static final String PROXY_PASSWORD_KEY = "password";

	public static final String BOT_USERNAME = "username";
	public static final String BOT_TOKEN = "token";

	public static final String JIRA_USERNAME_KEY = "username";
	public static final String JIRA_PASSWORD_KEY = "password";
	public static final String JIRA_URL_KEY = "url";

	private String proxyHost;
	private int proxyPort;
	private String proxyUser;
	private String proxyPassword;
	private String botUsername;
	private String botToken;
	private String jiraUsername;
	private String jiraPassword;
	private String jiraUrl;

	private static volatile BotConfiguration instance;

	public static BotConfiguration getInstance() {
		if (instance == null) {
			synchronized (BotConfiguration.class) {
				if (instance == null) {
					instance = new BotConfiguration();
				}
			}
		}
		return instance;
	}

}
