package ga.caseyavila.velcro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import ga.caseyavila.velcro.activities.MainActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;
import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class LoginService extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;

    public LoginService(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            if (sharedPreferences.contains("studentId") && sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
                casey.setStudentId(sharedPreferences.getString("studentId", "error"));
                casey.setUsername(sharedPreferences.getString("username", "error"));
                casey.setPassword(sharedPreferences.getString("password", "error"));
            } else {
                casey.findStudentId();
            }

            casey.getReportCard();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", casey.getUsername());
            editor.putString("password", casey.getPassword());
            editor.putString("studentId", casey.getStudentId());
            editor.apply();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();

        if (casey.isLoggedIn()) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {  // Alert user if username and password doesn't match
            activity.findViewById(R.id.login_notification).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.login_button).setEnabled(true);
            activity.findViewById(R.id.username_layout).setEnabled(true);
            activity.findViewById(R.id.password_layout).setEnabled(true);
        }
        activity.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }
}
