package org.stoyicker.androidcheck

import org.gradle.api.Project
import org.gradle.api.resources.TextResource

class CommonConfig {

    protected final Project project

    CommonConfig(Project project) {
        this.project = project
    }

    Boolean skip = null

    void skip(boolean skip) { this.skip = skip }

    Boolean abortOnError = null

    void abortOnError(boolean abortOnError) { this.abortOnError = abortOnError }

    private TextResource configResource = null
    private File configFile = null

    private void checkConfigDefined() {
        if (configResource || configFile) {
            throw new IllegalArgumentException('configuration XML is already defined')
        }
    }

    void config(TextResource resource) {
        checkConfigDefined()
        this.configResource = resource
    }

    void config(File file) {
        checkConfigDefined()
        this.configFile = file
    }

    void config(String path) {
        config(project.file(path))
    }

    File reportXML
    File reportHTML

    void reportXML(File reportXML) {
        this.reportXML = reportXML
    }

    void reportHTML(File reportHTML) {
        this.reportHTML = reportHTML
    }

    boolean resolveSkip(boolean defaultSkip) {
        return skip == null ? defaultSkip : skip
    }

    boolean resolveAbortOnError(boolean defaultAbortOnError) {
        return abortOnError == null ? defaultAbortOnError : abortOnError
    }

    private String resolveConfig(String code) {
        if (configResource) {
            return configResource.asString()
        }
        if (configFile) {
            return configFile.text
        }

        File file = project.file("config/${code}.xml")
        if (file.exists()) {
            return file.text
        }

        File rootFile = project.rootProject.file("config/${code}.xml")
        if (rootFile.exists()) {
            return rootFile.text
        }

        return Utils.getResource(project, "$code/conf-default.xml")
    }

    File resolveConfigFile(String code) {
        File file = new File(project.buildDir, "tmp/android-check/${code}.xml")
        file.parentFile.mkdirs()
        file.delete()
        file << resolveConfig(code)
        return file
    }

    private String resolveStyle(String code) {
        return Utils.getResource(project, "$code/${code}.xsl")
    }

    File resolveStyleFile(String code) {
        File file = new File(project.buildDir, "tmp/android-check/${code}.xsl")
        file.parentFile.mkdirs()
        file.delete()
        file << resolveStyle(code)
        return file
    }

    private File resolveReportFile(String extension, File reportFile, String code) {
        if (reportFile) {
            return reportFile
        }

        return new File(project.buildDir, "outputs/${code}/${code}.${extension}")
    }

    File resolveXmlReportFile(String code) {
        return resolveReportFile('xml', reportXML, code)
    }

    File resolveHtmlReportFile(String code) {
        return resolveReportFile('html', reportHTML, code)
    }

    List<File> getAndroidSources() {
        return Utils.getAndroidSources(project)
    }

}
