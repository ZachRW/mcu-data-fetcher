suspend fun main() {
    val releasedMoviesCategory = "Category:Released_Movies"
    val upcomingMoviesCategory = "Category:Upcoming_Movies"

    val client = MediaWikiApiClient("https://marvelcinematicuniverse.fandom.com/")

    val movies = client.getMoviesInCategory(releasedMoviesCategory) +
            client.getMoviesInCategory(upcomingMoviesCategory)

    for (movie in movies) {
        println(movie.title)
        for (release in movie.releases) {
            if (release.smallText.isNotEmpty()) {
                println(release.smallText)
            }
        }
        println()
    }
}
