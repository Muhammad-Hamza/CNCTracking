package com.example.cnctracking_2.ui.report.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.Base;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.Orientation;
import com.anychart.enums.Position;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.scales.Linear;
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
import com.anychart.core.cartesian.series.Line;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        AnyChartView anyBarChart;
        TextView tvTitle;
        TextView tvSeeChart;

        public Holder(View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.barChart);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSeeChart = itemView.findViewById(R.id.tvSeeChart);
            anyBarChart = itemView.findViewById(R.id.anyBarChart);
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
            chart.setVisibility(View.GONE);
            anyBarChart.setVisibility(View.VISIBLE);

            tvTitle.setText(chartModel.getTitle());
            tvSeeChart.setText(chartModel.getDetails());

            Cartesian cartesian = AnyChart.cartesian();

            cartesian.animation(true);

            cartesian.yScale().stackMode(ScaleStackMode.VALUE);

            cartesian.yAxis(0).labels().padding(0d, 0d, 0d, 5d).format("{%Value}{type:number, decimalsCount:2}");
            cartesian.yAxis(true);

            List<DataEntry> data = new ArrayList<>();

            int[] colorStr = new int[chartModel.getDataItems().size()];

            ArrayList<String> dataStackLabel = new ArrayList<>();//[chartModel.getDataItems().size()];

            for (int i = 0; i < chartModel.getDataItems().size(); i++) {
                dataStackLabel.add(chartModel.getDataItems().get(i).getName());
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

            Set set = com.anychart.data.Set.instantiate();
            List<String> listOfKeys = new ArrayList<String>(mapDriverBehaviour.keySet());
            sortArray(listOfKeys);
            for (int i = 0; i < listOfKeys.size(); i++) {
                float[] dataFloat = new float[mapDriverBehaviour.get(listOfKeys.get(i)).size()];
                for (int j = 0; j < mapDriverBehaviour.get(listOfKeys.get(i)).size(); j++) {
                    dataFloat[j] = mapDriverBehaviour.get(listOfKeys.get(i)).get(j).getY();
                    ValueDataEntry custom = new ValueDataEntry(
                            listOfKeys.get(i),
                            (int) dataFloat[j]);
                    data.add(custom);
                }

                String ValuesData = "value";

                Mapping columnData = set.mapAs("{ x: 'x', value: '" + ValuesData + "' }");

                Column column = cartesian.column(columnData);

                column.name(dataStackLabel.get(i))
                        .color(chartModel.getDataItems().get(i).getColor());

                column.tooltip()
                        .displayMode(TooltipDisplayMode.UNION)
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        .offsetX(0d)
                        .offsetY(5d)
                        .position("right");
            }

            cartesian.xAxis(true).labels().format("{%x}");

            cartesian.crosshair(true);
            set.data(data);

            cartesian.legend(true);
            cartesian.legend().enabled(true);
            cartesian.legend().inverted(true);
            cartesian.legend().fontSize(10d);
            cartesian.legend().padding(0d, 0d, 20d, 0d);
            anyBarChart.setChart(cartesian);
        }

        private int[] getColors() {

            // have as many colors as stack-values per entry
            int[] colors = new int[3];

            System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

            return colors;
        }

        protected void loadBarChart(NewGraphModel chartModel, int position) {
            anyBarChart.setVisibility(View.GONE);
            chart.setVisibility(View.VISIBLE);
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
