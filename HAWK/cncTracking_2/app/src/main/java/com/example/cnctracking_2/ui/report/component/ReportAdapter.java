package com.example.cnctracking_2.ui.report.component;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.WeeklyReportsItem;
import com.example.cnctracking_2.data.model.local.ChartModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.Holder> {

    private List<WeeklyReportsItem> list;

    public ReportAdapter(List<WeeklyReportsItem> model) {
        this.list = model;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_report, parent, false);
        return new Holder(v);
//        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_report, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.loadBarChart(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        BarChart chart;
        TextView tvTitle;

        public Holder(View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.barChart);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        protected void loadBarChart(WeeklyReportsItem chartModel, int position) {
            tvTitle.setText(chartModel.getTitle().getText());
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(true);

            chart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chart.setPinchZoom(false);

            chart.setDrawGridBackground(false);
            // chart.setDrawYLabels(false);

//            IAxisValueFormatter xAxisFormatter = new DefaultAxisValueFormatter(chart);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTypeface(tfLight);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setTextSize(8f);
//            final String xVal[]={"Val1","Val2","Val3"};
            xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return chartModel.getData().get(0).getDataPoints().get((int) value).getLabel();//super.getFormattedValue(value);
                }

//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return xVal[(int) value-1]; // xVal is a string array
//                }

            });
//            xAxis.setValueFormatter(xAxisFormatter);

//            IAxisValueFormatter custom = new MyAxisValueFormatter();
//
            YAxis leftAxis = chart.getAxisLeft();
//            leftAxis.setTypeface(tfLight);
            leftAxis.setLabelCount(chartModel.getData().size(), false);
//            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setTextSize(8f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setDrawGridLines(false);
//            rightAxis.setTypeface(tfLight);
            rightAxis.setLabelCount(chartModel.getData().size(), false);
//            rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            rightAxis.setEnabled(false);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(5f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            initData(chartModel, itemView.getContext(),position);
//            XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//            mv.setChartView(chart); // For bounds control
//            chart.setMarker(mv); // Set the marker to the chart
//
//            ArrayList<BarEntry> values = new ArrayList<BarEntry>();
//            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
//            for (int i = 0; i < chartModel.getGraphModels().size(); i++) {
////                yvalues.add(PieEntry(, services.get(i).name, (i + 1)))
//                values.add(
//                        new BarEntry(
//                                (i + 1),
//                                chartModel.getGraphModels().get(i).getyValue(),
//                                chartModel.getGraphModels().get(i).getxValue()
//                        )
//                );
//            }
//            PieDataSet dataSet = new PieDataSet(yvalues, chartModel.getyHeading());
//            dataSet.setDrawValues(true);
//
//            barChart.getDescription().setEnabled(true);
//            barChart.setPinchZoom(false);
////
//            barChart.setDrawBarShadow(false);
//            barChart.setDrawGridBackground(false);
//
//            barChart.setDrawValueAboveBar(true);
//            XAxis xAxis = barChart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setDrawGridLines(false);
//            xAxis.setGranularityEnabled(true);
//            xAxis.setGranularity(1f);
//            xAxis.setDrawLabels(true);
//            barChart.getAxisLeft().setDrawGridLines(false);
//
//            // add a nice and smooth animation
//            barChart.animateY(1500);
//
//            barChart.getLegend().setEnabled(true);
//            barChart.getLegend().setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//
//            BarDataSet set1 = new BarDataSet(values, chartModel.getyHeading());
////        set1.setColors(listOfColors)
//            dataSet.setColors(ColorTemplate.createColors(ColorTemplate.VORDIPLOM_COLORS));
//            set1.setDrawValues(false);
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//            BarData newBarData = new BarData(dataSets);
//
//            barChart.getDescription().setEnabled(true); // hide the description
//            barChart.getLegend().setEnabled(true); // hide the legend
//
//            barChart.getXAxis().setDrawLabels(true); // hide bottom label
//            barChart.getAxisLeft().setDrawLabels(true); // hide left label
//            barChart.getAxisRight().setDrawLabels(false); // hide right label
//
//            barChart.setData(newBarData);
//            barChart.setFitBars(true);
//
//            barChart.invalidate();
        }

        private void initData(WeeklyReportsItem chartModel, Context context, int position) {

            ArrayList<BarEntry> values = new ArrayList<>();

            for (int i = 0; i < chartModel.getData().get(0).getDataPoints().size(); i++) {

//                    values.add(new BarEntry(i, val, context.getResources().getDrawable(R.drawable.star)));
                values.add(new BarEntry(i, Float.parseFloat(chartModel.getData().get(0).getDataPoints().get(i).getY()),
                        chartModel.getData().get(0).getDataPoints().get(i).getY()));
            }

            BarDataSet set1;

//            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
//                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
//                set1.setValues(values);
//                chart.getData().notifyDataChanged();
//                chart.notifyDataSetChanged();
//
//            } else {
            set1 = new BarDataSet(values, chartModel.getTitle().getText());

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//                data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);
//            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(new int[]{getColorId(position, context)});

            chart.setData(data);
//            }
        }
    }

    private int getColorId(int position, Context context) {
        switch (position) {
            case 0:
                return Color.RED;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.GRAY;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.LTGRAY;
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.MAGENTA;
            case 8:
                return ContextCompat.getColor(context, R.color.light_green);
            default:
                return ContextCompat.getColor(context, R.color.teal_700);
        }
    }
}
