package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {

    private Context context;
    private int period;

    public CourseViewAdapter(Context ctx, int prd) {
        context = ctx;
        period = prd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPES.Header) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assignment, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.Normal) {
            holder.assignmentName.setText(casey.getAssignmentName(period, position));
            holder.assignmentCategory.setText(casey.getAssignmentCategory(period, position));
            holder.assignmentScoreEarned.setText(casey.getAssignmentScoreEarned(period, position));
            holder.assignmentScorePossible.setText(casey.getAssignmentScorePossible(period, position));
            holder.assignmentPercentage.setText(casey.getAssignmentPercentage(period, position));
        }
    }

    @Override
    public int getItemCount() {
        return casey.getNumberOfAssignments(period);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPES.Header;
        } else {
            return VIEW_TYPES.Normal;
        }
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

    private static class VIEW_TYPES {
        public static final int Header = 0;
        public static final int Normal = 1;
    }
}
