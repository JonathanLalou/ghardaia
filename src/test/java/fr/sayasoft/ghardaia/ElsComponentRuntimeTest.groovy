package fr.sayasoft.ghardaia

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class ElsComponentRuntimeTest {

    @Test
    void retrieveELS_OK() {
        final String book = new UnicodeComponent().formatFolder('src/main/resources')
//        final String search = "‭ןיבר"
//        final String search = "‭ןיבר"
//        final String search = ["ק" , "ח" , "צ" , "י"].join().reverse()
        final String search = ["ן" , "י" , "ב" , "ר" ].join().reverse()
//        final String search = "‭ןיבר קחצי"
        def radius = 10

        def elsOptional = new ElsComponent().translitterateAndretrieveELS(search, book, radius, false, 0, 100)
        assert elsOptional.isPresent()

        def equidistantLetterSequence = elsOptional.get()
        println equidistantLetterSequence.toString()
    }
}
