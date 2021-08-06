import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

suspend fun main() {
    val releasedMoviesCategory = "Category:Released_Movies"
    val upcomingMoviesCategory = "Category:Upcoming_Movies"

    val client = MediaWikiApiClient("https://marvelcinematicuniverse.fandom.com/")

    println("Fetching data")
    val movies = client.getMoviesInCategory(releasedMoviesCategory) +
            client.getMoviesInCategory(upcomingMoviesCategory)
    println("Data fetched")

    val movieReleases = TimelineData(
        listOf(
            Series(
                listOf(),
                movies.filter { it.releases.isNotEmpty() }
                    .map(Movie::toEvent)
            )
        )
    )

    File("out/mcu.json").writeText(Json.encodeToString(movieReleases))
}
