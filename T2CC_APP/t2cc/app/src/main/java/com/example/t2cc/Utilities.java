package com.example.t2cc;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

class Utilities {

  static Date getMessagesCutOffDate() {
    Calendar tCal = Calendar.getInstance();
    int MESSAGES_CUTOFF_DAYS = 90;
    Date today = tCal.getTime();
    tCal.add(Calendar.DAY_OF_YEAR, -MESSAGES_CUTOFF_DAYS);
    Date cutOffDate = tCal.getTime();
    return cutOffDate;
  }

  static String getCurrentTerm(Date currentDate, Integer currentYear) {
    String currentAcademicTerm;
    Calendar tCal = Calendar.getInstance();
    tCal.set(currentYear, 0, 27);
    Date springStart = tCal.getTime();
    tCal.set(currentYear, 4, 19);
    Date springEnd = tCal.getTime();
    tCal.set(currentYear, 4, 26);
    Date summerStart = tCal.getTime();
    tCal.set(currentYear, 7, 4);
    Date summerEnd = tCal.getTime();
    tCal.set(currentYear, 7, 31);
    Date fallStart = tCal.getTime();
    tCal.set(currentYear, 11, 21);
    Date fallEnd = tCal.getTime();
    tCal.set(currentYear, 0, 2);
    Date winterStart = tCal.getTime();
    tCal.set(currentYear, 0, 22);
    Date winterEnd = tCal.getTime();
    if (currentDate.after(springStart) && currentDate.before(springEnd)) {
      currentAcademicTerm = "Spring";
    } else if (currentDate.after(summerStart) && currentDate.before(summerEnd)) {
      currentAcademicTerm = "Summer";
    } else if (currentDate.after(fallStart) && currentDate.before(fallEnd)) {
      currentAcademicTerm = "Fall";
    } else if (currentDate.after(winterStart) && currentDate.before(winterEnd)) {
      currentAcademicTerm = "Winter";
    } else {
      currentAcademicTerm = "Unavailable";
    }
    return currentAcademicTerm;
  }
}
