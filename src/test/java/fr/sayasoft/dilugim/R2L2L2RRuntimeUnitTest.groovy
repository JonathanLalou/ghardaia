package fr.sayasoft.dilugim

import org.junit.jupiter.api.Test

class R2L2L2RRuntimeUnitTest {
    public static final int GENESIS_LENGTH = 78177
    public static final int EXODUS_LENGTH = 63621
    final R2L2L2R instance = new R2L2L2R()

    @Test
    void "format Genesis"() {
        def actual = instance.formatFile('/01-Genesis-source.txt')
//        println "*********************\n Formatted content: \n ${actual}"
        assert 'ב' == actual.charAt(0)
        assert 'ר' == actual.charAt(1)
        assert 'א' == actual.charAt(2)
        assert 'ש' == actual.charAt(3)
        assert 'י' == actual.charAt(4)
        assert GENESIS_LENGTH == actual.length()
    }

    @Test
    void "format Exodus"() {
        def actual = instance.formatFile('/02-Exodus-source.txt')
//        println "*********************\n Formatted content: \n ${actual}"
        assert 'ו' == actual.charAt(0)
        assert 'א' == actual.charAt(1)
        assert 'ל' == actual.charAt(2)
        assert 'ה' == actual.charAt(3)
        assert 'ש' == actual.charAt(4)
        assert EXODUS_LENGTH == actual.length()
    }

    @Test
    void "format all Books"() {
        def actual = instance.formatFolder('src/main/resources')
//        println "*********************\n Formatted content: \n ${actual}"
        assert 'ב' == actual.charAt(0)
        assert 'ר' == actual.charAt(1)
        assert 'א' == actual.charAt(2)
        assert 'ש' == actual.charAt(3)
        assert 'י' == actual.charAt(4)
        assert 'ו' == actual.charAt(GENESIS_LENGTH + 0)
        assert 'א' == actual.charAt(GENESIS_LENGTH + 1)
        assert 'ל' == actual.charAt(GENESIS_LENGTH + 2)
        assert 'ה' == actual.charAt(GENESIS_LENGTH + 3)
        assert 'ש' == actual.charAt(GENESIS_LENGTH + 4)
        assert EXODUS_LENGTH + GENESIS_LENGTH == actual.length()
    }

}