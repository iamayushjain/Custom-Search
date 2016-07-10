package com.ayush.hevenondemandapi1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hod.api.hodclient.HODApps;
import hod.api.hodclient.HODClient;
import hod.api.hodclient.IHODClientCallback;
import hod.response.parser.HODErrorObject;
import hod.response.parser.HODResponseParser;

public class MainActivity extends AppCompatActivity implements IHODClientCallback,AdapterView.OnItemSelectedListener {

    WebView wvContainer;
    // text input for entering web address
    EditText etWebPageAddress;
    // string to hold the current web address
    String mLink = "";
    String api = "bc7da6ae-8423-49e4-8f41-d7a058d0195b";
    String category[]={"Search By Pincode","Search by District"};

    HODClient hodClient;
    HODResponseParser hodParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

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
        hodClient = new HODClient(api, this);
        hodParser = new HODResponseParser();

        useHODClient();
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
    private void useHODClient() {
        String hodApp = HODApps.ENTITY_EXTRACTION;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("url", "http://www.cnn.com");
        List<String> entities = new ArrayList<String>();
        entities.add("people_eng");
        entities.add("places_eng");
        params.put("entity_type", entities);
        params.put("unique_entities", "true");

        hodClient.GetRequest(params, hodApp, HODClient.REQ_MODE.SYNC);
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
        System.out.print(response);

        if (resp != null) {
            String values = "";

            System.out.print(values);
            // print the values
        } else {
//            List<HODErrorObject> errors = parser.GetLastError();
//            String errorMsg = "";
//            for (HODErrorObject err : errors) {
//                errorMsg += String.format("Error code: %d\nError Reason: %s\n", err.error, err.reason);
//                if (err.detail != null)
//                    errorMsg += "Error detail: " + err.detail + "\n";
//                // handle error message
            }
        }

    @Override
    public void requestCompletedWithJobID(String response) {

    }

    @Override
    public void onErrorOccurred(String errorMessage) {
        // handle error if any
    }
}