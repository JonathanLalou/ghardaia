package fr.sayasoft.dilugim


import java.time.Duration
import java.time.LocalDateTime

import static org.apache.commons.lang3.StringUtils.substring

//@Log4j
class ShortestJump {
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

    Optional<EquidistantLetterSequence> retrieveELS(String search, String book, radius = 5, includeReverseOrder = false, minDistance = 0, maxDistance = 1000) {
        final LocalDateTime t0 = LocalDateTime.now();
        def searchSize = search.size()
        final Integer shortestEquidistantSequence, firstLetterIndex

        for (int step = minDistance; step < maxDistance; step++) {
            println("Testing step: ${step}")
// log.info("Testing step: ${step}")
            final StringBuilder stringBuilder = new StringBuilder("((\\w)*)")
            for (int i = 0; i < searchSize; i++) {
                stringBuilder.append("(${search.charAt(i)})").append("((\\w){$step})")
            }
            def regexp = stringBuilder.toString()
// println "with regexp: ${regexp}"
            def group = (book =~ regexp)
// NB: by default, the regexp gets the shortest matching string
            if (group.hasGroup() && group.size() > 0) {
                assert group.hasGroup()
                assert 'J' == group[0][3]
                shortestEquidistantSequence = step
                firstLetterIndex = group[0][1].size()
                break
            }
        }
        if (null == shortestEquidistantSequence) {
            return Optional.empty()
        }
// println "Shortest sequence : ${shortestEquidistantSequence}"
// println "First letter index : ${firstLetterIndex}"
        final StringBuilder matrixBuilder = new StringBuilder()
        for (int i = 0; i < searchSize; i++) {
            matrixBuilder.append(substring(book, firstLetterIndex + (i * shortestEquidistantSequence) - radius + i, firstLetterIndex + (i * shortestEquidistantSequence) + radius + i)).append("\n")
        }

        return Optional.of(
                new EquidistantLetterSequence(
                        distance: shortestEquidistantSequence,
                        firstLetterIndex: firstLetterIndex,
                        matrix: matrixBuilder.toString(),
                        bookSize: book.size(),
                        calculationTime: Math.abs(Duration.between(t0, LocalDateTime.now()).toMillis())
                )
        )
    }

    static void main(String[] args) {
// TODO reverse order (eg: find CBA instead of ABC)
// TODO find the shortest, and not the *first* sequence
        final String search = "JONATHAN"
        def radius = 5

        def elsOptional = new ShortestJump().retrieveELS(search, book)
        if (!elsOptional.isPresent()) {
            println("No result found! :-(")
        }
        def equidistantLetterSequence = elsOptional.get()
        println "Book size : ${equidistantLetterSequence.bookSize} letters"
        println "Calculation time : ${equidistantLetterSequence.calculationTime} ms"
        println "Shortest sequence : ${equidistantLetterSequence.distance} letters"
        println "First letter index : #${equidistantLetterSequence.firstLetterIndex}"
        println("Matrix: \n")
        println(equidistantLetterSequence.matrix)
    }
}