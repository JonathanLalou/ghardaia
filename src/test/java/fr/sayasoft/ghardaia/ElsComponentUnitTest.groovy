package fr.sayasoft.ghardaia

import org.junit.jupiter.api.Test

class ElsComponentUnitTest {
    static final String book =
            "BEGIN" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaxreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaxreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaxreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "Jhfkjdhfueyrueyrbfjdbcdbcdhsgaxreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaxreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "OhfkjdhfueyrueyrbfjdbcdbcdhsgaJxxxxxuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaOyyyyyuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "NhfkjdhfueyrueyrbfjdbcdbcdhsgaNzzzzzzewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaAttttttewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "AhfkjdhfueyrueyrbfjdbcdbcdhsgaTuuuuuuuwysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaHvvvvyuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "ThfkjdhfueyrueyrbfjdbcdbcdhsgaAwwwwwwwwysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgaNssssssewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "Hhfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "Ahfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "Nhfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "shfkjdhfueyrueyrbfjdbcdbcdhsgayreuryuewysjnbdasnbdxcmnbdasdhkjashduwyeuwyqebhs" +
                    "END"

    @Test
    void retrieveELS_notFound() {
        assert !new ElsComponent().retrieveELS("ABCDEFGH", book).isPresent()
        assert !new ElsComponent().retrieveELS("JONATHAN", book, 5, false, 200, 300).isPresent()
        assert !new ElsComponent().retrieveELS("JONATHAN", book, 5, false, 10, 20).isPresent()
    }

    @Test
    void retrieveELS_OK() {
//        final String book = new UnicodeComponent().formatFolder('src/main/resources')
        final String search = "JONATHAN"
        def radius = 3

        def elsOptional = new ElsComponent().retrieveELS(search, book, radius, false, 60, 100)
        assert elsOptional.isPresent()

        def equidistantLetterSequence = elsOptional.get()
        assert 1724 == equidistantLetterSequence.bookSize
        assert 77 == equidistantLetterSequence.distance
        assert 425 == equidistantLetterSequence.firstLetterIndex
        assert  "sgaJxxx\n" +
                "sgaOyyy\n" +
                "sgaNzzz\n" +
                "sgaAttt\n" +
                "sgaTuuu\n" +
                "sgaHvvv\n" +
                "sgaAwww\n" +
                "sgaNsss\n" == equidistantLetterSequence.matrix

        elsOptional = new ElsComponent().retrieveELS(search, book, radius, false, 150, 170)
        assert elsOptional.isPresent()

        equidistantLetterSequence = elsOptional.get()
        assert 1724 == equidistantLetterSequence.bookSize
        assert 155 == equidistantLetterSequence.distance
        assert 239 == equidistantLetterSequence.firstLetterIndex
        assert  "bhsJhfk\n" +
                "bhsOhfk\n" +
                "bhsNhfk\n" +
                "bhsAhfk\n" +
                "bhsThfk\n" +
                "bhsHhfk\n" +
                "bhsAhfk\n" +
                "bhsNhfk\n" == equidistantLetterSequence.matrix
    }
}
