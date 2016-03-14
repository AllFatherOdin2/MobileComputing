package name.heqian.cs528.googlefit;


/**
 * Created by Helios on 3/14/2016.
 */
public class Activity {
    private String activityName;
    private int startTime;

    public Activity(String activityName, int startTime){
        this.activityName = activityName;
        this.startTime = startTime;
    }

    public String getActivityName(){
        return activityName;
    }
    public int getStartTime(){
        return startTime;
    }

}
