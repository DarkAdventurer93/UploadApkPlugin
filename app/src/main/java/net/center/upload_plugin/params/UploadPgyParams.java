package net.center.upload_plugin.params;

import net.center.upload_plugin.PluginUtils;
import net.center.upload_plugin.helper.FileIOUtils;

import org.gradle.api.Project;

/**
 * Created by Android-ZX
 * 2021/9/3.
 * <p>
 * 蒲公英上传参数设置
 */
public class UploadPgyParams {

    //(必填) API Key 点击获取_api_key
    public String apiKey;
    public String appName;
    public String buildTypeName;//非蒲公英参数，用于控制打包release还是debug，默认1是release，2是debug
    //(选填)应用安装方式，值为(1,2,3，默认为1 公开安装)。1：公开安装，2：密码安装，3：邀请安装
    public int buildInstallType = 1;
    //(选填) 设置App安装密码，密码为空时默认公开安装
    public String buildPassword;
    //(选填) 版本更新描述，请传空字符串，或不传。
    public String buildUpdateDescription;
    //选填，版本更新描述文件
    public String buildUpdateDescriptionFile;
    //(选填)是否设置安装有效期，值为：1 设置有效时间， 2 长期有效，如果不填写不修改上一次的设置
    public int buildInstallDate = 2;
    //(选填)所需更新的指定渠道的下载短链接，只可指定一个渠道，字符串型，如：abcd
    public String buildChannelShortcut;

    public String uploadApkFilePath;
    public String pgyUploadTag;
    public String versionName;

    public UploadPgyParams() {

    }

    public UploadPgyParams(String apiKey) {
        this(apiKey, "Release");
    }

    public UploadPgyParams(String apiKey, String buildTypeName) {
        this(apiKey, "", buildTypeName, 1, "");
    }

    public UploadPgyParams(String apiKey, String appName, String buildTypeName, int buildInstallType, String buildPassword) {
        this(apiKey, appName, buildTypeName, buildInstallType, buildPassword, "");
    }

    public UploadPgyParams(String apiKey, String appName, String buildTypeName, int buildInstallType, String buildPassword, String buildUpdateDescription) {
        this(apiKey, appName, buildTypeName, buildInstallType, buildPassword, buildUpdateDescription, 2, "");
    }

    public UploadPgyParams(String apiKey, String appName, String buildTypeName, int buildInstallType, String buildPassword, String buildUpdateDescription, int buildInstallDate, String buildChannelShortcut) {
        this.apiKey = apiKey;
        this.appName = appName;
        this.buildTypeName = buildTypeName;
        this.buildInstallType = buildInstallType;
        this.buildPassword = buildPassword;
        this.buildUpdateDescription = buildUpdateDescription;
        this.buildInstallDate = buildInstallDate;
        this.buildChannelShortcut = buildChannelShortcut;
    }

    public static UploadPgyParams getConfig(Project project) {
        UploadPgyParams extension = project.getExtensions().findByType(UploadPgyParams.class);
        if (extension == null) {
            extension = new UploadPgyParams();
        }
        String s = FileIOUtils.parseFile(extension.buildUpdateDescriptionFile);
        if (!PluginUtils.isEmpty(s)){
            extension.buildUpdateDescription = s;
        }
        extension.printInfo();
        return extension;
    }


    @Override
    public String toString() {
        return "UploadPgyParams{" +
                "apiKey='" + apiKey + '\'' +
                ", appName='" + appName + '\'' +
                ", buildTypeName='" + buildTypeName + '\'' +
                ", buildInstallType=" + buildInstallType +
                ", buildPassword='" + buildPassword + '\'' +
                ", buildUpdateDescription='" + buildUpdateDescription + '\'' +
                ", buildUpdateDescriptionFile='" + buildUpdateDescriptionFile + '\'' +
                ", buildInstallDate=" + buildInstallDate +
                ", buildChannelShortcut='" + buildChannelShortcut + '\'' +
                ", uploadApkFilePath='" + uploadApkFilePath + '\'' +
                ", pgyUploadTag='" + pgyUploadTag + '\'' +
                ", version='" + versionName + '\'' +
                '}';
    }

    public void printInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("> 蒲公英上传信息:");
        if (!PluginUtils.isEmpty(appName)) {
            sb.append('\n').append("> appName:").append(appName);
        }
        if (!PluginUtils.isEmpty(versionName)) {
            sb.append('\n').append("> version: ").append(versionName);
        }
        sb.append('\n').append("> buildUpdateDescription:").append(buildUpdateDescription);
        if (!PluginUtils.isEmpty(pgyUploadTag)) {
            sb.append('\n').append("> pgyUploadTag:").append(pgyUploadTag);
        }
        System.out.println(sb);
    }
}
