package com.ayush.hevenondemandapi1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hod.api.hodclient.HODApps;
import hod.api.hodclient.HODClient;
import hod.api.hodclient.IHODClientCallback;
import hod.response.parser.HODResponseParser;

public class ImageToText extends AppCompatActivity implements IHODClientCallback {
    String api = "bc7da6ae-8423-49e4-8f41-d7a058d0195b";

    HODClient hodClient;
    HODResponseParser hodParser;
    int controlVariable = 0;
   // TextView outtext;
    int finalValue;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.imagetext1);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);

    }

    public void click(View v) {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//// Start the Intent
//        startActivityForResult(galleryIntent, 1);

    }

    public void capture(View v) {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked

            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

//                Uri selectedImage = data.getData(); // Set the Image in ImageView after decoding the String
                String realPath;
                Uri selectedImageURI = data.getData();
                realPath=(getRealPathFromURI(selectedImageURI));

                //setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(),realPath);
                useHODClient(realPath);
                out(realPath);
                //Toast.makeText(this, realPath, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","");
                setResult(2,intent);
                finish();

            }
            if (requestCode == 5 && resultCode == RESULT_OK) {
                //Bitmap b = (Bitmap) data.getExtras().get("data");

//                ImageView i = (ImageView) findViewById(R.id.imageView);
//                i.setImageBitmap(b);


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Intent intent=new Intent();
            intent.putExtra("MESSAGE","");
            setResult(2,intent);
            finish();

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void useHODClient(String incompleteSearchString) {

        if (controlVariable == 0) {

            hodClient = new HODClient(api, this);
            hodParser = new HODResponseParser();

            String hodApp = HODApps.OCR_DOCUMENT;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("file",incompleteSearchString);
//        List<String> entities = new ArrayList<String>();
//        entities.add("people_eng");
//        entities.add("places_eng");
//        params.put("entity_type", entities);
//        params.put("unique_entities", "true");
            //CancelConnectorResponse can= /new CancelConnectorResponse();
            controlVariable = 0;
            mProgressDialog = new ProgressDialog(ImageToText.this);
            mProgressDialog.setTitle("Uploading Image");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            hodClient.PostRequest(params, hodApp, HODClient.REQ_MODE.SYNC);

        } else {
            controlVariable = 2;
        }
    }

    @Override
    public void requestCompletedWithContent(String response) {

        System.out.println(response);

        if (response != null) {
            System.out.println("No Error" + getLocalClassName());
            //    autoCorrect.clear();
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
            Intent intent=new Intent();
            intent.putExtra("MESSAGE","");
            setResult(2,intent);
            finish();

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
//        controlVariable=0;
    }
    void JsonAnalysis(String responce) {
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(responce);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("text_block");
String text="";
            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                String name = jsonObject.optString("text").toString();
                text=text+"\n"+name;

            }
            mProgressDialog.cancel();
            String[] splitString=text.split("\\r?\\n");
            ArrayList<String> arraySplit=new ArrayList<>();
            arraySplit.add(text);
            for(String s:splitString)
            {
//                if(s.matches("([A-Za-z]|[0-9]|[ ]|[_])+"))
                String s1="";
                int flag=0;
                    for(int i=0;i<s.length();i++) {
                        String c=s.charAt(i)+"";
                        flag=0;
                        if(c.matches("([A-Za-z]|[0-9]|[ ]|[_])"))
                        {
                            if(c!=" ")
                               flag=1;
                            s1=s1+c;
                        }

                    }

                if(flag==1)
                {
                    s1=s1.trim();
                    arraySplit.add(s1);
                }


            }
            String[] displayArray = new String[arraySplit.size()];
            displayArray = arraySplit.toArray(displayArray);
            Intent intent=new Intent();
            intent.putExtra("MESSAGE",displayArray);
            setResult(2,intent);
            finish();

            //selectSummaryType();
            System.out.print(text);
//           outtext=(TextView)findViewById(R.id.textView);
//            outtext.setText(text);

            if(controlVariable==2)
            {
                controlVariable=0;
               //
                // String text=searchEditText.getText().toString();
                if(text!=null)
                    useHODClient(text);
            }
            else
            {
                controlVariable=0;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        // controlVariable=0;
    }

    void out(String n) {
        System.out.println(n);
    }
    private void selectSummaryType(final String[] items) {

// arraylist to keep the selected items

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Text to Search")

                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
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
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE",items[finalValue]);
                        setResult(2,intent);
                        finish();

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
