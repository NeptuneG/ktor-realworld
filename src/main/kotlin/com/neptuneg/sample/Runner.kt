package com.neptuneg.sample

class Runner(private val sample: Sample) {
    fun call(): String { return sample.foobar() }
}