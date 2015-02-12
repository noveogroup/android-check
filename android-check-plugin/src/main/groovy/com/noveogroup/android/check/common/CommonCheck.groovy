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

import com.noveogroup.android.check.CheckExtension
import org.gradle.api.GradleException
import org.gradle.api.Project

abstract class CommonCheck<Config extends CommonConfig> {

    final String taskCode
    final String taskName
    final String taskDescription

    CommonCheck(String taskCode, String taskName, String taskDescription) {
        this.taskCode = taskCode
        this.taskName = taskName
        this.taskDescription = taskDescription
    }

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
                        throw new GradleException(errorMessage)
                    } else {
                        target.logger.warn errorMessage
                    }
                }
            }
        }

        target.tasks.getByName('check').dependsOn taskName
    }

}
