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

/**
 * This Activity is responsible for scrapping the data from the website we have given it 
 * 
 *      textToSpeech = a TTS object that we will use to speak information to the user
 *      
 *      UpcCode = A String that will hold the UPC code that the user scanned (or at least what the API interpreted)
 *      
 *      Food_name, ServSize, CalsPerServ = The data items we care about that we are scrapping off the web to inform the user TODO (FOR NOW, EVENTUALLY MAIN FRAME DB2 SQL queries)
 *
 *      res = The response we get from the html POST we performed on the search.php script to search for the item
 *
 *      doc = The Document object that we are going to parse the HTML and find the data points to display for the user
 *
 *      x = and Intent that we will use to pass control over to the ResultDisp Activity after we load the data.
 *
 *      This activity works as a Web scrapper in the back-end while looking like a loading screen on the front-end.
 */
public class WebScrapper extends AppCompatActivity{

    TextToSpeech textToSpeech;
    String UpcCode;
    String Food_Name, ServSize, CalsPerServ;
    Connection.Response res;
    Document doc;
    Intent x;


    /**
     *
     * @param savedInstanceState - Creating the Instance of of the the last known saved state
     *                                in case there was any data that needed to be stored passively, allows
     *                                us to pick up right where we left off.
     *
     *      This method will start by acquiring the UPC code that we are going to be giving information on
     *             and then it starts the scrapping process in a background thread.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_scrapper);

        Intent i = getIntent();
        UpcCode = i.getStringExtra("bcode");

        BackGroundThread();

    }

    /**
     * This method is used just in case the Barcode Scanner doesn't understand the UPC that was interpreted.
     *      At the end of this method it throws the user right back to the SpeechText() and will allow
     *      the user to try the scanning process again.
     */
    public void TryAgain(){

        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
        /** Turning on the the TextToSpeech talker */
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {

            /** if No error is found then TextToSpeech can perform the translation */
            if (i != TextToSpeech.ERROR) {
                /** To Choose language of speech */
                textToSpeech.setLanguage(Locale.getDefault());
                /** Let the user know what actions are occurring */
                textToSpeech.speak("I'm sorry, something went wrong, please scan again", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        textToSpeech.shutdown();

        /** Starting the SpeechText Activity over again */
        Intent x = new Intent(WebScrapper.this, SpeechText.class);
        startActivity(x);
    }


    /**
     *  This is the Web Scrapping thread where we begin with submitting a HTML POST for the UPC code of
     *      the item scanned and then returns the resulting page query into the 'res' object that we can
     *      then place in a Document and parse the HTML tags.
     *
     *  After we scan the result of the POST we then decide what we need to do with the data next ultimately ending with
     *  the products information being displayed for the user
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
                        Food_Name = foodName.text();
                        if(Food_Name.contains("by")){
                           int end = Food_Name.indexOf("by");
                           Food_Name = Food_Name.substring(0, end);
                        }
                        Food_Name = Food_Name.replace(',', ' ');

                        Element ServingSize = doc.select("span#serving-size").first();
                        ServSize = ServingSize.text();
                        ServSize = ServSize.replace(',',' ');


                        Element Calories = doc.select("td#calories").first();
                        CalsPerServ = Calories.text();
                        CalsPerServ = CalsPerServ.replace(',',' ');

                        x = new Intent(WebScrapper.this, ResultDisp.class);
                        x.putExtra("bcode", UpcCode);
                        x.putExtra("nameOfItem", Food_Name);
                        x.putExtra("Servings", ServSize);
                        x.putExtra("CaloriesPerServing", CalsPerServ);


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
