import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class MovieRelease(
    val title: String,
    val date: Date? = null
)

@Serializable
data class Date(
    val year: Int,
    val month: Int,
    val day: Int
)

fun LocalDate.toDate() =
    Date(year, monthValue, dayOfMonth)
