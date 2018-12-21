package fr.sayasoft.ghardaia

import groovy.util.logging.Log4j2
import org.springframework.stereotype.Component

import java.time.Duration
import java.time.LocalDateTime

import static org.apache.commons.lang3.StringUtils.substring

@Log4j2
@Component
class ElsComponent {
//    @Autowired
    UnicodeComponent unicodeComponent = new UnicodeComponent()

    Optional<EquidistantLetterSequence> translitterateAndretrieveELS(String search, String book, radius = 5, includeReverseOrder = false, minDistance = 0, maxDistance = 1000) {
        final String latinSearch = unicodeComponent.translitterateToLatin(search)
        final String latinBook = unicodeComponent.translitterateToLatin(book)
        def optionalEquidistantLetterSequence = retrieveELS(latinSearch, latinBook, radius, includeReverseOrder, minDistance, maxDistance)
        if(optionalEquidistantLetterSequence.isPresent()){
            def equidistantLetterSequence = new EquidistantLetterSequence(
                    distance: optionalEquidistantLetterSequence.get().distance,
                    firstLetterIndex: optionalEquidistantLetterSequence.get().firstLetterIndex,
                    matrix: unicodeComponent.translitterateToHebrew(optionalEquidistantLetterSequence.get().matrix),
                    bookSize: optionalEquidistantLetterSequence.get().bookSize,
                    calculationTime: optionalEquidistantLetterSequence.get().calculationTime
            )

            log.info("Book size          : ${equidistantLetterSequence.bookSize} letters")
            log.info("Calculation time   : ${equidistantLetterSequence.calculationTime} ms")
            log.info("Shortest sequence  : ${equidistantLetterSequence.distance} letters")
            log.info("First letter index : #${equidistantLetterSequence.firstLetterIndex}")
            log.info("Matrix             : \n" + equidistantLetterSequence.matrix)

            return Optional.of(
                    equidistantLetterSequence
            )

        }
        return Optional.empty()
    }

    Optional<EquidistantLetterSequence> retrieveELS(String search, String book, radius = 5, includeReverseOrder = false, minDistance = 0, maxDistance = 1000) {
        log.info("Searching for...   : ${search}")
        final LocalDateTime t0 = LocalDateTime.now();
        def searchSize = search.size()
        final Integer shortestEquidistantSequence, firstLetterIndex

        for (int step = minDistance; step < maxDistance; step++) {
            if (log.isTraceEnabled()) log.trace("Testing step: ${step}")
            final StringBuilder stringBuilder = new StringBuilder("((\\w)*)")
            for (int i = 0; i < searchSize; i++) {
                stringBuilder.append("(${search.charAt(i)})").append("((\\w){$step})")
            }
            def regexp = stringBuilder.toString()
            if (log.isTraceEnabled()) log.trace("with regexp: ${regexp}")
            def group = (book =~ regexp)
            // NB: by default, the regexp gets the shortest matching string
            if (group.hasGroup() && group.size() > 0) {
                shortestEquidistantSequence = step
                firstLetterIndex = group[0][1].size()
                break
            }
        }
        if (null == shortestEquidistantSequence) {
            return Optional.empty()
        }
        final StringBuilder matrixBuilder = new StringBuilder()
        for (int i = 0; i < searchSize; i++) {
            matrixBuilder.append(substring(book, firstLetterIndex + (i * shortestEquidistantSequence) - radius + i, firstLetterIndex + (i * shortestEquidistantSequence) + radius + i + 1)).append("\n")
        }
        def equidistantLetterSequence = new EquidistantLetterSequence(
                distance: shortestEquidistantSequence,
                firstLetterIndex: firstLetterIndex,
                matrix: matrixBuilder.toString(),
                bookSize: book.size(),
                calculationTime: Math.abs(Duration.between(t0, LocalDateTime.now()).toMillis())
        )

        log.info("Book size          : ${equidistantLetterSequence.bookSize} letters")
        log.info("Calculation time   : ${equidistantLetterSequence.calculationTime} ms")
        log.info("Shortest sequence  : ${equidistantLetterSequence.distance} letters")
        log.info("First letter index : #${equidistantLetterSequence.firstLetterIndex}")
        log.info("Matrix             : \n" + equidistantLetterSequence.matrix)

        return Optional.of(
                equidistantLetterSequence
        )
    }
}