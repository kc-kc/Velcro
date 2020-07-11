package ga.caseyavila.velcro.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.CourseActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseAsyncTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;
    private int period;

    public CourseAsyncTask(Activity activity, int courseNumber) {
        activityReference = new WeakReference<>(activity);
        period = courseNumber;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findProgressReport(period);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();

        ProgressBar courseProgressBar = activity.findViewById(R.id.course_progress_bar);

        courseProgressBar.setEnabled(true);  //Make progressbar appear
        ((CourseActivity) activity).addCards();  //Load cards
        courseProgressBar.setVisibility(View.INVISIBLE);  //Make progress bar disappear after loading cards
    }
}
