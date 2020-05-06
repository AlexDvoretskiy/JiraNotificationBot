package JiraNotificationBot.TelegramBot.properties;


import lombok.Getter;
import lombok.Setter;


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


	@Getter
	@Setter
	private static String proxyHost;
	@Getter
	@Setter
	private static int proxyPort;
	@Getter
	@Setter
	private static String proxyUser;
	@Getter
	@Setter
	private static String proxyPassword;

	@Getter
	@Setter
	private static String botUsername;
	@Getter
	@Setter
	private static String botToken;

	@Getter
	@Setter
	private static String jiraUsername;
	@Getter
	@Setter
	private static String jiraPassword;
	@Getter
	@Setter
	private static String jiraUrl;
}
