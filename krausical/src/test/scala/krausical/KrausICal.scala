package krausical

import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.parameter.Value
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Date
import net.fortuna.ical4j.model.Property
import org.joda.time.LocalDate
import scalaj.http.Http
import java.util.{ Calendar ⇒ JCalendar }
import net.fortuna.ical4j.model.component.VEventFactory

class KrausICal extends App {

  def location(offset: Int) = {
    val html = Http.get("http://jautz.org/kraus/index.cgi?offset=" + offset).asString
    val extract = """(?s).+<b>(.+)</b>.+""".r
    val extract(location) = html
  }


  val today = new LocalDate()

  //  val today.getDayOfWeek()

}