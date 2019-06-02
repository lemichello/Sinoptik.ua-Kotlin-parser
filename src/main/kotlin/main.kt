import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

fun getPage(): Document {
    val url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D1%80%D0%BE%D0%B2%D0%BD%D0%BE"

    return Jsoup.parse(URL(url), 5000)
}

fun getCurrentDay(document: Document): String {
    val dayData = document.select("div[class=main loaded]")

    return dayData.select("p[class=day-link]").first().text() + " " +
            dayData.select("p[class=date dateFree]").first().text() + " " +
            dayData.select("p[class=month]").first().text()
}

// Prints whole forecast.
fun printForecast(document: Document) {
    val temperatures = document.select("tr[class=temperature]").select("td")!!
    val pressures = document.select("tr[class=gray]").select("td")!!
    val humidity = document
            .select("table[class=weatherDetails]")
            .select("tr")[6]
            .select("td")!!

    val windSpeeds = document
            .select("table[class=weatherDetails]")
            .select("tr")[7]
            .select("td")!!

    // Evening, morning, day, night.
    val dayTimes = document.select("tr[class=gray time]").select("td")!!

    for(i in 0 until dayTimes.count()) {
        print(String.format("%-10s", dayTimes[i].text()))
        print(String.format("%13s", temperatures[i].text()))
        print(String.format("%17s", pressures[i].text()))
        print(String.format("%19s", humidity[i].text()))
        println(String.format("%21s", windSpeeds[i].text()))
    }
}

fun printHeaders(currentDay: String) {
    println("Сегодня : $currentDay")
    print(String.format("%-10s", "Время"))
    print(String.format("%13s", "Температура"))
    print(String.format("%17s", "Давление"))
    print(String.format("%19s", "Влажность, %"))
    println(String.format("%21s", "Ветер, м/сек"))
}

fun main() {
    val document = getPage()
    val currentDay = getCurrentDay(document)

    printHeaders(currentDay)
    printForecast(document)
}