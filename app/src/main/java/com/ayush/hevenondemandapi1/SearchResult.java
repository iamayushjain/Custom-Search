package com.ayush.hevenondemandapi1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

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
public class SearchResult extends Activity implements IHODClientCallback {
    ImageButton searchView;
    AutoCompleteTextView searchEditText;
    String api = "bc7da6ae-8423-49e4-8f41-d7a058d0195b";
    ListView listView;
    HODClient hodClient;
    int backPressed = 0;

    HODResponseParser hodParser;
    ArrayList<String> autoCorrectNew;

    ArrayList<String> autoCorrect;
    int controlVariable = 0, controlVariableNew = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    ArrayList results = new ArrayList<DataObject>();
    ArrayList<String> resultsUrl = new ArrayList<>();
    AnimatedCircleLoadingView animatedCircleLoadingView;
    RelativeLayout relativeLayout, relativeLayout1;
    String stringToSearch;
    TextView searchresulttext;
    CardView cardView;
    FloatingActionButton fab;
    int resultValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        Bundle bund = getIntent().getExtras();
        String s = bund.getString("Search");
        stringToSearch = s;
        initView();

        useHODClient(s);

    }

    void initView() {

        listView = (ListView) findViewById(R.id.listView);
        autoCorrect = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        searchresulttext = (TextView) findViewById(R.id.textViewSearch);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        animatedCircleLoadingView.startIndeterminate();


        searchEditText = (AutoCompleteTextView) findViewById(R.id.editText);
        searchView = (ImageButton) findViewById(R.id.searchView);
        searchView.setBackgroundColor(Color.TRANSPARENT);
        searchView.setColorFilter(Color.parseColor("#FF4081"));
        cardView = (CardView) findViewById(R.id.card_view);
        autoCorrectNew = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                Intent i = new Intent(SearchResult.this, MainPageSearchEngine.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                finish();

            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s != null) {
                    useHODClientTextOnChange(s);
                    out("About to change");
                }
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                String text = searchEditText.getText().toString();
                if (!text.equals("")) {


                    animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
                    animatedCircleLoadingView.setBackgroundColor(Color.TRANSPARENT);
                    animatedCircleLoadingView.stopFailure();
                    animatedCircleLoadingView.setVisibility(View.VISIBLE);
                    animatedCircleLoadingView.startIndeterminate();

                    performSearch(text);
                }
            }
        });

    }


    private void useHODClient(String search) {


        hodClient = new HODClient(api, this);
        hodParser = new HODResponseParser();

        String hodApp = HODApps.QUERY_TEXT_INDEX;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("text", search);
        List<String> entities = new ArrayList<String>();
//        entities.add("news_eng");
//        entities.add("places_eng");
        String contentType = "news_eng";
        String[] contentTypeCases = {
                "wiki_eng",
                "news_eng",
                "world_factbook",
                "arxiv",
                "transport"
        };

        String sortType = "date";
        String[] sortTypeCases = {"relevance",
                "reverse_relevance",
                "date",
                "reverse_date"};
        String resultCount = "10";
        String[] resultCountCases = {
                "10",
                "20",
                "30",
                "40",
                "50"
        };
        String summary = "quick";
        String[] summaryCases = {
                "off",
                "quick",
                "concept",
                "context"};
//sort
        try {
            SharedPreferences sp = getSharedPreferences("SORT", 0);
            int number = 2;
            try {
                number = Integer.parseInt(sp.getString("sortno", ""));
            } catch (Exception e) {
                number = 2;
            }
            sortType = sortTypeCases[number];

        } catch (Exception e) {

        }
//numberpage
        try {
            SharedPreferences sp = getSharedPreferences("NUMBERPAGE", 0);
            int number = 0;
            try {
                number = Integer.parseInt(sp.getString("nopage", ""));
            } catch (Exception e) {
                number = 0;
            }
            resultCount = resultCountCases[number];

        } catch (Exception e) {

        }
//summary
        try {
            SharedPreferences sp = getSharedPreferences("SUMMARY", 0);
            int number = 1;
            try {
                number = Integer.parseInt(sp.getString("summaryno", ""));
            } catch (Exception e) {
                number = 1;
            }
            summary = summaryCases[number];

        } catch (Exception e) {

        }
//items0
        try {
            SharedPreferences sp = getSharedPreferences("SETTINGS0", 0);
            int number = 0;
            try {
                number = Integer.parseInt(sp.getString("settingitem0", ""));
            } catch (Exception e) {
                number = 0;
            }
            if (number == 1)
                entities.add(contentTypeCases[0]);

        } catch (Exception e) {

        }
//item1
        try {
            SharedPreferences sp = getSharedPreferences("SETTINGS1", 0);
            int number = 0;
            try {
                number = Integer.parseInt(sp.getString("settingitem1", ""));
            } catch (Exception e) {
                number = 0;
            }
            if (number == 1)
                entities.add(contentTypeCases[1]);

        } catch (Exception e) {

        }
        //item2
        try {
            SharedPreferences sp = getSharedPreferences("SETTINGS2", 0);
            int number = 0;
            try {
                number = Integer.parseInt(sp.getString("settingitem2", ""));
            } catch (Exception e) {
                number = 0;
            }
            if (number == 1)
                entities.add(contentTypeCases[2]);

        } catch (Exception e) {

        }
        //item3
        try {
            SharedPreferences sp = getSharedPreferences("SETTINGS3", 0);
            int number = 0;
            try {
                number = Integer.parseInt(sp.getString("settingitem3", ""));
            } catch (Exception e) {
                number = 0;
            }
            if (number == 1)
                entities.add(contentTypeCases[3]);

        } catch (Exception e) {

        }
        //item 4
        try {
            SharedPreferences sp = getSharedPreferences("SETTINGS4", 0);
            int number = 0;
            try {
                number = Integer.parseInt(sp.getString("settingitem4", ""));
            } catch (Exception e) {
                number = 0;
            }
            if (number == 1)
                entities.add(contentTypeCases[4]);

        } catch (Exception e) {

        }
        out(sortType);
        out(search);
        out(resultCount);
        out(summary);
        System.out.println(entities);
        if (!entities.isEmpty()) {

            params.put("indexes", entities);
        } else {
            params.put("indexes", "news_eng");
        }
        params.put("absolute_max_results", resultCount);
        params.put("summary", summary);
//        params.put("unique_entities", "true");
        out("I m here");
        hodClient.GetRequest(params, hodApp, HODClient.REQ_MODE.SYNC);
        controlVariable = 1;

    }

    private void useHODClientTextOnChange(CharSequence incompleteSearchString) {

        if (controlVariableNew == 0) {

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
            controlVariableNew = 1;
            hodClient.GetRequest(params, hodApp, HODClient.REQ_MODE.SYNC);

        } else {
            controlVariableNew = 2;
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
        System.out.println(response);


        if (resp != null) {
            System.out.println("No Error" + getLocalClassName());
            if (resultValue == 0) {
                autoCorrect.clear();
                JsonAnalysis(response);
            } else {
                autoCorrectNew.clear();
                JsonAnalysisTextChange(response);
            }
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
            controlVariableNew = 0;
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
        controlVariableNew = 0;
        out(errorMessage);

    }

    void JsonAnalysisTextChange(String responce) {
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(responce);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("words");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = jsonArray.getString(i);

                String name = jsonArray.getString(i);
                autoCorrectNew.add(name);

            }
            //System.out.print(autoCorrect);
            String[] arr = autoCorrectNew.toArray(new String[autoCorrectNew.size()]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, arr);

            searchEditText.setThreshold(1);
            searchEditText.setAdapter(adapter);
            if (controlVariableNew == 2) {
                controlVariableNew = 0;
                String text = searchEditText.getText().toString();
                if (text != null)
                    useHODClientTextOnChange(text);
            } else {
                controlVariableNew = 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        // controlVariable=0;
    }

    void JsonAnalysis(String responce) {
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(responce);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("documents");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                String name = jsonObject.optString("title").toString();
                String summary = jsonObject.optString("summary").toString();
                String urlName = jsonObject.optString("reference").toString();
                DataObject obj = new DataObject(name,
                        summary);
                results.add(i, obj);
                autoCorrect.add(name);
                resultsUrl.add(urlName);
            }
            System.out.print(autoCorrect);
            mAdapter = new MyRecyclerViewAdapter(results);
            mRecyclerView.setAdapter(mAdapter);
            final String[] url = resultsUrl.toArray(new String[resultsUrl.size()]);
            animatedCircleLoadingView.stopOk();
            relativeLayout.setBackgroundColor(Color.parseColor("#FAFAFA"));
            animatedCircleLoadingView.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
            searchEditText.setText(stringToSearch);
            fab.setVisibility(View.VISIBLE);
            searchresulttext.setText(Html.fromHtml("Showing Results for <font color=#03A9F4 > <u>" + stringToSearch + "</u></font>"));

            resultValue = 1;
            ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                    .MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Log.i(LOG_TAG, " Clicked on Item " + position);
                    vibrate();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url[position]));
                    startActivity(browserIntent);
                }
            });


//            String[] arr = autoCorrect.toArray(new String[autoCorrect.size()]);
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                    (this, android.R.layout.select_dialog_item, arr);
            //   listView.setAdapter(adapter);
//
//            searchEditText.setThreshold(1);
//            searchEditText.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void out(String n) {
        System.out.println(n);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
//                .MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                Log.i(LOG_TAG, " Clicked on Item " + position);
//            }
//        });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            String message = data.getStringExtra("MESSAGE");
            autoCorrect.clear();
            resultValue = 0;
            results.clear();
            resultsUrl.clear();
            JsonAnalysis(message);
            //searchEditText.setText(message);
        }
    }

    void performSearch(String searchedString) {
        Intent i = new Intent(SearchResult.this, SearchResultOnResult.class);
        stringToSearch = searchedString;

        i.putExtra("Search", searchedString);
        startActivityForResult(i, 2);


    }

    @Override
    public void onBackPressed() {
        vibrate();
        if (backPressed == 1)
            super.onBackPressed();
        else
            backPressed++;
    }


    private ArrayList<DataObject> getDataSet() {
//        ArrayList results = new ArrayList<DataObject>();
//        for (int index = 0; index < 20; index++) {
//            DataObject obj = new DataObject("" + index,
//                    "Secondary " + index);
//            results.add(index, obj);
//        }
        return results;
    }

    void vibrate() {
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(
                Context.VIBRATOR_SERVICE);
        vibrator.vibrate(60);

    }

}
