package fr.sayasoft.ghardaia

import org.apache.commons.lang3.StringUtils

import static java.nio.file.Paths.get

//@Log4j2
class R2L2L2R {


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
}