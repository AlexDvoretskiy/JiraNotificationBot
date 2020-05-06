package JiraNotificationBot.JiraService;

import JiraNotificationBot.TelegramBot.properties.BotConfiguration;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.util.concurrent.Promise;

import java.util.ArrayList;
import java.util.List;

public class JiraService {
    private static JiraClient jiraClient;
    private static JiraRestClient restClient;


    public final static String GROUP_ISSUE_DEFAULT_QUERY = "assignee in (prptspuio, sp.dev.ufos)";
    public final static String PERSONAL_ISSUE_DEFAULT_QUERY = "status in (Open, \"In Progress\", Reopened, Design, \"In Development\", Analysis, Paused, \"For review\", Approving, Formed, \"Information Required\") AND assignee in (currentUser())";

    public static void connect() {
        jiraClient = new JiraClient(BotConfiguration.getJiraUsername(), BotConfiguration.getJiraPassword(), BotConfiguration.getJiraUrl());
    }

    public static List<JiraIssue> getUpdatedIssueList(String query){
        List<JiraIssue> issueList = new ArrayList<>();

        try {
            restClient = jiraClient.getRestClient();
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
