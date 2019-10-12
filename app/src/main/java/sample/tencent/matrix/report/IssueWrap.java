package sample.tencent.matrix.report;

import com.tencent.matrix.plugin.Plugin;
import com.tencent.matrix.report.Issue;

import org.json.JSONObject;

public class IssueWrap {

    public Integer type;
    public String tag;
    public String key;
    public JSONObject content;


    public IssueWrap(Issue issue) {
        this(issue.getType(), issue.getTag(), issue.getKey(), issue.getContent(), null);
    }

    public IssueWrap(Integer type, String tag, String key, JSONObject content, Plugin plugin) {
        this.type = type;
        this.tag = tag;
        this.key = key;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "IssueWrap{" +
                "type=" + type +
                ", tag='" + tag + '\'' +
                ", key='" + key + '\'' +
                ", content=" + content +
                '}';
    }
}
