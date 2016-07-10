package com.ayush.hevenondemandapi1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 7/9/2016.
 */
public class SettingPage extends Activity {
    ListView listView;
    int finalValue;
    int finalValue1;
    int finalValue2;
    String[] parameters = {
            "Type",
            "Number of Results",
            "Sort",
            "Summary",
            //"Langauge"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        initView();


    }

    void initView() {
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, parameters);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    selectTypes();
                else if (position == 1)
                    selectNumberofResult();
                else if (position == 2)
                    selectSort();
                else if (position == 3)
                    selectSummaryType();
            }
        });
    }

    private void selectSummaryType() {
        final CharSequence[] items = {
                "Off",
                "Quick",
                "Concept",
                "Context"
        };
// arraylist to keep the selected items
        final ArrayList<Integer> seletedItems = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("SUMMARY", 0);
        int number = 0;
        try {
            number = Integer.parseInt(sp.getString("summaryno", ""));
        } catch (Exception e) {
            number = 0;
        }

        finalValue2 = number;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Summary Type")

                .setSingleChoiceItems(items, number, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //          Toast.makeText(getApplicationContext(),which+"",Toast.LENGTH_SHORT).show();
                                finalValue2 = which;
                            }
                        }
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        SharedPreferences sp1 = getSharedPreferences("SUMMARY", 0);
                        SharedPreferences.Editor ed1 = sp1.edit();
                        ed1.putString("summaryno", finalValue2 + "");
                        ed1.commit();


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    private void selectSort() {
        final CharSequence[] items = {
                "Relevance",
                "Reverse Relevance",
                "Date",
                "Reverse Date"
        };
// arraylist to keep the selected items
        final ArrayList<Integer> seletedItems = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("SORT", 0);
        int number = 0;
        try {
            number = Integer.parseInt(sp.getString("sortno", ""));
        } catch (Exception e) {
            number = 0;
        }

        finalValue1 = number;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Sort Type")

                .setSingleChoiceItems(items, number, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //          Toast.makeText(getApplicationContext(),which+"",Toast.LENGTH_SHORT).show();
                                finalValue1 = which;
                            }
                        }
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        SharedPreferences sp1 = getSharedPreferences("SORT", 0);
                        SharedPreferences.Editor ed1 = sp1.edit();
                        ed1.putString("sortno", finalValue1 + "");
                        ed1.commit();


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    private void selectNumberofResult() {
        final CharSequence[] items = {
                "10",
                "20",
                "30",
                "40",
                "50"
        };
// arraylist to keep the selected items
        final ArrayList<Integer> seletedItems = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("NUMBERPAGE", 0);
        int number = 0;
        try {
            number = Integer.parseInt(sp.getString("nopage", ""));
        } catch (Exception e) {
            number = 0;
        }

        finalValue = number;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Items to Display")

                .setSingleChoiceItems(items, number, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //          Toast.makeText(getApplicationContext(),which+"",Toast.LENGTH_SHORT).show();
                                finalValue = which;
                            }
                        }
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        SharedPreferences sp1 = getSharedPreferences("NUMBERPAGE", 0);
                        SharedPreferences.Editor ed1 = sp1.edit();
                        ed1.putString("nopage", finalValue + "");
                        ed1.commit();


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    private void selectTypes() {
        final CharSequence[] items = {
                "Wikipedia",
                "News",
                "Fact",
                "Patents and Research",
                "Transport"
        };
// arraylist to keep the selected items
        final ArrayList<Integer> seletedItems = new ArrayList<>();
        final boolean[] checkbox = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            SharedPreferences sp = getSharedPreferences("SETTINGS" + i, 0);

            if (sp.getString("settingitem" + i, "").equals("1")) {
                checkbox[i] = true;
            } else {
                checkbox[i] = false;
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Items to Display")
                .setMultiChoiceItems(items, checkbox, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedItems.add(indexSelected);
                            checkbox[indexSelected] = true;
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedItems.remove(Integer.valueOf(indexSelected));
                            checkbox[indexSelected] = false;
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        for (int i = 0; i < items.length; i++) {
                            if (checkbox[i]) {
                                SharedPreferences sp1 = getSharedPreferences("SETTINGS" + i, 0);
                                SharedPreferences.Editor ed1 = sp1.edit();
                                ed1.putString("settingitem" + i, "1");
                                ed1.commit();
                            } else {
                                SharedPreferences sp1 = getSharedPreferences("SETTINGS" + i, 0);
                                SharedPreferences.Editor ed1 = sp1.edit();
                                ed1.putString("settingitem" + i, "0");
                                ed1.commit();
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

}
