Android Check 2
===============

Static code analysis plugin for Android project.
This is a fork of [the original android-check plugin][1], which implements a really useful concept, but unfortunately seems abandoned.

Build status
------------

### master [![master](https://travis-ci.org/stoyicker/android-check-2.svg?branch=master)](https://travis-ci.org/stoyicker/android-check-2)
### dev [![dev](https://travis-ci.org/stoyicker/android-check-2.svg?branch=dev)](https://travis-ci.org/stoyicker/android-check-2)

Usage
-----

Modifications in `<project_dir>/build.gradle`:

```
buildscript {
    repositories { jcenter() }
    dependencies {
        ...
        classpath 'io.github.stoyicker:check:2.0.0'
        ...
    }
}
```

Modifications in `<project_dir>/<module_name>/build.gradle`:

```
apply plugin: 'org.stoyicker.android-check'
```

Configuration
-------------

### Recommended

The default one.

### Customized

```
// Configuration is completely optional, defaults will be used if not present
check {
  // Do absolutely nothing, default: false
  skip true/false
  // Fails build if a violation is found, default: false
  abortOnError true/false
  // Checkstyle configuration
  checkstyle {
    // Completely skip CheckStyle, default: false
    skip true/false

    // Fails build if CheckStyle rule violation is found, default: false
    abortOnError true/false

    // Configuration file for CheckStyle, default: <project_path>/config/checkstyle.xml, if non-existent then <project_path>/<module_path>/config/checkstyle.xml, if non-existent then plugin/src/main/resources/checkstyle/conf-default.xml
    config 'path/to/checkstyle.xml'

    // Output file for XML reports, default: new File(project.buildDir, 'outputs/checkstyle/checkstyle.xml')
    reportXML new File(project.buildDir, 'path/where/you/want/checkstyle.xml')

    // Output file for HTML reports, default: not generated
    reportHTML new File(project.buildDir, 'path/where/you/want/checkstyle.html')
  }
  // FindBugs configuration
  findbugs {
    // Same options as Checkstyle, except for a couple of defaults:

    // Configuration file for CheckStyle, default: <project_path>/config/findbugs.xml, if non-existent then <project_path>/<module_path>/config/findbugs.xml, if non-existent then plugin/src/main/resources/findbugs/conf-default.xml
    config 'path/to/findbugs.xml'

    // Output file for XML reports, default: new File(project.buildDir, 'outputs/findbugs/findbugs.xml')
    reportXML new File(project.buildDir, 'path/where/you/want/findbugs.xml')
  }
  // PMD configuration
  pmd {
    // Same options as Checkstyle and FindBugs, except for a couple of defaults:

    // Configuration file for CheckStyle, default: <project_path>/config/pmd.xml, if non-existent then <project_path>/<module_path>/config/pmd.xml, if non-existent then plugin/src/main/resources/pmd/conf-default.xml
    config 'path/to/pmd.xml'

    // Output file for XML reports, default: new File(project.buildDir, 'outputs/pmd/pmd.xml')
    reportXML new File(project.buildDir, 'path/where/you/want/pmd.xml')
  }
}
```

Developed By
============

The original version of this plugin was developed by:

  - [Noveo Group][2]
  - [Pavel Stepanov](https://github.com/stefan-nsk) - <pstepanov@noveogroup.com>

This fork is owned and maintained by [Jorge Antonio Diaz-Benito Soriano](https://www.linkedin.com/in/jorgediazbenitosoriano).

License
=======

See [LICENSE.txt](LICENSE.txt).

Original work licensed under [MIT license](https://github.com/noveogroup/android-check/blob/master/LICENSE.txt).

[1]: https://github.com/noveogroup/android-check
[2]: http://noveogroup.com/
