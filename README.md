Android Check
=============

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--check-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1530)

Static code analysis plugin for Android project.

Usage
-----

Modifications in `<project_dir>/build.gradle`:

```groovy
buildscript {
    repositories { jcenter() }
    dependencies {
        ...
        classpath 'com.noveogroup.android:check:1.2.5'
        ...
    }
}
```

Modifications in `<project_dir>/<module_name>/build.gradle`:

```groovy
apply plugin: 'com.noveogroup.android.check'
```

Configuration
-------------

### Recommended

Recommended configuration is a default one (empty).

```groovy
// no configuration
```

### Hardcore

```groovy
check {
  abortOnError true
  checkstyle { config hard() }
  findbugs { config hard() }
  pmd { config hard() }
}
```

### Skip checking

```groovy
check { skip true }
```

Skip Checkstyle only

```groovy
check { checkstyle { skip true } }
```

Skip FindBugs only

```groovy
check { findbugs { skip true } }
```

Skip PMD only

```groovy
check { pmd { skip true } }
```

### Description

```groovy
// configuration is optional
check {
  // skip source code checking or not, false by default
  skip true/false
  // fails build if code style violation is found, false by default
  abortOnError true/false
  // configuration of Checkstyle checker
  checkstyle {
    // skip Checkstyle, false by default
    skip true/false

    // fails build if Checkstyle rule violation is found, false by default
    abortOnError true/false

    // fails build if threshold is breached. -1 by default, meaning threshold not checked
    threshold -1

    // configuration file
    config project.file('path/to/checkstyle.xml')
    // configuration resource
    // see http://gradle.org/docs/2.2/release-notes#sharing-configuration-files-across-builds
    config resources.text.fromFile(someTask)
    // configuration path
    config 'path/to/checkstyle.xml'
    // predefined configurations: easy and hard
    config easy()
    config hard()
    // by default plugin finds configuration file in <rootProject>/config/checkstyle.xml,
    // after that in <project>/config/checkstyle.xml and if there are no configuration
    // file, easy() configuration will be used.

    // directory for report files
    report new File(project.buildDir, 'reports/pmd')
    // XML report file
    reportXML new File(project.buildDir, 'reports/checkstyle/checkstyle.xml')
    // HTML report file
    reportHTML new File(project.buildDir, 'reports/checkstyle/checkstyle.html')
  }
  // configuration of FindBugs checker
  findbugs {
    // the same configuration as for Checkstyle

    // by default plugin finds configuration file in <rootProject>/config/findbugs.xml,
    // after that in <project>/config/findbugs.xml and if there are no configuration
    // file, easy() configuration will be used.
  }
  // configuration of PMD checker
  pmd {
    // the same configuration as for Checkstyle

    // by default plugin finds configuration file in <rootProject>/config/pmd.xml,
    // after that in <project>/config/pmd.xml and if there are no configuration
    // file, easy() configuration will be used.
  }
}
```

Developed By
============

* [Noveo Group][1]
* [Pavel Stepanov](https://github.com/stefan-nsk)
* [Sergey Malichenko](https://github.com/smalichenko) - <Sergey.Malichenko@noveogroup.com>

License
=======

    Copyright (c) 2017 Noveo Group

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    Except as contained in this notice, the name(s) of the above copyright holders
    shall not be used in advertising or otherwise to promote the sale, use or
    other dealings in this Software without prior written authorization.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

[1]: http://noveogroup.com/
