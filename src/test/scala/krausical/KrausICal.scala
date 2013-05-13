package krausical

import org.joda.time.DateTime
import org.scala_tools.time.Imports.RichDateTime

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import scalaj.http.Http

object KrausICal extends App {

  def location(offset: Int) = {
    val html = Http.get("http://jautz.org/kraus/index.cgi?offset=" + offset).asString
    val extract = """(?s).+<b>(.+)</b>.+""".r
    val extract(location) = html
    location
  }

  val cal = ICalUtils.newCalendar

  val timezone = ICalUtils.timeZone("Europe/Berlin")
  val tz = timezone.getVTimeZone()

  val today = DateTime.now()

  for (i ← 0 to 100) {
    val dt = today + i

    val d = dt.day.get
    val m = dt.month.get
    val y = dt.year.get

    val start = ICalUtils.date(timezone, y, m, d, 12, 0)
    val end = ICalUtils.date(timezone, y, m, d, 12, 30)

    ICalUtils.addEvent("kraus", location(i), cal, tz, start, end)
  }

  ICalUtils.out(cal, System.out)
}
