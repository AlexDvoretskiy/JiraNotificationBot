package JiraNotificationBot.NotificationService;


import JiraNotificationBot.JiraService.JiraIssue;
import JiraNotificationBot.JiraService.JiraService;
import JiraNotificationBot.TelegramBot.TelegramBot;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class NotificationService {
	private static final Logger log = LogManager.getLogger(NotificationService.class);

	private static final String NEW_GROUP_ISSUES_NOTIFICATION = "На вашу группу назначено новых задач: %s \n";
	private static final String REMOVED_GROUP_ISSUES_NOTIFICATION = "Взято в работу задач группой: %s \n";

	private static final String NEW_PERSONAL_ISSUES_NOTIFICATION = "На вас назначено новых задач: %s \n";

	private TelegramBot telegramBot;
	private List<JiraIssue> groupCacheIssueList;
	private List<JiraIssue> personalCasheIssueList;

	public NotificationService(TelegramBot telegramBot) {
		this.telegramBot = telegramBot;
		groupCacheIssueList = new ArrayList<>();
		personalCasheIssueList = new ArrayList<>();
	}

	public void run() {
		JiraService.connect();

		while(true) {
			log.debug(String.format("Поток [%s] проснулся", Thread.currentThread().getName()));
			List<JiraIssue> groupIssueList = JiraService.getUpdatedIssueList(JiraService.GROUP_ISSUE_DEFAULT_QUERY);
			List<JiraIssue> personalIssueList = JiraService.getUpdatedIssueList(JiraService.PERSONAL_ISSUE_DEFAULT_QUERY);
			log.debug("[Группа] Размер groupIssueList: " + groupIssueList.size());
			log.debug("[Персональные] Размер personalIssueList: " + groupIssueList.size());

			checkGroupIssueListAndSendNotification(groupIssueList);
			checkPersonalIssueListAndSendNotification(personalIssueList);
			try {
				log.debug(String.format("Поток [%s] уснул", Thread.currentThread().getName()));
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private void checkPersonalIssueListAndSendNotification(List<JiraIssue> personalIssueList) {
		if (CollectionUtils.isNotEmpty(personalIssueList)) {
			List<JiraIssue> newIssueList = checkNewIssues(personalIssueList, personalCasheIssueList);

			if (CollectionUtils.isNotEmpty(newIssueList)) {
				updatePersonalIssueList(personalIssueList);

				String newIssuesMessage = String.format(NEW_PERSONAL_ISSUES_NOTIFICATION, newIssueList.size())
				                          + buildNotificationMessageForIssues(newIssueList);
				sendNotification(newIssuesMessage);
				log.info("[Персональные] Отправлен список новых задач: " + newIssueList.size());
			}
		}
	}

	private void checkGroupIssueListAndSendNotification(List<JiraIssue> groupIssueList) {
		if (CollectionUtils.isNotEmpty(groupIssueList)) {
			List<JiraIssue> newIssueList = checkNewIssues(groupIssueList, groupCacheIssueList);
			List<JiraIssue> removedIssueList = checkRemovedIssues(groupIssueList);

			if (CollectionUtils.isNotEmpty(newIssueList)) {
				updateGroupIssueList(groupIssueList);
				String newIssuesMessage = String.format(NEW_GROUP_ISSUES_NOTIFICATION, newIssueList.size())
				                          + buildNotificationMessageForIssues(newIssueList);
				sendNotification(newIssuesMessage);
				log.info("[Группа] Отправлен список новых задач: " + newIssueList.size());
			}
			if (CollectionUtils.isNotEmpty(removedIssueList)) {
				updateGroupIssueList(groupIssueList);
				String removedIssuesMessage = String.format(REMOVED_GROUP_ISSUES_NOTIFICATION, removedIssueList.size())
				                              + buildNotificationMessageForIssues(removedIssueList);
				sendNotification(removedIssuesMessage);
				log.info("[Группа] Отправлен список задач, взятых в работу: " + newIssueList.size());
			}
		}
	}

	private void updateGroupIssueList(List<JiraIssue> tempIssueList) {
		groupCacheIssueList.clear();
		groupCacheIssueList.addAll(tempIssueList);
	}

	private void updatePersonalIssueList(List<JiraIssue> tempIssueList) {
		personalCasheIssueList.clear();
		personalCasheIssueList.addAll(tempIssueList);
	}

	private void sendNotification(String message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(telegramBot.getChatId());
		sendMessage.setText(message);

		try {
			telegramBot.execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private List<JiraIssue> checkNewIssues(List<JiraIssue> tempIssueList, List<JiraIssue> mainIssueList) {
		List<JiraIssue> newIssueTempList = new ArrayList<>();

		for (JiraIssue tempIssue : tempIssueList) {
			if (!mainIssueList.contains(tempIssue)) {
				newIssueTempList.add(tempIssue);
			}
		}
		return newIssueTempList;
	}

	private List<JiraIssue> checkRemovedIssues(List<JiraIssue> tempIssueList) {
		List<JiraIssue> removedIssueTempList = new ArrayList<>();

		if (tempIssueList.size() < groupCacheIssueList.size()) {
			for (JiraIssue issue : groupCacheIssueList) {
				if (!tempIssueList.contains(issue)) {
					removedIssueTempList.add(issue);
				}
			}
		}
		return removedIssueTempList;
	}

	private String buildNotificationMessageForIssues(List<JiraIssue> issueList) {
		StringBuilder stringBuilder = new StringBuilder();

		for(int i = 1; i <= issueList.size(); i++) {
			stringBuilder.append(i).append(". ")
					.append(issueList.get(i - 1).getHyperlink())
					.append("\n");
		}
		return stringBuilder.toString();
	}
}
