package ga.caseyavila.velcro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;

import static ga.caseyavila.velcro.LoginActivity.casey;


public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {

    private Context context;
    private int period;

    CourseViewAdapter(Context ctx, int prd) {
        context = ctx;
        period = prd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assignment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.assignmentName.setText(casey.getAssignmentName(period, position));
        holder.assignmentCategory.setText(casey.getAssignmentCategory(period, position));
        holder.assignmentScoreEarned.setText(casey.getAssignmentScoreEarned(period, position));
        holder.assignmentScorePossible.setText(casey.getAssignmentScorePossible(period, position));
//        holder.assignmentPercentage.setText(String.valueOf(casey.getAssignmentPercentage(Float.parseFloat(String.valueOf(holder.assignmentScoreEarned.getText())), Float.parseFloat(String.valueOf(holder.assignmentScorePossible.getText())))));
        holder.assignmentPercentage.setText(casey.getAssignmentPercentage(period, position));
    }

    @Override
    public int getItemCount() {
            return casey.getNumberOfAssignments(period);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView assignmentName;
        MaterialTextView assignmentCategory;
        MaterialTextView assignmentScoreEarned;
        MaterialTextView assignmentScorePossible;
        MaterialTextView assignmentPercentage;

        public ViewHolder(View itemView) {
            super(itemView);

            assignmentName = itemView.findViewById(R.id.assignment_name);
            assignmentCategory = itemView.findViewById(R.id.assignment_category);
            assignmentScoreEarned = itemView.findViewById(R.id.assignment_score_earned);
            assignmentScorePossible = itemView.findViewById(R.id.assignment_score_possible);
            assignmentPercentage = itemView.findViewById(R.id.assignment_percentage);
        }
    }
}
