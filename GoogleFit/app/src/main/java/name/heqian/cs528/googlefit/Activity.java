package name.heqian.cs528.googlefit;


import java.util.UUID;

/**
 * Created by Helios on 3/14/2016.
 */
public class Activity {
    private String id;
    private String activityName;
    private long startTime;

    public Activity(String activityName){
        this.id = UUID.randomUUID().toString();
        this.activityName = activityName;
        this.startTime = System.currentTimeMillis();
    }

    public Activity(String id, String activityName, long startTime){
        this.id = id;
        this.activityName = activityName;
        this.startTime = startTime;
    }

    public String getid(){
        return id;
    }

    public String getActivityName(){
        return activityName;
    }
    public long getStartTime(){
        return startTime;
    }

}
