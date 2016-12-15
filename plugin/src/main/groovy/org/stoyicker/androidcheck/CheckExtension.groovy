package org.stoyicker.androidcheck

import org.stoyicker.androidcheck.checkstyle.CheckstyleConfig
import org.stoyicker.androidcheck.findbugs.FindbugsConfig
import org.stoyicker.androidcheck.pmd.PmdConfig
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
