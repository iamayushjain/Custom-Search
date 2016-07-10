package com.ayush.hevenondemandapi1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.mingle.sweetpick.ViewPagerDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hod.api.hodclient.HODApps;
import hod.api.hodclient.HODClient;
import hod.api.hodclient.IHODClientCallback;
import hod.response.parser.CancelConnectorResponse;
import hod.response.parser.HODResponseParser;

/**
 * Created by user on 7/9/2016.
 */
public class MainPageSearchEngine extends Activity implements IHODClientCallback, AdapterView.OnItemSelectedListener {
    ImageButton searchView;
    AutoCompleteTextView searchEditText;
    String api = "bc7da6ae-8423-49e4-8f41-d7a058d0195b";
    private SweetSheet mSweetSheet;
    HODClient hodClient;
    HODResponseParser hodParser;
    ArrayList<String> autoCorrect;
    Spinner spinner;
    int controlVariable = 0;
    int backPressed = 0;
    //animatedCircleLoadingView //animatedCircleLoadingView;

    ImageButton uploadImage, docImage, advanceSearch, settingsButton;

    int finalValue5 = 0;
    int finalValue;
    int finalValue1;
    int finalValue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //handler();
        setContentView(R.layout.search_page);

        initView();
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s != null) {
                    useHODClient(s);
                    out("About to change");
                }
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                String text = searchEditText.getText().toString();
                if (!text.equals(""))
                    performSearch(text);
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                Intent intent = new Intent(MainPageSearchEngine.this, ImageToText.class);
                //animatedCircleLoadingView.setVisibility(View.VISIBLE);
                //animatedCircleLoadingView.stopFailure();
                //animatedCircleLoadingView.startIndeterminate();

                startActivityForResult(intent, 2);

                ///start
            }
        });
        docImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                Intent intent = new Intent(MainPageSearchEngine.this, DocTotext.class);
                startActivityForResult(intent, 2);
                ///start
            }
        });
        advanceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainPageSearchEngine.this,MainActivity.class);
//                startActivityForResult(intent, 2);

                spinner.setVisibility(View.VISIBLE);


            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
//                Intent intent=new Intent(MainPageSearchEngine.this,SettingPage.class);
//startActivity(intent);
                                                  vibrate();
                                                  //从menu 中设置数据源
                                                  mSweetSheet.setMenuList(R.menu.menu_settings);
                                                  mSweetSheet.setDelegate(new RecyclerViewDelegate(true));
                                                  mSweetSheet.setBackgroundEffect(new DimEffect(0.5f));
                                                  mSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                                                      @Override
                                                      public boolean onItemClick(int position, MenuEntity menuEntity1) {

                                                          if (position == 0)
                                                              selectTypes();
                                                          else if (position == 1)
                                                              selectNumberofResult();
                                                          else if (position == 2)
                                                              selectSort();
                                                          else if (position == 3)
                                                              selectSummaryType();
                                                          return true;
                                                      }
                                                  });
                                                  mSweetSheet.toggle();
                                              }
                                          }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            try {
                String[] message = data.getStringArrayExtra("MESSAGE");
                selectImageOutputType(message);
            } catch (Exception e) {

            }
            //animatedCircleLoadingView.stopOk();
            //animatedCircleLoadingView.setVisibility(View.INVISIBLE);
            //searchEditText.setText(message);
        }
    }

    void initView() {
        searchEditText = (AutoCompleteTextView) findViewById(R.id.editText);
        searchView = (ImageButton) findViewById(R.id.searchView);

        searchView.setBackgroundColor(Color.TRANSPARENT);
        searchView.setColorFilter(Color.parseColor("#FF4081"));
        autoCorrect = new ArrayList<>();
        uploadImage = (ImageButton) findViewById(R.id.Imageupload);
        uploadImage.setBackgroundColor(Color.TRANSPARENT);
        uploadImage.setColorFilter(Color.parseColor("#FF4081"));
        settingsButton = (ImageButton) findViewById(R.id.SettingSearch);
        settingsButton.setBackgroundColor(Color.TRANSPARENT);
        settingsButton.setColorFilter(Color.parseColor("#FF4081"));
        docImage = (ImageButton) findViewById(R.id.Docupload);
        docImage.setBackgroundColor(Color.TRANSPARENT);
        docImage.setColorFilter(Color.parseColor("#FF4081"));
        advanceSearch = (ImageButton) findViewById(R.id.button);
        advanceSearch.setBackgroundColor(Color.TRANSPARENT);
        advanceSearch.setColorFilter(Color.parseColor("#FF4081"));
        //animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        rl.setVisibility(View.VISIBLE);
        mSweetSheet = new SweetSheet(rl);

        spinner = (Spinner) findViewById(R.id.spinner1);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        System.out.print(searchEditText.getText().toString() + " " + autoCorrect);
        showCase1();
    }

    ShowcaseView b, b1;

    void showCase1() {
        SharedPreferences sp = getSharedPreferences("FIRSTTIME", 0);
        int number = 0;
        try {
            number = Integer.parseInt(sp.getString("FIRSTTIMELOGIN", ""));
        } catch (Exception e) {
            number = 0;
        }
        if (number == 0) {
            b = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(R.id.Imageupload, this))
                    .setContentTitle("Image Uploader")
                    .setContentText("This is the Image Upload Button")
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showCase2();
                        }
                    })
                    .hideOnTouchOutside()
                    .withMaterialShowcase()
                    .build();

        }
    }

    void showCase2() {
        b.hide();
        b1 = new ShowcaseView.Builder(this)
                .setTarget(new ViewTarget(R.id.searchView, this))
                .setContentTitle("Search Button")
                .setContentText("Click when you want to find the search results.")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCase3();
                    }
                })
                .hideOnTouchOutside().withMaterialShowcase()
                .build();

        SharedPreferences sp1 = getSharedPreferences("FIRSTTIME", 0);
        SharedPreferences.Editor ed1 = sp1.edit();
        ed1.putString("FIRSTTIMELOGIN", "1");
        ed1.commit();

    }

    void showCase3() {
        b1.hide();
    }

    void performSearch(String searchedString) {
        Intent i = new Intent(MainPageSearchEngine.this, SearchResult.class);

        i.putExtra("Search", searchedString);

        startActivity(i);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        finish();


    }

    private void useHODClient(CharSequence incompleteSearchString) {

        if (controlVariable == 0) {

            hodClient = new HODClient(api, this);
            hodParser = new HODResponseParser();

            String hodApp = HODApps.AUTO_COMPLETE;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("text", incompleteSearchString.toString());
//        List<String> entities = new ArrayList<String>();
//        entities.add("people_eng");
//        entities.add("places_eng");
//        params.put("entity_type", entities);
//        params.put("unique_entities", "true");
            //CancelConnectorResponse can= /new CancelConnectorResponse();
            controlVariable = 1;
            hodClient.GetRequest(params, hodApp, HODClient.REQ_MODE.SYNC);

        } else {
            controlVariable = 2;
        }
    }

    // define a custom response class
    public class EntityExtractionResponse {
        public List<Entity> entities;

        public class AdditionalInformation {
            public List<String> person_profession;
            public String person_date_of_birth;
            public String wikipedia_eng;
            public Long place_population;
            public String place_country_code;
            public Double place_elevation;
        }

        public class Entity {
            public String normalized_text;
            public String type;
            public AdditionalInformation additional_information;
        }
    }

    // implement callback functions
    @Override
    public void requestCompletedWithContent(String response) {
        EntityExtractionResponse resp = (EntityExtractionResponse) hodParser.ParseCustomResponse
                (EntityExtractionResponse.class, response);
        //System.out.println(response);

        if (resp != null) {
            System.out.println("No Error" + getLocalClassName());
            autoCorrect.clear();
            JsonAnalysis(response);
//            String values = "";
//            for (EntityExtractionResponse.Entity ent : resp.entities) {
//                values += ent.type + "\n";
//                values += ent.normalized_text + "\n";
//                if (ent.type.equals("places_eng")) {
//                    values += ent.additional_information.place_country_code + "\n";
//                    values += ent.additional_information.place_elevation + "\n";
//                    values += ent.additional_information.place_population + "\n";
//                } else if (ent.type.equals("people_eng")) {
//                    values += ent.additional_information.person_date_of_birth + "\n";
//                    values += ent.additional_information.person_profession + "\n";
//                    values += ent.additional_information.wikipedia_eng + "\n";
//                }
//            }
//            System.out.print(values);
//            // print the values
        } else {
//            List<HODErrorObject> errors = parser.GetLastError();
//            String errorMsg = "";
//            for (HODErrorObject err : errors) {
//                errorMsg += String.format("Error code: %d\nError Reason: %s\n", err.error, err.reason);
//                if (err.detail != null)
//                    errorMsg += "Error detail: " + err.detail + "\n";
//                // handle error message
            controlVariable = 0;
            System.out.println("Error");
        }
    }

    @Override
    public void requestCompletedWithJobID(String response) {
        System.out.print(response);

    }

    @Override
    public void onErrorOccurred(String errorMessage) {
        // handle error if any
        out(errorMessage);
        controlVariable = 0;
    }

    void JsonAnalysis(String responce) {
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(responce);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("words");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = jsonArray.getString(i);

                String name = jsonArray.getString(i);
                autoCorrect.add(name);

            }
            System.out.print(autoCorrect);
            String[] arr = autoCorrect.toArray(new String[autoCorrect.size()]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, arr);

            searchEditText.setThreshold(1);
            searchEditText.setAdapter(adapter);
            if (controlVariable == 2) {
                controlVariable = 0;
                String text = searchEditText.getText().toString();
                if (text != null)
                    useHODClient(text);
            } else {
                controlVariable = 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        // controlVariable=0;
    }

    void out(String n) {
        System.out.println(n);
    }

    private void selectImageOutputType(final String[] items) {

// arraylist to keep the selected items

        String[] newItems = new String[items.length];
        newItems[0] = "Complete String";
        for (int i = 1; i < items.length; i++)
            newItems[i] = items[i];
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Text to Search")

                .setSingleChoiceItems(newItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //          Toast.makeText(getApplicationContext(),which+"",Toast.LENGTH_SHORT).show();
                                finalValue5 = which;
                            }
                        }
                ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        try {
                            searchEditText.setText(items[finalValue5]);
                        } catch (Exception e) {
                            searchEditText.setText("");
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

    @Override
    public void onBackPressed() {
        vibrate();
        if (mSweetSheet.isShow()) {
            if (mSweetSheet.isShow()) {
                mSweetSheet.dismiss();
            }

        } else {
            if (backPressed == 1)
                super.onBackPressed();
            else
                backPressed++;
        }


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    void handler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Intent i = new Intent(MainPageSearchEngine.this, MainPageSearchEngine.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                finish();

            }
        });


    }

    void vibrate() {
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(
                Context.VIBRATOR_SERVICE);
        vibrator.vibrate(60);

    }


}
