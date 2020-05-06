package JiraNotificationBot.JiraService;

import JiraNotificationBot.TelegramBot.properties.BotConfiguration;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.util.concurrent.Promise;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JiraService {
    private static JiraClient jiraClient;

    public static final String GROUP_ISSUE_DEFAULT_QUERY = "assignee in (prptspuio, sp.dev.ufos)";
    public static final String PERSONAL_ISSUE_DEFAULT_QUERY = "status in (Open, \"In Progress\", Reopened, Design, \"In Development\", Analysis, Paused, \"For review\", Approving, Formed, \"Information Required\") AND assignee in (currentUser())";

    public static void connect() {
        BotConfiguration botConfiguration = BotConfiguration.getInstance();
        jiraClient = new JiraClient(botConfiguration.getJiraUsername(), botConfiguration.getJiraPassword(), botConfiguration.getJiraUrl());
    }

    public static List<JiraIssue> getUpdatedIssueList(String query) {
        List<JiraIssue> issueList = new ArrayList<>();

        try {
            JiraRestClient restClient = jiraClient.getRestClient();
            SearchRestClient searchRestClient = restClient.getSearchClient();
            Promise<SearchResult> searchResult = searchRestClient.searchJql(query, 500, 0,null);

            for (Issue issue : searchResult.claim().getIssues()) {
                JiraIssue jiraIssue = new JiraIssue(issue);
                issueList.add(jiraIssue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issueList;
    }
}
