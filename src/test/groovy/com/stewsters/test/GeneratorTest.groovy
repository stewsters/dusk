package com.stewsters.test

import com.stewsters.dusk.core.map.gen.name.KnightNameGen
import spock.lang.Specification

class GeneratorTest extends Specification {
    def "Test generator gives legal results"() {

        when:

        30.times {
            println KnightNameGen.generate()
        }

        then:
        true

    }
}
