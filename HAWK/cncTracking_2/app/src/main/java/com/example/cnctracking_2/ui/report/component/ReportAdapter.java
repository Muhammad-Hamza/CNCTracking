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
import com.example.cnctracking_2.data.model.DataItem;
import com.example.cnctracking_2.data.model.DataPointsItem;
import com.example.cnctracking_2.data.model.WeeklyReportsItem;
import com.example.cnctracking_2.data.model.local.ChartModel;
import com.example.cnctracking_2.data.model.local.NewGraphModel;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.Holder> {

    private List<NewGraphModel> list;

    public ReportAdapter(List<NewGraphModel> model) {
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
        if (list.get(position).isMultipleAdded()) {
            holder.loadStackBarChart(list.get(position), position);
        } else {
            holder.loadBarChart(list.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        BarChart chart;
        TextView tvTitle;
        TextView tvSeeChart;

        public Holder(View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.barChart);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSeeChart = itemView.findViewById(R.id.tvSeeChart);
        }

        private int getTotalValue(WeeklyReportsItem item) {
            int sum = 0;
            for (DataPointsItem dataItem : item.getDataPoints()) {
                sum += dataItem.getY();
            }

            return sum;
        }

        private String getGraphText(String text, Context context, WeeklyReportsItem item) {
            switch (text) {
                case "Mileage Covered":
                    return context.getString(R.string.txtMileageCovered, getTotalValue(item));
                case "Trips Count":
                    return context.getString(R.string.txtTotalTrips, getTotalValue(item));
                case "Fuel Used":
                    return context.getString(R.string.txtTotalFuel, getTotalValue(item));
                case "Speed Graph":
                    return context.getString(R.string.txtTotalSpeed, getTotalValue(item));
                case "Max Speed":
                    return context.getString(R.string.txtMaxSpeed, getTotalValue(item));
                case "Idling Count":
                    return context.getString(R.string.txtIdlingCount, getTotalValue(item));
                case "Fence Count":
                    return context.getString(R.string.txtFenceCount, getTotalValue(item));
                default:
                    return "Text Not Found";
            }
        }

        protected void loadStackBarChart(NewGraphModel chartModel, int position) {
            tvTitle.setText(chartModel.getTitle());
            tvSeeChart.setText(chartModel.getDetails());
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(true);

            chart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            // TODO: 2/10/2022  chart.setMaxVisibleValueCount(chartModel.getMultipleMapping().size());

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
            YAxis leftAxis = chart.getAxisLeft();
//            leftAxis.setTypeface(tfLight);

            // TODO: 2/10/2022  leftAxis.setLabelCount(chartModel.getMultipleMapping().size(), false);

            //            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setTextSize(8f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setDrawGridLines(false);
//            rightAxis.setTypeface(tfLight);

            // TODO: 2/10/2022  rightAxis.setLabelCount(chartModel.getMultipleMapping().size(), false);

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

            initStackData(chartModel, itemView.getContext(), position);
        }

        private void initStackData(NewGraphModel chartModel, Context context, int position) {

            ArrayList<BarEntry> values = new ArrayList<>();
//            Map<String, List<Float>> map = new HashMap<>();
            int[] colorStr = new int[chartModel.getDataItems().size()];
            String[] dataStackLabel = new String[chartModel.getDataItems().size()];
            for (int i = 0; i < chartModel.getDataItems().size(); i++) {
                dataStackLabel[i] = chartModel.getDataItems().get(i).getName();
                colorStr[i] = Color.parseColor(chartModel.getDataItems().get(i).getColor());
            }
            Map<String, List<DataPointsItem>> mapDriverBehaviour = new HashMap<>();
            for (int i = 0; i < chartModel.getDataItems().size(); i++) {
                for (int j = 0; j < chartModel.getDataItems().size(); j++) {
                    if (mapDriverBehaviour.containsKey(chartModel.getDataItems().get(i).getDataPoints().get(j).getLabel())) {
                        List<DataPointsItem> nestedGraphListNew = mapDriverBehaviour.get(chartModel.getDataItems().get(i).getDataPoints().get(j).getLabel());
                        nestedGraphListNew.add(chartModel.getDataItems().get(i).getDataPoints().get(j));
                        mapDriverBehaviour.put(chartModel.getDataItems().get(i).getDataPoints().get(j).getLabel(), nestedGraphListNew);
                    } else {
                        List<DataPointsItem> nestedGraphList = new ArrayList<>();
                        nestedGraphList.add(chartModel.getDataItems().get(i).getDataPoints().get(j));
                        mapDriverBehaviour.put(chartModel.getDataItems().get(i).getDataPoints().get(j).getLabel(), nestedGraphList);
                    }
                }
            }

            List<String> listOfKeys = new ArrayList<String>(mapDriverBehaviour.keySet());
            for (int i = 0; i < listOfKeys.size(); i++) {
                float[] dataFloat = new float[mapDriverBehaviour.get(listOfKeys.get(i)).size()];
                for (int j = 0; j < mapDriverBehaviour.get(listOfKeys.get(i)).size(); j++) {
                    dataFloat[j] = mapDriverBehaviour.get(listOfKeys.get(i)).get(j).getY();
                }
                values.add(new BarEntry(
                        i,
                        dataFloat, mapDriverBehaviour.get(listOfKeys.get(i)).get(0).getLabel()));
            }

            BarDataSet set1;

            set1 = new BarDataSet(values, chartModel.getTitle());
            set1.setDrawIcons(false);
            set1.setColors(colorStr);
//            for (int i = 0; i < chartModel.getListOfDataPoint().get(i); i++) {
//                dataStackLabel[i] = chartModel.getMultipleMapping().get(listOfKeys.get(i)).
//            }
            set1.setStackLabels(dataStackLabel);
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (listOfKeys.size() > (int) value)
                        return listOfKeys.get((int) value);
                    return "";
                }
            };


            xAxis.setValueFormatter(formatter);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
//                data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);

            chart.setFitBars(true);
        }

        private int[] getColors() {

            // have as many colors as stack-values per entry
            int[] colors = new int[3];

            System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

            return colors;
        }

        protected void loadBarChart(NewGraphModel chartModel, int position) {
            tvTitle.setText(chartModel.getFirstHeading());
            tvSeeChart.setText(chartModel.getSecondHeading());
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
/*            xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return chartModel.getDataPoints().get((int) value).getLabel();//super.getFormattedValue(value);
                }

//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return xVal[(int) value-1]; // xVal is a string array
//                }

            });*/

//            xAxis.setValueFormatter(xAxisFormatter);

//            IAxisValueFormatter custom = new MyAxisValueFormatter();
//
            YAxis leftAxis = chart.getAxisLeft();
//            leftAxis.setTypeface(tfLight);
            leftAxis.setLabelCount(chartModel.getListOfDataPoint().size(), false);
//            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setTextSize(8f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setDrawGridLines(false);
//            rightAxis.setTypeface(tfLight);
            rightAxis.setLabelCount(chartModel.getListOfDataPoint().size(), false);
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

            initData(chartModel, itemView.getContext(), position);
        }

        private void initData(NewGraphModel chartModel, Context context, int position) {

            ArrayList<BarEntry> values = new ArrayList<>();

            for (int i = 0; i < chartModel.getListOfDataPoint().size(); i++) {

//                    values.add(new BarEntry(i, val, context.getResources().getDrawable(R.drawable.star)));
                values.add(new BarEntry(i, chartModel.getListOfDataPoint().get(i).getY(), chartModel.getListOfDataPoint().get(i).getStrY()));
            }

            BarDataSet set1;

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return chartModel.getListOfDataPoint().get((int) value).getLabel();
                }
            };


            xAxis.setValueFormatter(formatter);
            set1 = new BarDataSet(values, chartModel.getTitle());

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//                data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);
//            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColor(Color.parseColor(chartModel.getColor()));

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

    private void sortArray(List<String> arraylist) {
        Collections.sort(arraylist, new sortCompare());
    }

    class sortCompare implements Comparator<String> {
        // Method of this class
        @Override
        public int compare(String a, String b) {
            try {
                Date aDate = new SimpleDateFormat("dd-MM-yy").parse(a);
                Date bDate = new SimpleDateFormat("dd-MM-yy").parse(b);
                /* Returns sorted data in ascending order */
                return aDate.compareTo(bDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
}
