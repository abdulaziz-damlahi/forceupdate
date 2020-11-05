package ders.yasin.jsonparsing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity<currentV> extends AppCompatActivity {
    ListView lvCompanies;
    ImageView ivLogo;
    String[] logoURLS;
   Integer currentV=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivLogo=findViewById(R.id.iv_Logo);
        lvCompanies=findViewById(R.id.lv_Companies);

        String URL="https://staging.yostek.net/api/SupportedBuilds";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.GET, URL,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    int lengthOfArray=jsonArray.length();
                    String[] companyInfo=new String[lengthOfArray];
                    String[] build=new String[lengthOfArray];
                    String[] current=new String[lengthOfArray];

                    logoURLS=new String[lengthOfArray];

                    for(int i=0;i<lengthOfArray;i++) {
                        JSONObject companies = jsonArray.getJSONObject(i);
                        String buildNumber1 = companies.getString("buildNumber");

                        int bulitnumberint = Integer.decode(buildNumber1);

                        String osType1 = companies.getString("osType");
                        String updatePriority1 = companies.getString("updatePriority");

                        //int updatePriorityint = Integer.decode(updatePriority1);
                         int updatePriorityint = 1;
                        //int currentVersion = android.os.Build.VERSION.SDK_INT;
                        companyInfo[0] = "api build number" + buildNumber1 + "," + osType1 + "," + updatePriority1 + "," + currentV + ",";
                        build[0] =  buildNumber1;


                        if (bulitnumberint>currentV){

                            if (updatePriorityint==0){


                           AlertDialog.Builder myAlertBuilder =new AlertDialog.Builder(MainActivity.this);
                           myAlertBuilder.setTitle("suggested update");
                           myAlertBuilder.setMessage("you have to update the program ");
                           myAlertBuilder.setPositiveButton("open store", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                                   Toast.makeText(MainActivity.this, "you opened store", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=net.arroot.yosar"));
                                   startActivity(intent);



                               }
                           });

                                myAlertBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "you canceled", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                myAlertBuilder.show();
                            }else{

                                    //send to forcedownload
                                Intent intent = new Intent(MainActivity.this, forcedownload.class);
                                startActivity(intent);


                            }

                        }else{Toast.makeText(MainActivity.this, "you should not update the app", Toast.LENGTH_SHORT).show();}

                    }




                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,companyInfo);
                    lvCompanies.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Error",error.getMessage());
            }
        });

        queue.add(request);

        lvCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int clickedIndex, long l) {
                Picasso.get().load(logoURLS[clickedIndex]).into(ivLogo);
            }
        });
    }
}
