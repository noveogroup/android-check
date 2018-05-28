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

package com.noveogroup.android.check.pmd

import com.noveogroup.android.check.CheckExtension
import com.noveogroup.android.check.common.CommonCheck
import com.noveogroup.android.check.common.CommonConfig
import groovy.util.slurpersupport.GPathResult
import net.sourceforge.pmd.ant.Formatter
import net.sourceforge.pmd.ant.PMDTask
import org.gradle.api.Project
import java.nio.file.Path
import java.nio.file.Paths

class PmdCheck extends CommonCheck {

    PmdCheck() { super('pmd', 'androidPmd', 'Runs Android PMD') }

    @Override
    protected CommonConfig getConfig(CheckExtension extension) { return extension.pmd }

    @Override
    protected void performCheck(Project project, List<File> sources,
                                File configFile, File xmlReportFile) {
        PMDTask pmdTask = new PMDTask()

        pmdTask.project = project.ant.antProject
        pmdTask.ruleSetFiles = configFile.toString()
        pmdTask.addFormatter(new Formatter(type: 'xml', toFile: xmlReportFile))

        Path cacheDir = Paths.get(project.getBuildDir().toString(), "tmp", "pmd.cache")
        pmdTask.cacheLocation = cacheDir.toString()

        pmdTask.failOnError = false
        pmdTask.failOnRuleViolation = false

        sources.findAll { it.exists() }.each {
            pmdTask.addFileset(project.ant.fileset(dir: it))
        }

        pmdTask.perform()
    }

    @Override
    protected int getErrorCount(File xmlReportFile) {
        GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
        return xml.file.inject(0) { count, file -> count + file.violation.size() }
    }

    @Override
    protected String getErrorMessage(int errorCount, File htmlReportFile) {
        return "$errorCount PMD rule violations were found. See the report at: ${htmlReportFile.toURI()}"
    }

}
