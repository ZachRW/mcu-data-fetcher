import java.time.LocalDate

fun LocalDate.toCommonDate() =
    CommonDate(year, monthValue, dayOfMonth)
