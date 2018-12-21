package fr.sayasoft.ghardaia

import org.junit.jupiter.api.Test

class UnicodeComponentUnitTest {
    UnicodeComponent unicodeComponent = new UnicodeComponent();

    @Test
    void translitterateToLatin() {
        assert "EOBRYT" == unicodeComponent.translitterateToLatin("העברית")
        assert "ABCDEFGHKLMNOPQRSTVWXYZ" == unicodeComponent.translitterateToLatin("אבצדה׆גחכלמנעפקרסתושטיז")
    }

    @Test
    void translitterateToHebrew() {
        assert "העברית" == unicodeComponent.translitterateToHebrew("EOBRYT")
        assert "אבצדה׆גחכלמנעפקרסתושטיז" == unicodeComponent.translitterateToHebrew("ABCDEFGHKLMNOPQRSTVWXYZ")
    }

}