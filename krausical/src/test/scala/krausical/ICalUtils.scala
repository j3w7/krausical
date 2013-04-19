package krausical

import java.io.FileOutputStream
import java.util.Calendar.APRIL
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE
import java.util.Calendar.MONTH
import java.util.Calendar.SECOND
import java.util.Calendar.YEAR
import java.util.GregorianCalendar
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.Location
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.TimeZone
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.util.UidGenerator
import net.fortuna.ical4j.model.component.VTimeZone
import java.io.OutputStream
import org.joda.time.{ DateTime ⇒ JoDateTime }
import org.scala_tools.time.Imports._

object ICalUtils {

  def newCalendar = {
    val calendar = new Calendar()
    calendar.getProperties().add(new ProdId("-//j3w7//krausical//EN"))
    calendar.getProperties().add(Version.VERSION_2_0)
    calendar.getProperties().add(CalScale.GREGORIAN)
    calendar
  }

  def timeZone(tzname: String) = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone(tzname)

  // TODO better: joda.DateTime -> GregorianCalendar
  def date(timezone: TimeZone, year: Int, month: Int, day: Int, hour: Int, minute: Int): DateTime =
    date(timezone, year, month, day, hour, minute, 0)

  def date(timezone: TimeZone, year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) = {
    val date = new GregorianCalendar()
    date.setTimeZone(timezone)
    date.set(MONTH, month)
    date.set(DAY_OF_MONTH, day)
    date.set(YEAR, year)
    date.set(HOUR_OF_DAY, hour)
    date.set(MINUTE, minute)
    date.set(SECOND, second)
    new DateTime(date.getTime())
  }

  def addEvent(name: String, location: String, icsCalendar: Calendar, timeZone: VTimeZone, start: DateTime, end: DateTime) = {
    // Create the event
    val event = new VEvent(start, end, name)

    event.getProperties().add(new Location(location))

    // add timezone info..
    event.getProperties().add(timeZone.getTimeZoneId())

    // generate unique identifier..
    val ug = new UidGenerator("uidGen")
    val uid = ug.generateUid()
    event.getProperties().add(uid)

    // Add the event 
    icsCalendar.getComponents().add(event)
  }

  def fout(ics: Calendar, ics_filename: String) = out(ics, new FileOutputStream(ics_filename))

  def out(calendar: Calendar, outputStream: OutputStream) = {
    val outputter = new CalendarOutputter()
    outputter.output(calendar, outputStream)
  }
}