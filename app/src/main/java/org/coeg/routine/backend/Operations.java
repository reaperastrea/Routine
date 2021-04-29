package org.coeg.routine.backend;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Operations {
    public static int countLateMinutes(Routine routine, History history) {
        Date lateTime = history.getTime();
        Date routineTime = routine.getTime();
        long late = lateTime.getTime() - routineTime.getTime();
        int mins = (int)(late - (late % 60)) / 60;

        return mins;
    }
}
