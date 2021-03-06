package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.CourseActivity;
import ga.caseyavila.velcro.fragments.AssignmentEditDialogFragment;
import ga.caseyavila.velcro.fragments.AssignmentRemoveDialogFragment;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;
import java.util.ArrayList;
import java.util.List;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {

    private final Context context;
    private final int period;

    public CourseViewAdapter(Context context, int period) {
        this.context = context;
        this.period = period;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == COURSE_VIEW_TYPES.HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assignment, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == COURSE_VIEW_TYPES.HEADER) {  // If the card is a header
            holder.headerTeacher.setText(casey.getPeriod(period).getTeacher());

            if (casey.getPeriod(period).hasTrends()) {  // If class has trends (grades posted more than once)
                LineChartView lineChartView = holder.trendChart;

                new Thread (() -> {
                    List<PointValue> values = new ArrayList<>();
                    for (int i = 0; i < casey.getPeriod(period).getTrend().xTrendValues().size(); i++) {
                        values.add(new PointValue(casey.getPeriod(period).getTrend().xTrendValues().get(i),
                                casey.getPeriod(period).getTrend().yTrendValues().get(i)));
                    }

                    Line line = new Line(values).setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    line.setFilled(true);
                    line.setHasPoints(false);

                    List<Line> lines = new ArrayList<>();
                    lines.add(line);

                    LineChartData data = new LineChartData();
                    data.setLines(lines);

                    Axis axis = new Axis(casey.getPeriod(period).getTrend().dateLabels());
                    axis.setTextSize(14);
                    axis.setTypeface(ResourcesCompat.getFont(context, R.font.manrope_medium));
                    axis.setHasLines(true);
                    axis.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
                    axis.setLineColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
                    data.setAxisXBottom(axis);

                    Axis yAxis = new Axis();
                    yAxis.setFormatter(new SimpleAxisValueFormatter(1));  // One place after decimals
                    yAxis.setTextSize(14);
                    yAxis.setTypeface(ResourcesCompat.getFont(context, R.font.manrope_medium));
                    yAxis.setHasLines(true);
                    yAxis.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
                    yAxis.setLineColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
                    yAxis.setMaxLabelChars(4);
                    data.setAxisYLeft(yAxis);
                    lineChartView.setLineChartData(data);
                    lineChartView.setInteractive(true);

                    Viewport viewport = lineChartView.getCurrentViewport();
                    // Have 1/4 of the graph space as top padding
                    viewport.top = casey.getPeriod(period).getTrend().getTrendMax() + (float) 0.25 * casey.getPeriod(period).getTrend().getTrendRange();
                    // Have 1/4 of the graph space as bottom padding
                    viewport.bottom = casey.getPeriod(period).getTrend().getTrendMin() - (float) 0.25 * casey.getPeriod(period).getTrend().getTrendRange();
                    lineChartView.setMaximumViewport(viewport);
                }).start();
            } else {
                holder.trendChart.setVisibility(View.GONE);  // Don't show graph if trends do not exist
            }

            holder.headerGrade.setText(casey.getPeriod(period).getCalculatedGrade());
            holder.headerPercentage.setText(casey.getPeriod(period).getCalculatedPercentage());
            holder.gradeUpdateDate.setText(context.getString(R.string.grade_last_updated, casey.getPeriod(period).getGradeUpdateDate()));

        } else {
            // Subtract one due to the offset the header card creates
            holder.assignmentName.setText(casey.getPeriod(period).getAssignment(position - 1).getName());
            holder.assignmentCategory.setText(casey.getPeriod(period).getAssignment(position - 1).getCategory());
            holder.assignmentScoreEarned.setText(casey.getPeriod(period).getAssignment(position - 1).getScoreEarned());
            holder.assignmentScorePossible.setText(casey.getPeriod(period).getAssignment(position - 1).getScorePossible());
            holder.assignmentPercentage.setText(casey.getPeriod(period).getAssignment(position - 1).getPercentage());
            holder.assignmentCardView.setOnClickListener(view -> {
                FragmentManager fragmentManager = ((CourseActivity) context).getSupportFragmentManager();
                DialogFragment dialogFragment = new AssignmentEditDialogFragment(this);

                Bundle bundle = new Bundle();
                bundle.putInt("period", period);
                bundle.putInt("assignment", position - 1);
                dialogFragment.setArguments(bundle);

                dialogFragment.show(fragmentManager, "assignment_edit_dialog");
            });

            holder.assignmentCardView.setOnLongClickListener(view -> {
                FragmentManager fragmentManager = ((CourseActivity) context).getSupportFragmentManager();
                DialogFragment dialogFragment = new AssignmentRemoveDialogFragment(this);

                Bundle bundle = new Bundle();
                bundle.putInt("period", period);
                bundle.putInt("assignment", position - 1);
                dialogFragment.setArguments(bundle);

                dialogFragment.show(fragmentManager, "assignment_remove_dialog");

                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        // Add one for header card
        return casey.getPeriod(period).getNumberOfAssignments() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Set the first card to a header
        if (position == 0) {
            return COURSE_VIEW_TYPES.HEADER;
        } else {
            return COURSE_VIEW_TYPES.NORMAL;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Views for assignment card
        MaterialCardView assignmentCardView;
        MaterialTextView assignmentName;
        MaterialTextView assignmentCategory;
        MaterialTextView assignmentScoreEarned;
        MaterialTextView assignmentScorePossible;
        MaterialTextView assignmentPercentage;

        // Views for header card
        MaterialCardView headerCardView;
        MaterialTextView headerTeacher;
        MaterialTextView headerGrade;
        MaterialTextView headerPercentage;
        MaterialTextView gradeUpdateDate;
        LineChartView trendChart;

        public ViewHolder(View itemView) {
            super(itemView);

            assignmentCardView = itemView.findViewById(R.id.assignment_card);
            assignmentName = itemView.findViewById(R.id.assignment_name);
            assignmentCategory = itemView.findViewById(R.id.assignment_category);
            assignmentScoreEarned = itemView.findViewById(R.id.assignment_score_earned);
            assignmentScorePossible = itemView.findViewById(R.id.assignment_score_possible);
            assignmentPercentage = itemView.findViewById(R.id.assignment_percentage);

            headerCardView = itemView.findViewById(R.id.course_header_card);
            headerTeacher = itemView.findViewById(R.id.header_teacher);
            headerGrade = itemView.findViewById(R.id.header_grade);
            headerPercentage = itemView.findViewById(R.id.header_percentage);
            gradeUpdateDate = itemView.findViewById(R.id.grade_update_date);
            trendChart = itemView.findViewById(R.id.trend_chart);
        }
    }

    private static class COURSE_VIEW_TYPES {
        public static final int HEADER = 0;
        public static final int NORMAL = 1;
    }
}
