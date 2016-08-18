package com.example.user.mop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MOP_Activity_History_of_MOP_AVM extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listViewForHistory;
    private TextView textViewForHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mop_activity_history_of_mop_avm);

        toolbar=(Toolbar) findViewById(R.id.my_toolbar);
        listViewForHistory = (ListView) findViewById(R.id.listViewForHistory);
        textViewForHistory = (TextView) findViewById(R.id.textViewForHistory);
        setSupportActionBar(toolbar);

        ParseTitle parseTitle = new ParseTitle();
        parseTitle.execute();

        try {
            final HashMap<String, String> hashMap = parseTitle.get();

            final ArrayList<String> arrayList = new ArrayList<>();
            for(Map.Entry entry : hashMap.entrySet()){
                arrayList.add(entry.getKey().toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MOP_Activity_History_of_MOP_AVM.this
                    ,android.R.layout.simple_list_item_1, arrayList);

            listViewForHistory.setAdapter(arrayAdapter);
            listViewForHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseText parseText = new ParseText();
                    parseText.execute(hashMap.get(arrayList.get(position)));

                    try {
                        listViewForHistory.setVisibility(View.GONE);
                        textViewForHistory.setText(parseText.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();

        if (id==R.id.news){
            Intent intent = new Intent(this, MOP_Activity_MOP_NEWS.class);
            startActivity(intent);
            return true;
        }
        if (id==R.id.schedule){
            Intent intent = new Intent(this, MOP_Activity_Timetable.class);
            startActivity(intent);
            return true;
        }
        if (id==R.id.About_the_department){
            Intent intent = new Intent(this, MOP_Activity_History_of_MOP_AVM.class);
            startActivity(intent);
            return true;
        }
        if (id==R.id.Send_news_to_social_networks){
            Intent intent = new Intent(this, MOP_Activity_Publication.class);
            startActivity(intent);
            return true;
        }
        if (id==R.id.authorization){
            Intent intent = new Intent(this, MOP_Activity_Autoresation.class);
            startActivity(intent);
            return true;
        }
        if (id==R.id.Enrollee){
            Intent intent = new Intent(this, MOP_Activity_Abiturient.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        listViewForHistory.setVisibility(View.VISIBLE);
        textViewForHistory.setVisibility(View.GONE);
    }

    class ParseText extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String str = " ";

            try {
                Document document = Jsoup.connect(params[0]).get();

                Element element = document.select(".wrapper").first();
                str = element.text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    class ParseTitle extends AsyncTask<Void, Void, HashMap<String, String>>{

        @Override
        protected HashMap<String, String> doInBackground(Void... params){
            HashMap<String, String> hashMap = new HashMap<>();
            try {
                Document document = Jsoup.connect("http://sfedu.ru/www/stat_pages22.show?p=HIS/main/M").get();
                Elements elements = document.select(".main");
                for(Element element : elements){
                    Element element1 = element.select("a[href]").first();
                    hashMap.put(element.text(), element1.attr("abs:href"));
                }
            } catch (IOException e){
                e.printStackTrace();
            }

            return hashMap;
        }
    }

}

