#!/usr/bin/env groovy

def xml = new XmlSlurper ().parse (args [0])

xml.file.error.inject ([:]) { stats, error ->
    String key = error.@source
    stats [key] = stats.get (key, 0) + 1
    stats
}.sort { -it.value }.each {
    println "${it.value.toString ().padLeft (4)}  ${it.key}"
}
