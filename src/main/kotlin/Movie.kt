import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class Movie(
    val title: String,
    val releases: List<Release>
) {
    fun toEvent(): Event {
        if (releases.isEmpty()) {
            throw IllegalStateException("""Movie "$title" does not have any releases.""")
        }

        val releasesBySmallText = mutableMapOf<String, LocalDate>()
        for ((date, smallText) in releases.reversed()) {
            releasesBySmallText[smallText] = date
        }

        val releaseDate: LocalDate =
            releasesBySmallText[""]
                ?: releasesBySmallText["international"]
                ?: releasesBySmallText["U.S."]
                ?: releases.first().date

        return Event(title, releaseDate.toCommonDate())
    }

    companion object {
        private val titleRegex = Regex("""^\|title = ''(.+)''$""", RegexOption.MULTILINE)
        private val releasesRegex = Regex("""^\|release = (.+)$""", RegexOption.MULTILINE)
        private val releaseRegex = Regex("""^(\S+ \S+ [^ <]+)(?: <small>\(([^)]+)\)</small>)?(?:<[^>]+>)?$""")
        private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")

        fun fromParseInfo(parseInfo: ParseInfo) =
            Movie(
                getTitle(parseInfo),
                getReleases(parseInfo.wikitext)
            )

        private fun getTitle(parseInfo: ParseInfo): String {
            val titleMatch = titleRegex.find(parseInfo.wikitext)

            return if (titleMatch == null) {
                parseInfo.title
            } else {
                titleMatch.groupValues[1]
            }.removeSuffix(" (film)")
        }

        private fun getReleases(wikitext: String): List<Release> {
            val releasesMatch = releasesRegex.find(wikitext)

            return if (releasesMatch == null) {
                listOf()
            } else {
                val releasesStr = releasesMatch.groupValues[1]

                releasesStr.split("<br>").map(::getRelease)
            }
        }

        private fun getRelease(releaseStr: String): Release {
            val releaseMatch = releaseRegex.find(releaseStr)
                ?: throw IllegalArgumentException("""Failed to parse release dates in wikitext: "$releaseStr".""")
            val (dateStr, location) = releaseMatch.destructured

            val date = try {
                LocalDate.parse(dateStr, dateFormatter)
            } catch (dateTimeParseException: DateTimeParseException) {
                throw IllegalArgumentException(
                    "Failed to parse release dates in wikitext.",
                    dateTimeParseException
                )
            }

            return Release(date, location)
        }
    }
}

data class Release(
    val date: LocalDate,
    val smallText: String
)
