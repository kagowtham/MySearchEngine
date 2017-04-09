package com.codebuildrs.mysearchengine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    String searchword;
    ArrayList<String> array;
    String as[];
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView=(ListView)findViewById(R.id.listV);
        TextView textView=(TextView)findViewById(R.id.textView2);

        Intent intent=getIntent();
        searchword=intent.getStringExtra("word");
        textView.setText("Showing result for \""+searchword+"\" \n");
        search(searchword);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(array.get(position)));
                startActivity(i);
            }
        });

    }
    private void search(String searchword) {
        Task t=new Task(searchword);
        t.execute();
    }
    class Task extends AsyncTask {
        String searchword;
        Task(String s){
            searchword=s;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String a;
            array=new ArrayList<String>();
            try {
                URL u=new URL("https://www.googleapis.com/customsearch/v1?key="+"AIzaSyCi404ZeL3_EKMLfnuE-g0bLGWhnkovFwM"+"&cx=013847021561033763048:tsqbzspdjgu&q="+searchword+"&alt=json");
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                int i=0;
                while ((a = br.readLine()) != null) {

                    if(a.contains("\"link\": \"")){
                        String link=a.substring(a.indexOf("\"link\": \"")+("\"link\": \"").length(), a.indexOf("\","));
                        array.add(link);
                        Log.e("log",array.get(i++));
                    }
                    as=new String[i];

                }

                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(Main2Activity.this, R.layout.lista,array);
            listView.setAdapter(arrayAdapter);
        }
    }
}
