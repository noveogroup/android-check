/*
 * Copyright (c) 2014 Noveo Group
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

package com.noveogroup.android.check

import net.sourceforge.pmd.ant.Formatter
import net.sourceforge.pmd.ant.PMDTask
import org.gradle.api.GradleException
import org.gradle.api.Project

public class AndroidPmdPlugin extends AbstractAndroidCheckPlugin {

    private PMDTask createPMDTask(Project target, URL configURL, File xmlReport) {
        PMDTask pmdTask = new PMDTask()

        pmdTask.project = target.ant.antProject
        pmdTask.ruleSetFiles = configURL.toString()
        pmdTask.addFormatter(new Formatter(type: 'xml', toFile: xmlReport))

        getAndroidSources(target).findAll { it.exists() }.each {
            pmdTask.addFileset(target.ant.fileset(dir: it))
        }

        return pmdTask
    }

    @Override
    public void apply(Project target) {
        target.extensions.add('androidPmd', new CheckExtension())

        target.task(
                [group      : 'verification',
                 description: 'Runs Android PMD'],
                'androidPmd') << {
            CheckExtension check = target.extensions.androidCheckstyle

            File configFile = check.config
            if (configFile == null) configFile = target.file('config/pmd.xml')
            URL config = configFile.toURI().toURL()

            File xmlReport = new File(target.buildDir, 'outputs/pmd/pmd.xml')
            File htmlReport = new File(target.buildDir, 'outputs/pmd/pmd.html')
            String template = target.buildscript.classLoader.getResourceAsStream('pmd/pmd.xsl').text

            xmlReport.parentFile.mkdirs()
            createPMDTask(target, config, xmlReport).perform()
            htmlReport.parentFile.mkdirs()
            target.ant.xslt(in: xmlReport, out: htmlReport) { style { string(template) } }

            def xml = new XmlSlurper().parseText(xmlReport.text)
            int count = xml.file.inject(0) { count, file -> count + file.violation.size() }
            if (count) {
                String message = "$count PMD rule violations were found. See the report at: ${htmlReport.toURI()}"
                if (check.abortOnError) {
                    throw new GradleException(message)
                } else {
                    target.logger.warn message
                }
            }
        }

        target.tasks.getByName('check').dependsOn 'androidPmd'
    }

}
