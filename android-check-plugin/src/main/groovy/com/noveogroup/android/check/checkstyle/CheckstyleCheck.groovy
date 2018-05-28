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

package com.noveogroup.android.check.checkstyle

import com.noveogroup.android.check.CheckExtension
import com.noveogroup.android.check.common.CommonCheck
import com.noveogroup.android.check.common.CommonConfig
import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask
import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask.Formatter
import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask.FormatterType
import groovy.util.slurpersupport.GPathResult
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
        checkStyleTask.setConfig(configFile.getPath())
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
