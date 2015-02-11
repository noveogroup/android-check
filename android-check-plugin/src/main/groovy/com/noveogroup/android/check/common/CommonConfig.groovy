/*
 * Copyright (c) 2015 Noveo Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.android.check.common

import org.gradle.api.Project
import org.gradle.api.resources.TextResource

class CommonConfig {

    protected final Project project

    CommonConfig(Project project) {
        this.project = project
    }

    Boolean abortOnError = null

    void abortOnError(boolean abortOnError) { this.abortOnError = abortOnError }

    private TextResource configResource = null
    private File configFile = null
    private Severity configSeverity = null

    private void checkConfigDefined() {
        if (configResource || configFile || configSeverity) {
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

    void config(String severity) {
        checkConfigDefined()
        this.configSeverity = Severity.parse(severity)
        if (!configSeverity) {
            throw new IllegalArgumentException("wrong severity $severity. allowed values are: ${Severity.values()}")
        }
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
        if (configSeverity) {
            return Utils.getResource(project, "$code/$code-${configSeverity.suffix}.xml")
        }

        File file = project.file("config/${code}.xml")
        if (file.exists()) {
            return file.text
        }

        return Utils.getResource(project, "$code/$code-${Severity.EASY.suffix}.xml")
    }

    File resolveConfigFile(String code) {
        File file = new File(project.buildDir, "tmp/android-check/${code}.xml")
        file.parentFile.mkdirs()
        file << resolveConfig(code)
        return file
    }

    private String resolveStyle(String code) {
        return Utils.getResource(project, "$code/${code}.xsl")
    }

    File resolveStyleFile(String code) {
        File file = new File(project.buildDir, "tmp/android-check/${code}.xsl")
        file.parentFile.mkdirs()
        file << resolveStyle(code)
        return file
    }

    File resolveXmlReportFile(String code) {
        return new File(project.buildDir, "outputs/$code/${code}.xml")
    }

    File resolveHtmlReportFile(String code) {
        return new File(project.buildDir, "outputs/$code/${code}.html")
    }

    List<File> getAndroidSources() {
        return Utils.getAndroidSources(project)
    }

}
