package com.example.capstoneattmpt1shopandspeak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class WebScrapper extends AppCompatActivity{

    String UpcCode;
    String test, test2, test3;
    Connection.Response res;
    Document doc;
    Intent x;
    TextView a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_scrapper);

        Intent i = getIntent();
        UpcCode = i.getStringExtra("bcode");
        a = findViewById(R.id.ItemName);
        b = findViewById(R.id.SS);
        c = findViewById(R.id.Cal);

       new Thread (new Runnable() {
            @Override
            public void run() {
                try  {
                    try {
                        res = Jsoup.connect("https://www.nutritionvalue.org/search.php")
                                .data("food_query", UpcCode)
                                .method(Connection.Method.POST)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .execute();

                        doc = res.parse();

                        if(doc.title().contains("foods")) {

                            Element link = doc.select("td.left > a.table_item_name").first();

                            String url = link.absUrl("href");

                            Log.d("URL for new one", url);

                           res = Jsoup.connect(url)
                                   .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                   .execute();

                           doc = res.parse();
                        }

                        //TODO Check for commas in names and check to make sure names arent too long, then we need to tidy up url connections

                        Elements foodName = doc.select("h1#food-name");

                        for (Element x : foodName) {
                            test = x.text();
                        }

                        Elements ServingSize = doc.select("span#serving-size");
                        for (Element x : ServingSize) {
                                test2 = x.text();
                        }

                        Elements Calories = doc.select("td#calories");
                        for (Element x : Calories) {
                                test3 = x.text();
                        }
                            x = new Intent(WebScrapper.this, ResultDisp.class);
                            x.putExtra("bcode", UpcCode);
                            x.putExtra("nameOfItem", test);
                            x.putExtra("Servings", test2);
                            x.putExtra("CaloriesPerServing", test3);
                            startActivity(x);

                    } catch (IOException e) {
                        Log.d("Connection.Response: ", "There is nothing stored under this bar code");
                        throw new RuntimeException(e);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        a.setText(test);
                        b.setText(test2);
                        c.setText(test3);

                    }
                });
            }
        }).start();

    }

}
