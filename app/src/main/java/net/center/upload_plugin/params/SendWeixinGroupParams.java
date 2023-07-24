package net.center.upload_plugin.params;

import com.android.tools.r8.u.b.S;

import org.gradle.api.Project;

import java.util.List;

/**
 * Created by Android-ZX
 * <p>
 * 发送到企业微信群的消息参数
 */
public class SendWeixinGroupParams {

    public String webHookUrl;
    public String msgtype = "markdown";
    /**
     * 如果使用文本可添加参数是否@全体群人员，默认true：isAtAll = true。其他类型不支持
     */
    public boolean isAtAll = false;
    public String contentTitle;
    public String contentText;
    public String mentionedList;

    /**
     * 是否支持发送git记录
     */
    public boolean isSupportGitLog = true;

    public SendWeixinGroupParams() {

    }

    public SendWeixinGroupParams(String webHookUrl, String msgtype, boolean isAtAll, String contentTitle, String contentText,String mentionedList) {
        this.webHookUrl = webHookUrl;
        this.msgtype = msgtype;
        this.isAtAll = isAtAll;
        this.contentText = contentText;
        this.contentTitle = contentTitle;
        this.mentionedList = mentionedList;
    }

    public static SendWeixinGroupParams getWeixinGroupConfig(Project project) {
        SendWeixinGroupParams extension = project.getExtensions().findByType(SendWeixinGroupParams.class);
        if (extension == null) {
            extension = new SendWeixinGroupParams();
        }
        return extension;
    }

}
