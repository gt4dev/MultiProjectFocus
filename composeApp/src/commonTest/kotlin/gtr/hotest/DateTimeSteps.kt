package gtr.hotest

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

object DateTimeSteps {

    // todo: rename to Time, Duration ... no need to repeat String
    // sample input: 12:34
    data class TimeString(val txt: String) {
        fun HOTestCtx.toInstant(): Instant {
            return this.timeToInstant(txt)
        }
    }

    // sample input:  1h30
    data class DurationString(val txt: String) {
        fun toDuration(): Duration {
            val parts = txt.split("h")
            require(parts.size == 2) { "Invalid format: $txt" }
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()

            return hours.hours + minutes.minutes
        }
    }

    // "2025-12-10"
    fun HOTestCtx.`given example hours implicitly have date`(data: String) {
        this["example-hours-implicit-date"] = LocalDate.parse(data)
    }

    // 12:34
    fun HOTestCtx.timeToInstant(time: String): Instant {
        val time = LocalTime.parse(time)
        val date: LocalDate =
            try {
                this["example-hours-implicit-date"]
            } catch (e: Exception) {
                LocalDate.parse("2025-12-20")
            }
        return toInstant(date, time)
    }
}


private fun toInstant(date: LocalDate, time: LocalTime): Instant {
    val zone = TimeZone.of("Europe/Warsaw")
    val localDateTime = LocalDateTime(
        date = date,
        time = time
    )
    return localDateTime.toInstant(zone)
}