package com.ayush.hevenondemandapi1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import hod.response.parser.HODResponseParser;

/**
 * Created by user on 7/9/2016.
 */
public class MainPageImageAnalysis extends Activity {
//    ImageButton searchView;
//    AutoCompleteTextView searchEditText;
//    String api = "bc7da6ae-8423-49e4-8f41-d7a058d0195b";
//    Button cameraButton,galleryButton;
//    HODClient hodClient;
//    HODResponseParser hodParser;
//    ArrayList<String> autoCorrect;
//    int controlVariable=0;
//    ImageView imageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.search_page);
//        initView();
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                useHODClient();
//            }
//        });
//    }
//
//    void initView() {
//    cameraButton=(Button)findViewById(R.id.buttoncamera);
//        galleryButton=(Button)findViewById(R.id.buttongallery);
//        imageView=(ImageView)findViewById(R.id.imageView);
//cameraButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//   selectImage();
//    }
//});
//
//    }
//    String userChoosenTask;
//    private void selectImage() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainPageImageAnalysis.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                //boolean result=Utility.checkPermission(MainActivity.this);
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask="Take Photo";
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, REQUEST_CAMERA);
//
//                    }
//                 else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask="Choose from Library";
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);//
//                    startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    void performSearch(String searchedString) {
//        Intent i=new Intent(MainPageImageAnalysis.this,SearchResult.class);
//
//        i.putExtra("Search",searchedString);
//        startActivity(i);
//
//    }
//
//    private void useHODClient(CharSequence incompleteSearchString) {
//
//        if (controlVariable == 0) {
//
//            hodClient = new HODClient(api, this);
//            hodParser = new HODResponseParser();
//
//            String hodApp = HODApps.AUTO_COMPLETE;
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("text", incompleteSearchString.toString());
////        List<String> entities = new ArrayList<String>();
////        entities.add("people_eng");
////        entities.add("places_eng");
////        params.put("entity_type", entities);
////        params.put("unique_entities", "true");
//            //CancelConnectorResponse can= /new CancelConnectorResponse();
//controlVariable=1;
//            hodClient.GetRequest(params, hodApp, HODClient.REQ_MODE.SYNC);
//
//        } else {
//            controlVariable = 2;
//        }
//    }
//    // define a custom response class
//    public class EntityExtractionResponse {
//        public List<Entity> entities;
//
//        public class AdditionalInformation {
//            public List<String> person_profession;
//            public String person_date_of_birth;
//            public String wikipedia_eng;
//            public Long place_population;
//            public String place_country_code;
//            public Double place_elevation;
//        }
//
//        public class Entity {
//            public String normalized_text;
//            public String type;
//            public AdditionalInformation additional_information;
//        }
//    }
//
//    // implement callback functions
//    @Override
//    public void requestCompletedWithContent(String response) {
//        EntityExtractionResponse resp = (EntityExtractionResponse) hodParser.ParseCustomResponse
//                (EntityExtractionResponse.class, response);
//        //System.out.println(response);
//
//        if (resp != null) {
//            System.out.println("No Error"+getLocalClassName());
//            autoCorrect.clear();
//            JsonAnalysis(response);
////            String values = "";
////            for (EntityExtractionResponse.Entity ent : resp.entities) {
////                values += ent.type + "\n";
////                values += ent.normalized_text + "\n";
////                if (ent.type.equals("places_eng")) {
////                    values += ent.additional_information.place_country_code + "\n";
////                    values += ent.additional_information.place_elevation + "\n";
////                    values += ent.additional_information.place_population + "\n";
////                } else if (ent.type.equals("people_eng")) {
////                    values += ent.additional_information.person_date_of_birth + "\n";
////                    values += ent.additional_information.person_profession + "\n";
////                    values += ent.additional_information.wikipedia_eng + "\n";
////                }
////            }
////            System.out.print(values);
////            // print the values
//        } else {
////            List<HODErrorObject> errors = parser.GetLastError();
////            String errorMsg = "";
////            for (HODErrorObject err : errors) {
////                errorMsg += String.format("Error code: %d\nError Reason: %s\n", err.error, err.reason);
////                if (err.detail != null)
////                    errorMsg += "Error detail: " + err.detail + "\n";
////                // handle error message
//            controlVariable=0;
//            System.out.println("Error");
//        }
//    }
//
//    @Override
//    public void requestCompletedWithJobID(String response) {
//        System.out.print(response);
//
//    }
//
//    @Override
//    public void onErrorOccurred(String errorMessage) {
//        // handle error if any
//        out(errorMessage);
//        controlVariable=0;
//    }
//
//    void JsonAnalysis(String responce) {
//        String data = "";
//        try {
//            JSONObject jsonRootObject = new JSONObject(responce);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.optJSONArray("words");
//
//            //Iterate the jsonArray and print the info of JSONObjects
//            for (int i = 0; i < jsonArray.length(); i++) {
//                //JSONObject jsonObject = jsonArray.getString(i);
//
//                String name = jsonArray.getString(i);
//                autoCorrect.add(name);
//
//            }
//            System.out.print(autoCorrect);
//            String[] arr = autoCorrect.toArray(new String[autoCorrect.size()]);
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                    (this,android.R.layout.select_dialog_item, arr);
//
//            searchEditText.setThreshold(1);
//            searchEditText.setAdapter(adapter);
//            if(controlVariable==2)
//            {
//                controlVariable=0;
//                String text=searchEditText.getText().toString();
//                if(text!=null)
//                    useHODClient(text);
//            }
//            else
//            {
//                controlVariable=0;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//        }
//       // controlVariable=0;
//    }
//    void out(String n)
//    {
//        System.out.println(n);
//    }
//}
}