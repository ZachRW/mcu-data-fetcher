import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json as KotlinxJson

class MediaWikiApiClient(url: String) {
    private val url = if (url.endsWith('/')) {
        url
    } else {
        "$url/"
    }

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(KotlinxJson {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getMoviesInCategory(categoryTitle: String) =
        getCategoryMemberIDs(categoryTitle).map { pageID ->
            Movie.fromParseInfo(getPageContent(pageID))
        }

    suspend fun getPageContent(pageID: Int): ParseInfo =
        client.get<ParseResponse>(
            "${url}api.php?" +
                    "action=parse&" +
                    "pageid=$pageID&" +
                    "format=json&" +
                    "prop=wikitext"
        ).parseInfo

    suspend fun getCategoryMemberIDs(categoryTitle: String): List<Int> =
        client.get<CategoryResponse>(
            "${url}api.php?" +
                    "action=query&" +
                    "list=categorymembers&" +
                    "cmtitle=$categoryTitle&" +
                    "cmlimit=500&" +
                    "format=json"
        ).pageIDs
}
