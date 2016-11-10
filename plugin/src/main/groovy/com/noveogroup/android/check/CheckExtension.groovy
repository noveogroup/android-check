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

package com.noveogroup.android.check

import com.noveogroup.android.check.checkstyle.CheckstyleConfig
import com.noveogroup.android.check.findbugs.FindbugsConfig
import com.noveogroup.android.check.pmd.PmdConfig
import org.gradle.api.Action
import org.gradle.api.Project

class CheckExtension {

    static final String NAME = 'check'

    private final Project project

    CheckstyleConfig checkstyle

    void checkstyle(Action<CheckstyleConfig> action) { action.execute(checkstyle) }

    FindbugsConfig findbugs

    void findbugs(Action<FindbugsConfig> action) { action.execute(findbugs) }

    PmdConfig pmd

    void pmd(Action<PmdConfig> action) { action.execute(pmd) }

    CheckExtension(Project project) {
        this.project = project
        this.checkstyle = new CheckstyleConfig(project)
        this.findbugs = new FindbugsConfig(project)
        this.pmd = new PmdConfig(project)
    }

    boolean skip = false

    void skip(boolean skip) { this.skip = skip }

    boolean abortOnError = false

    void abortOnError(boolean abortOnError) { this.abortOnError = abortOnError }

}
