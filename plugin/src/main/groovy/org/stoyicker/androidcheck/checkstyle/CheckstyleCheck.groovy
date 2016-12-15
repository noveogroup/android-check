package org.stoyicker.androidcheck.checkstyle

import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask
import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask.FormatterType
import groovy.util.slurpersupport.GPathResult
import org.stoyicker.androidcheck.CheckExtension
import org.stoyicker.androidcheck.CommonCheck
import org.stoyicker.androidcheck.CommonConfig
import org.gradle.api.Project

class CheckstyleCheck extends CommonCheck {

    CheckstyleCheck() { super('checkstyle', 'androidCheckstyle', 'Runs Android Checkstyle') }

    @Override
    protected CommonConfig getConfig(CheckExtension extension) { return extension.checkstyle }

    @Override
    protected void performCheck(Project project, List<File> sources,
                                File configFile, File xmlReportFile) {
        CheckstyleAntTask checkStyleTask = new CheckstyleAntTask()

        checkStyleTask.project = project.ant.antProject
        checkStyleTask.configURL = configFile.toURI().toURL()
        checkStyleTask.addFormatter(new Formatter(type: new FormatterType(value: 'xml'), tofile: xmlReportFile))

        checkStyleTask.failOnViolation = false

        sources.findAll { it.exists() }.each {
            checkStyleTask.addFileset(project.ant.fileset(dir: it))
        }

        checkStyleTask.perform()
    }

    @Override
    protected int getErrorCount(File xmlReportFile) {
        GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
        return xml.file.inject(0) { count, file -> count + file.error.size() }
    }

    @Override
    protected String getErrorMessage(int errorCount, File htmlReportFile) {
        return "$errorCount Checkstyle rule violations were found. See the report at: ${htmlReportFile.toURI()}"
    }

}
