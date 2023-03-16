package com.example.capstoneattmpt1shopandspeak;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Locale;


public class WebScrapper extends AppCompatActivity{

    TextToSpeech textToSpeech;
    String UpcCode;
    String test, test2, test3;
    Connection.Response res;
    Document doc;
    Intent x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_scrapper);

        Intent i = getIntent();
        UpcCode = i.getStringExtra("bcode");

        BackGroundThread();

    }

    /*

     */
    public void TryAgain(){

        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
        //Turning on the the TextToSpeech talker
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {

            // if No error is found then TextToSpeech can perform the translation
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech.setLanguage(Locale.getDefault());
                //Let the user know what actions are occurring
                textToSpeech.speak("I'm sorry, something went wrong, please scan again", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        textToSpeech.shutdown();


        Intent x = new Intent(WebScrapper.this, SpeechText.class);
        startActivity(x);
    }


    /*

     */
    public void BackGroundThread() {

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

                            if (link == null) {
                                TryAgain();
                            }

                            String url = link.absUrl("href");

                            if(url.isEmpty()){
                                TryAgain();
                            }

                            res = Jsoup.connect(url)
                                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                    .execute();

                            doc = res.parse();
                        }

                        Element foodName = doc.select("h1#food-name").first();
                        test = foodName.text();
                        if(test.contains("by")){
                           int end = test.indexOf("by");
                           test = test.substring(0, end);
                        }
                        test = test.replace(',', ' ');

                        Element ServingSize = doc.select("span#serving-size").first();
                        test2 = ServingSize.text();
                        test2 = test2.replace(',',' ');


                        Element Calories = doc.select("td#calories").first();
                        test3 = Calories.text();
                        test3 = test3.replace(',',' ');

                        x = new Intent(WebScrapper.this, ResultDisp.class);
                        x.putExtra("bcode", UpcCode);
                        x.putExtra("nameOfItem", test);
                        x.putExtra("Servings", test2);
                        x.putExtra("CaloriesPerServing", test3);


                    } catch (IOException e) {
                        Log.d("Connection.Response: ", "There is nothing stored under this bar code");
                        throw new RuntimeException(e);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                runOnUiThread(() -> {

                    new CountDownTimer(5000, 1000){
                        public void onFinish(){
                            startActivity(x);
                        }
                        @Override
                        public void onTick(long l) {} }.start();


                });
            }
        }).start();
    }

}
