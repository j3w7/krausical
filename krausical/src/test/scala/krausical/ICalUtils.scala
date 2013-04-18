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
import net.fortuna.ical4j.data.CalendarOutputter
import java.io.FileOutputStream
import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import java.util.GregorianCalendar
import net.fortuna.ical4j.util.UidGenerator
import net.fortuna.ical4j.model.DateTime
import java.net.URI
import net.fortuna.ical4j.model.parameter.Cn
import net.fortuna.ical4j.model.parameter.Role
import net.fortuna.ical4j.model.property.Location

object ICalUtils {

  def newCalendar = {
    val calendar = new Calendar();
    calendar.getProperties().add(new ProdId("-//j3w7//krausical//EN"));
    calendar.getProperties().add(Version.VERSION_2_0);
    calendar.getProperties().add(CalScale.GREGORIAN);
    calendar
  }

  def addEvent(icsCalendar: Calendar) = {
    // Create a TimeZone
    val registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    val timezone = registry.getTimeZone("America/Mexico_City");
    val tz = timezone.getVTimeZone();

    // Start Date is on: April 1, 2008, 9:00 am
    val startDate = new GregorianCalendar();
    startDate.setTimeZone(timezone);
    startDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
    startDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
    startDate.set(java.util.Calendar.YEAR, 2008);
    startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
    startDate.set(java.util.Calendar.MINUTE, 0);
    startDate.set(java.util.Calendar.SECOND, 0);

    // End Date is on: April 1, 2008, 13:00
    val endDate = new GregorianCalendar();
    endDate.setTimeZone(timezone);
    endDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
    endDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
    endDate.set(java.util.Calendar.YEAR, 2008);
    endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
    endDate.set(java.util.Calendar.MINUTE, 0);
    endDate.set(java.util.Calendar.SECOND, 0);

    // Create the event
    val eventName = "kraus";
    val start = new DateTime(startDate.getTime());
    val end = new DateTime(endDate.getTime());
    val meeting = new VEvent(start, end, eventName);

    meeting.getProperties().add(new Location(""));

    // add timezone info..
    meeting.getProperties().add(tz.getTimeZoneId());

    // generate unique identifier..
    val ug = new UidGenerator("uidGen");
    val uid = ug.generateUid();
    meeting.getProperties().add(uid);

    // Add the event 
    icsCalendar.getComponents().add(meeting);
  }

  def fout(calendar: Calendar, ics_filename: String) = {
    val fout = new FileOutputStream(ics_filename);

    val outputter = new CalendarOutputter();
    outputter.output(calendar, fout);
  }

}