package org.stoyicker.androidcheck

import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile

final class Utils {

    private Utils() { throw new UnsupportedOperationException() }

    static String getResource(Project project, String resourcePath) {
        Set<File> files = new HashSet<>()
        files += project.buildscript.configurations.classpath.resolve()
        files += project.rootProject.buildscript.configurations.classpath.resolve()
        File file = files.find { new JarFile(it).getJarEntry(resourcePath) }
        if (file == null) {
            return null
        } else {
            JarFile jarFile = new JarFile(file)
            JarEntry jarEntry = jarFile.getJarEntry(resourcePath)
            return jarFile.getInputStream(jarEntry).text
        }
    }

    static List<File> getAndroidSources(Project project) {
        project.android.sourceSets.inject([]) {
            dirs, sourceSet -> dirs + sourceSet.java.srcDirs
        }
    }

}
