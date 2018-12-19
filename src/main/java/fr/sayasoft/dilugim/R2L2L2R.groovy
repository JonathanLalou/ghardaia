package fr.sayasoft.dilugim


import org.apache.commons.lang3.StringUtils

//@Log4j2
class R2L2L2R {

    String filePath

    /** Extract the letters of the text, and sets them as a L2R String */
    String format() {
        final StringBuffer stringBuffer = new StringBuffer()
        final String fileContents = new File(filePath).getText('UTF-8')
//        log.debug("Original Content: ${fileContents}")
        final List<String> verses = fileContents.split("\n").toList()
        verses.each { String verse ->
            for (int i = 0; i < verse.length(); i++) {
                stringBuffer.append(verse.charAt(i))
            }
            // clean "-", blanks, etc.
        }
        String answer = stringBuffer.toString()
        answer = StringUtils.replaceAll(answer, "\r", "")
        answer = StringUtils.replaceAll(answer, " ", "")
        answer = StringUtils.replaceAll(answer, "־", "")
        return answer
    }
}