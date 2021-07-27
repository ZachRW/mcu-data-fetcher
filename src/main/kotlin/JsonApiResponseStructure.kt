import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ParseResponse private constructor(
    private val parse: ParseObj
) {
    val parseInfo: ParseInfo
        get() = ParseInfo(parse.title, parse.wikitext.asterisk)

    @Serializable
    private class ParseObj(
        val title: String,
        val wikitext: WikitextObj
    )

    @Serializable
    private class WikitextObj(
        @SerialName("*")
        val asterisk: String
    )
}

data class ParseInfo(
    val title: String,
    val wikitext: String
)

@Serializable
class CategoryResponse private constructor(
    private val query: QueryObj
) {
    val pageIDs: List<Int>
        get() = query.categoryMembers.map { it.pageID }

    @Serializable
    private class QueryObj(
        @SerialName("categorymembers")
        val categoryMembers: List<PageObj>
    )

    @Serializable
    private class PageObj(
        @SerialName("pageid")
        val pageID: Int
    )
}
