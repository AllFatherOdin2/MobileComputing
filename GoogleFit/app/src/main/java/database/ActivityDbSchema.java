package database;

/**
 * Created by Helios on 3/14/2016.
 */
public class ActivityDbSchema {
    public static final class ActivityTable {
        public static final String NAME = "activities";
        public static final class Cols {
            public static final String UUID = "UUID";
            public static final String ACTIVITY_NAME = "activityName";
            public static final int START_TIME = 0;
        }
    }
}
