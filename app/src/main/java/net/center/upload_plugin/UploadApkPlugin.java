package net.center.upload_plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.internal.dsl.BuildType;
import com.android.builder.model.ClassField;

import net.center.upload_plugin.params.GitLogParams;
import net.center.upload_plugin.params.SendDingParams;
import net.center.upload_plugin.params.SendFeishuParams;
import net.center.upload_plugin.params.SendWeixinGroupParams;
import net.center.upload_plugin.params.UploadPgyParams;
import net.center.upload_plugin.task.BuildAndUploadTask;
import net.center.upload_plugin.task.OnlyUploadTask;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Map;

/**
 * Created by Android-ZX
 * 2021/9/3.
 */
public class UploadApkPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        UploadPgyParams uploadParams = project.getExtensions().create(PluginConstants.UPLOAD_PARAMS_NAME, UploadPgyParams.class);
        createParams(project);
        dependsOnOnlyUploadTask(uploadParams, project);
//        UploadPgyParams ext = (UploadPgyParams) project.property(PluginConstants.UPLOAD_PARAMS_NAME);
//        System.out.println("apkfile path1: " + uploadParams.uploadApkFilePath);
//        System.out.println("apkfile path2: " + ext.uploadApkFilePath);
        project.afterEvaluate(project1 -> {
            AppExtension appExtension = ((AppExtension) project1.getExtensions().findByName(PluginConstants.ANDROID_EXTENSION_NAME));
            if (appExtension == null) {
                return;
            }
            printBuildConfigFields(uploadParams.buildTypeName, appExtension);
            UploadPgyParams.getConfig(project);
            SendWeixinGroupParams.getWeixinGroupConfig(project);
            DomainObjectSet<ApplicationVariant> appVariants = appExtension.getApplicationVariants();
            for (ApplicationVariant applicationVariant : appVariants) {
                if (applicationVariant.getBuildType() != null) {
                    dependsOnTask(applicationVariant, uploadParams, project1, appExtension);
                }
            }
        });
    }

    private void createParams(Project project) {
        project.getExtensions().create(PluginConstants.GIT_LOG_PARAMS_NAME, GitLogParams.class);
        project.getExtensions().create(PluginConstants.DING_PARAMS_NAME, SendDingParams.class);
        project.getExtensions().create(PluginConstants.FEISHU_PARAMS_NAME, SendFeishuParams.class);
        project.getExtensions().create(PluginConstants.WEIXIN_GROUP_PARAMS_NAME, SendWeixinGroupParams.class);
    }


    private void dependsOnTask(ApplicationVariant applicationVariant, UploadPgyParams uploadParams, Project project1, AppExtension appExtension) {
        String variantName = getVariantName(applicationVariant, uploadParams);
        //创建我们，上传到蒲公英的task任务
        BuildAndUploadTask uploadTask = project1.getTasks()
                .create(PluginConstants.TASK_EXTENSION_NAME + variantName, BuildAndUploadTask.class);
        uploadTask.init(applicationVariant, project1);

        //依赖关系 。上传依赖打包，打包依赖clean。
//        applicationVariant.getAssembleProvider().get().dependsOn(project1.getTasks().findByName("clean"));
        uploadTask.dependsOn(applicationVariant.getAssembleProvider().get()).doLast(task -> printBuildConfigFields(variantName, appExtension));
    }

    private static String getVariantName(ApplicationVariant applicationVariant, UploadPgyParams uploadParams) {
        String variantName =
                applicationVariant.getName().substring(0, 1).toUpperCase() + applicationVariant.getName().substring(1);
        if (PluginUtils.isEmpty(variantName)) {
            variantName = PluginUtils.isEmpty(uploadParams.buildTypeName) ? "Release" : uploadParams.buildTypeName;
        }
        return variantName;
    }

    private static void printBuildConfigFields(String variantName, AppExtension appExtension) {
        BuildType byName = appExtension.getBuildTypes().findByName(variantName.toLowerCase());
        if (byName != null) {
            Map<String, ClassField> buildConfigFields = byName.getBuildConfigFields();
            if (buildConfigFields != null) {
                buildConfigFields.forEach((s, classField) -> System.out.println(String.format("> [%s]:%s", s.toLowerCase(), classField.getValue())));
            }
        }
    }

    private void dependsOnOnlyUploadTask(UploadPgyParams uploadParams, Project project1) {
        //创建我们，上传到蒲公英的task任务
        OnlyUploadTask uploadTask = project1.getTasks()
                .create(PluginConstants.TASK_EXTENSION_NAME_ONLY_UPLOAD, OnlyUploadTask.class);
        uploadTask.init(null, project1);

//        //依赖关系 。上传依赖打包，打包依赖clean。
//        applicationVariant.getAssembleProvider().get().dependsOn(project1.getTasks().findByName("clean"));
//        uploadTask.dependsOn(applicationVariant.getAssembleProvider().get());
    }
}


