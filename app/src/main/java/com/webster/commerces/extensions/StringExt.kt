package com.webster.commerces.extensions

import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern

fun String.deAccent(): String {
    val nfdNormalizedString: String = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
}

fun String.containsDeAccentLowCase(value: String): Boolean {
    val validateString = this.deAccent()
    return validateString.toLowerCase(Locale.getDefault()).contains(value)
}