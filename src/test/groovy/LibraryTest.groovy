import spock.lang.Specification

class LibraryTest extends Specification {
    def "someLibraryMethod returns true"() {

        when:
        def fish = true
        then:
        fish == true
    }
}
