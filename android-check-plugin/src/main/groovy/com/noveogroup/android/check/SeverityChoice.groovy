package com.noveogroup.android.check

enum SeverityChoice {
    lite (rulesetCheckstyle: 'checkstyle/checkstyle-lite.xml', rulesetPMD: 'pmd/pmd-lite.xml'),
    hard (rulesetCheckstyle: 'checkstyle/checkstyle-hard.xml', rulesetPMD: 'pmd/pmd-hard.xml');

    final String rulesetCheckstyle;
    final String rulesetPMD;
}
