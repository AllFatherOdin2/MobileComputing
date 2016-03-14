package name.heqian.cs528.googlefit;


/**
 * Created by Helios on 3/14/2016.
 */
public class Activity {
    private String activityName;
    private long startTime;

    public Activity(String activityName){
        this.activityName = activityName;
        this.startTime = System.currentTimeMillis();
    }

    public String getActivityName(){
        return activityName;
    }
    public long getStartTime(){
        return startTime;
    }

}
