import kotlinx.serialization.encodeToString as myJsonEncode
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

    val movieReleases = movies.map(Movie::toMovieRelease)

    File("out/mcuData.txt").writeText(Json.myJsonEncode(movieReleases))
}
