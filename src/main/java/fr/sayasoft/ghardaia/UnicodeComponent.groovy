package fr.sayasoft.ghardaia

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import groovy.util.logging.Log4j2
import org.apache.commons.lang3.StringUtils

import static java.nio.file.Paths.get

@Log4j2
class UnicodeComponent {

    static final BiMap<Character, Character> HEBREW_TO_LATIN_TRANSLITTERATION = HashBiMap.create([
            ('׆' as Character): ("F" as Character), //  "Nun hafucha" /!\ non-intuitive
            ('א' as Character): ("A" as Character),
            ('ב' as Character): ("B" as Character),
            ('ג' as Character): ("G" as Character),
            ('ד' as Character): ("D" as Character),
            ('ה' as Character): ("E" as Character),
            ('ו' as Character): ("V" as Character),
            ('ז' as Character): ("Z" as Character),
            ('ח' as Character): ("H" as Character),
            ('ט' as Character): ("X" as Character), // /!\ non-intuitive
            ('י' as Character): ("Y" as Character),
            ('ך' as Character): ("k" as Character),
            ('כ' as Character): ("K" as Character),
            ('ל' as Character): ("L" as Character),
            ('ם' as Character): ("m" as Character),
            ('מ' as Character): ("M" as Character),
            ('ן' as Character): ("n" as Character),
            ('נ' as Character): ("N" as Character),
            ('ס' as Character): ("S" as Character),
            ('ע' as Character): ("O" as Character),
            ('ף' as Character): ("p" as Character),
            ('פ' as Character): ("P" as Character),
            ('ץ' as Character): ("c" as Character),
            ('צ' as Character): ("C" as Character), // /!\ non-intuitive
            ('ק' as Character): ("Q" as Character),
            ('ר' as Character): ("R" as Character),
            ('ש' as Character): ("W" as Character), // /!\ non-intuitive
            ('ת' as Character): ("T" as Character), // /!\ not to be confused with Teth / X
    ])

    static final BiMap<Character, Character> LATIN_TO_HEBREW_TRANSLITTERATION = HEBREW_TO_LATIN_TRANSLITTERATION.inverse()
//    static final BiMap<Character, Character> LATIN_TO_HEBREW_TRANSLITTERATION = HEBREW_TO_LATIN_TRANSLITTERATION.entrySet().stream().collect(Collectors.toMap())

    /** Extract the letters of the text, and sets them as a L2R String */
    String formatFile(String filePath) {
        final StringBuffer stringBuffer = new StringBuffer()
        final String fileContents = new File(getClass().getResource(filePath).toURI()).getText('UTF-8')

//        log.debug("Original Content: ${fileContents}")
        final List<String> verses = fileContents.split("\n").toList()
        verses.each { String verse ->
            for (int i = 0; i < verse.length(); i++) {
                stringBuffer.append(verse.charAt(i))
            }
            // clean "-", blanks, Latin characters, etc.
        }
        String answer = stringBuffer.toString()
        answer = StringUtils.replaceAll(answer, "\r", "")
        answer = StringUtils.replaceAll(answer, " ", "")
        answer = StringUtils.replaceAll(answer, "־", "")
        return answer
    }

    String formatFolder(String folderPath) {
        final StringBuffer stringBuffer = new StringBuffer()
        get(folderPath).eachFileMatch(~/.*.txt/) { file -> stringBuffer.append(formatFile("/" + file.fileName.toString())) }
        return stringBuffer.toString()
    }

    String translitterateToLatin(String szHebrew) {
        final StringBuilder stringBuilder = new StringBuilder()
        for (int i = 0; i < szHebrew.size(); i++) {
            stringBuilder.append(HEBREW_TO_LATIN_TRANSLITTERATION[szHebrew.charAt(i)])
        }
        return stringBuilder.toString()
    }

    String translitterateToHebrew(String szLatin) {
        final StringBuilder stringBuilder = new StringBuilder()
        for (int i = 0; i < szLatin.size(); i++) {
            stringBuilder.append(LATIN_TO_HEBREW_TRANSLITTERATION[szLatin.charAt(i)])
        }
        return stringBuilder.toString()
    }
}