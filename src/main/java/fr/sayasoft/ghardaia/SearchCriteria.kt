package fr.sayasoft.ghardaia

import org.apache.commons.lang3.StringUtils

class SearchCriteria(
        var search: String? = StringUtils.EMPTY,
        var book: String? = StringUtils.EMPTY,
        var radius: Int? = 5,
        var includeReverseOrder: Boolean? = false,
        var minDistance: Int? = 0,
        var maxDistance: Int? = 1000,
        var searchFromIndex: Int? = 0,
        var searchToIndex: Int? = book?.length,
        var searchMode: SearchMode? = SearchMode.ITERATIVE) {
}