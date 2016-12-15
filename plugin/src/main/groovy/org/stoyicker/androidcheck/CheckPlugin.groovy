package org.stoyicker.androidcheck

import org.stoyicker.androidcheck.checkstyle.CheckstyleCheck
import org.stoyicker.androidcheck.findbugs.FindbugsCheck
import org.stoyicker.androidcheck.pmd.PmdCheck
import org.gradle.api.Plugin
import org.gradle.api.Project

class CheckPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.extensions.create(CheckExtension.NAME, CheckExtension, target)

        new CheckstyleCheck().apply(target)
        new FindbugsCheck().apply(target)
        new PmdCheck().apply(target)
    }

}
