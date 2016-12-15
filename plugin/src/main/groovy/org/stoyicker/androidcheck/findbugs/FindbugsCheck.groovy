package org.stoyicker.androidcheck.findbugs

import edu.umd.cs.findbugs.anttask.FindBugsTask
import groovy.util.slurpersupport.GPathResult
import org.stoyicker.androidcheck.CheckExtension
import org.stoyicker.androidcheck.CommonCheck
import org.stoyicker.androidcheck.CommonConfig
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.Path
import org.gradle.api.Project

class FindbugsCheck extends CommonCheck {

    FindbugsCheck() { super('findbugs', 'androidFindbugs', 'Runs Android FindBugs') }

    @Override
    protected Set<String> getDependencies() { ['assemble'] }

    @Override
    protected CommonConfig getConfig(CheckExtension extension) { return extension.findbugs }

    @Override
    protected void performCheck(Project project, List<File> sources,
                                File configFile, File xmlReportFile) {

        FindBugsTask findBugsTask = new FindBugsTask()

        findBugsTask.project = project.ant.antProject
        findBugsTask.workHard = true
        findBugsTask.excludeFilter = configFile
        findBugsTask.output = "xml:withMessages"
        findBugsTask.outputFile = xmlReportFile
        findBugsTask.failOnError = false

        Path sourcePath = findBugsTask.createSourcePath()
        sources.findAll { it.exists() }.each {
            sourcePath.addFileset(project.ant.fileset(dir: it))
        }

        Path classpath = findBugsTask.createClasspath()
        project.rootProject.buildscript.configurations.classpath.resolve().each {
            classpath.createPathElement().location = it
        }
        project.buildscript.configurations.classpath.resolve().each {
            classpath.createPathElement().location = it
        }

        Set<String> includes = []
        sources.findAll { it.exists() }.each { File directory ->
            FileSet fileSet = project.ant.fileset(dir: directory)
            Path path = project.ant.path()
            path.addFileset(fileSet)

            path.each {
                String includePath = new File(it.toString()).absolutePath - directory.absolutePath
                includes.add("**${includePath.replaceAll('\\.java$', '')}*")
            }
        }

        findBugsTask.addFileset(project.ant.fileset(dir: project.buildDir, includes: includes.join(',')))

        findBugsTask.perform()
    }

    @Override
    protected int getErrorCount(File xmlReportFile) {
        GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
        return xml.FindBugsSummary.getProperty('@total_bugs').text() as int
    }

    @Override
    protected String getErrorMessage(int errorCount, File htmlReportFile) {
        return "$errorCount FindBugs rule violations were found. See the report at: ${htmlReportFile.toURI()}"
    }

}
