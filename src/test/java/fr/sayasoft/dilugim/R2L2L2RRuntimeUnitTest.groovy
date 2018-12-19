package fr.sayasoft.dilugim

import org.junit.jupiter.api.Test

class R2L2L2RRuntimeUnitTest {
    @Test
    void "format Genesis"() {
        final R2L2L2R instance = new R2L2L2R(filePath: 'src/main/resources/01-Genesis-source.txt')
        def actual = instance.format()
//        println "*********************\n Formatted content: \n ${actual}"
        assert 'ב' == actual.charAt(0)
        assert 'ר' == actual.charAt(1)
        assert 'א' == actual.charAt(2)
        assert 'ש' == actual.charAt(3)
        assert 'י' == actual.charAt(4)
    }

    @Test
    void "format Exodus"() {
        final R2L2L2R instance = new R2L2L2R(filePath: 'src/main/resources/02-Exodus-source.txt')
        def actual = instance.format()
//        println "*********************\n Formatted content: \n ${actual}"
        assert 'ו' == actual.charAt(0)
        assert 'א' == actual.charAt(1)
        assert 'ל' == actual.charAt(2)
        assert 'ה' == actual.charAt(3)
        assert 'ש' == actual.charAt(4)
    }

}