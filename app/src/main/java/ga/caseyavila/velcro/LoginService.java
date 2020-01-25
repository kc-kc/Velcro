package ga.caseyavila.velcro;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.LoginActivity.casey;

public class LoginService extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;

    LoginService(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            User.isLoggedIn = false;
            casey.getMainDocument();
            casey.loginChecker();
            casey.findNumberOfPeriods();
            casey.teacherFinder();
            casey.gradeFinder();
            casey.classFinder();
            casey.percentageFinder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {

        Activity activity = activityReference.get();

        if (User.isLoggedIn) {  // Alert user if username and password doesn't match
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.findViewById(R.id.login_notification).setVisibility(View.INVISIBLE);
        } else {
            activity.findViewById(R.id.login_notification).setVisibility(View.VISIBLE);
        }
        activity.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }
}