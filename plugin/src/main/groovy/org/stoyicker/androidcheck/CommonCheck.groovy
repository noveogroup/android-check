package org.stoyicker.androidcheck

import org.gradle.api.GradleException
import org.gradle.api.Project

import java.awt.*
import java.util.List

abstract class CommonCheck<Config extends CommonConfig> {

    final String taskCode
    final String taskName
    final String taskDescription

    CommonCheck(String taskCode, String taskName, String taskDescription) {
        this.taskCode = taskCode
        this.taskName = taskName
        this.taskDescription = taskDescription
    }

    protected Set<String> getDependencies() { [] }

    protected abstract Config getConfig(CheckExtension extension)

    protected abstract void performCheck(Project project, List<File> sources,
                                         File configFile, File xmlReportFile)

    protected abstract int getErrorCount(File xmlReportFile)

    protected abstract String getErrorMessage(int errorCount, File htmlReportFile)

    protected void reformatReport(Project project, File styleFile,
                                  File xmlReportFile, File htmlReportFile) {
        project.ant.xslt(in: xmlReportFile, out: htmlReportFile) {
            style { string(styleFile.text) }
        }
    }

    void apply(Project target) {
        target.task(
                [group      : 'verification',
                 description: taskDescription],
                taskName) << {
            CheckExtension extension = target.extensions.findByType(CheckExtension)
            Config config = getConfig(extension)

            boolean skip = config.resolveSkip(extension.skip)
            boolean abortOnError = config.resolveAbortOnError(extension.abortOnError)
            File configFile = config.resolveConfigFile(taskCode)
            File styleFile = config.resolveStyleFile(taskCode)
            File xmlReportFile = config.resolveXmlReportFile(taskCode)
            File htmlReportFile = config.resolveHtmlReportFile(taskCode)
            List<File> sources = config.getAndroidSources()

            if (skip) {
                target.logger.warn "skip $taskName"
            } else {
                xmlReportFile.parentFile.mkdirs()
                performCheck(target, sources, configFile, xmlReportFile)
                htmlReportFile.parentFile.mkdirs()
                reformatReport(target, styleFile, xmlReportFile, htmlReportFile)

                int errorCount = getErrorCount(xmlReportFile)
                if (errorCount) {
                    String errorMessage = getErrorMessage(errorCount, htmlReportFile)
                    if (abortOnError) {
                        Desktop.browse(new URI(htmlReportFile))
                        throw new GradleException(errorMessage)
                    } else {
                        target.logger.warn errorMessage
                    }
                }
            }
        }

        target.tasks.getByName('check').dependsOn taskName
        dependencies.each { target.tasks.getByName(taskName).dependsOn it }
    }

}
