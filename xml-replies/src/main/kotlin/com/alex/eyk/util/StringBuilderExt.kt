package com.alex.eyk.util

fun StringBuilder.removeLastChars(count: Int) = apply {
    this.delete(this.length - count, this.length)
}