package ders.yasin.jsonparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    ListView lvCompanies;
    ImageView ivLogo;
    String[] logoURLS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivLogo=findViewById(R.id.iv_Logo);
        lvCompanies=findViewById(R.id.lv_Companies);

        //String URL="http://www.mustafaaksin.com/web/recyle.json";
        String URL="http://web.karabuk.edu.tr/yasinortakci/dokumanlar/web_dokumanlari/recyle.json";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.GET, URL,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("Companies");
                    int lengthOfArray=jsonArray.length();
                    String[] companyInfo=new String[lengthOfArray];
                    logoURLS=new String[lengthOfArray];

                    for(int i=0;i<lengthOfArray;i++){
                        JSONObject companies=jsonArray.getJSONObject(i);
                        String heading=companies.getString("Heading");
                        String detail=companies.getString("Detail");
                        String imageURL=companies.getString("ImageURL");
                        companyInfo[i]=heading+","+detail;
                        logoURLS[i]=imageURL;
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
