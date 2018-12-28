package fr.sayasoft.ghardaia

import groovy.util.logging.Log4j2
import org.apache.commons.lang3.StringUtils
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
        if (optionalEquidistantLetterSequence.isPresent()) {
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

    Optional<EquidistantLetterSequence> retrieveELS(String search, String book, Integer radius = 5, Boolean includeReverseOrder = false, Integer minDistance = 0, Integer maxDistance = 1000, SearchMode searchMode = SearchMode.ITERATIVE) {
        log.info("Searching for      : ${search}")
        log.info("Searching in book  : ${StringUtils.substring(book, 0, 30)}...")
        final LocalDateTime t0 = LocalDateTime.now();
        def searchSize = search.size()
        Integer shortestEquidistantSequence, firstLetterIndex

        switch (searchMode) {
            case SearchMode.REGEXP:
                (shortestEquidistantSequence, firstLetterIndex) = searchWithRegexp(minDistance, maxDistance, searchSize, search, book)
                break
            case SearchMode.ITERATIVE:
            default:
                (shortestEquidistantSequence, firstLetterIndex) = searchIteratively(search, minDistance, maxDistance, searchSize, book)
                break
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
                calculationTime: Math.abs(Duration.between(t0, LocalDateTime.now()).toMillis()),
                searchMode: searchMode
        )

        log.info("searchMode         : ${equidistantLetterSequence.searchMode}")
        log.info("Book size          : ${equidistantLetterSequence.bookSize} letters")
        log.info("Calculation time   : ${equidistantLetterSequence.calculationTime} ms")
        log.info("Shortest sequence  : ${equidistantLetterSequence.distance} letters")
        log.info("First letter index : #${equidistantLetterSequence.firstLetterIndex}")
        log.info("Matrix             : \n" + StringUtils.leftPad("*", radius + 1) + "\n" + equidistantLetterSequence.matrix + StringUtils.leftPad("*", radius + 1))

        return Optional.of(equidistantLetterSequence)
    }


    protected List searchWithRegexp(int minDistance, int maxDistance, int searchSize, String search, String book) {
        Integer shortestEquidistantSequence = null, firstLetterIndex = null

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
        [shortestEquidistantSequence, firstLetterIndex]
    }


    private List searchIteratively(String search, int minDistance, int maxDistance, int searchSize, String book) {
        Integer shortestEquidistantSequence = null, firstLetterIndex = null
        Integer currentIndex
        final List<Character> characters = search.toCharArray().toList();
        Boolean match

        for (int step = minDistance; step < maxDistance; step++) {
            if (log.isTraceEnabled()) log.trace("Testing step: ${step}")
            currentIndex = -1

            // TODO put a while currentIndex < bookSize+ something
            while ((currentIndex + (searchSize * step) < book.size())) {
                currentIndex = StringUtils.indexOf(book, "" + characters.get(0), currentIndex + 1)
                // TODO protect against currentIndex+1>bookSize

                if (currentIndex < 0) break // break while

                if (log.isTraceEnabled()) log.trace("Current index for 1st letter: ${currentIndex}")

                match = true
                for (int i = 0; i < characters.size() && (currentIndex + (i * step) < book.size()); i++) {
                    final Character currentChar = characters[i]
                    if (book.charAt(currentIndex + i * (step + 1)).toUpperCase().equals(currentChar.toUpperCase())) {
                        match &= true
                        if ((i > 0) && log.isTraceEnabled()) log.trace("Found letter: ${currentChar}!")
                    } else {
                        match = false
                        break // break for i
                    }
                }
                if (match) {
                    shortestEquidistantSequence = step
                    firstLetterIndex = currentIndex
                    log.trace("Found complete match!")
                    break // break while
                }
            }
            if (match) break // break for step
        }
        [shortestEquidistantSequence, firstLetterIndex]
    }
}