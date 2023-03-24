package com.neptuneg.usecase.interator

import com.neptuneg.usecase.inputport.Sample

class SampleImpl : Sample {
    override fun foobar(): Sample.Message {
        return Sample.Message("Good luck🐱")
    }
}
