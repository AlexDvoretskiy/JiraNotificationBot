package JiraNotificationBot.JiraService;

import com.atlassian.jira.rest.client.api.domain.Issue;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
public class JiraIssue {
    private String label;
    private String hyperlink;
    private String createInfo;
    private String noteSummary;
    private String assigneeInfo;
    private Long priorityID;
    private String priority;
    private String status;
    private Long statusID;
    private String issueType;
    private Long issueTypeID;

    private static final String MAIN_LINK = "https://job-jira.otr.ru/browse/";

    public JiraIssue(Issue issue){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formatDate = dateFormat.format(issue.getCreationDate().toDate());

        label = issue.getKey();
        hyperlink = MAIN_LINK + label;
        createInfo = issue.getReporter().getDisplayName() + "\n" + formatDate;
        noteSummary = issue.getSummary();
        assigneeInfo = issue.getAssignee().getDisplayName();
        priority = issue.getPriority().getName();
        priorityID = issue.getPriority().getId();
        status = issue.getStatus().getName();
        statusID = issue.getStatus().getId();
        issueType = issue.getIssueType().getName();
        issueTypeID = issue.getIssueType().getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        JiraIssue jiraIssue = (JiraIssue) obj;

        return jiraIssue.getLabel().equals(this.label);
    }
}
