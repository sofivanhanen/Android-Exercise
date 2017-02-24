package com.example.sofi.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://external.api.yle.fi/";

    private EditText editText;
    private Button button;
    private TextView nothingFound;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.et_main); //Finding views
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch(editText.getText().toString()); //Get search term from text field
            }
        });
        nothingFound = (TextView) findViewById(R.id.tv_nothing_found);
        listView = (ListView) findViewById(R.id.list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void doSearch(final String searchTerm) {
        /* Did this to check if searchTerm is null when editText is empty
        if (searchTerm.equals(null)) {
            Log.w("doSearch", "got null string");
        }
        */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YleApiService service = retrofit.create(YleApiService.class);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Call<BaseData> call = service
                .getBaseData(preferences.getString(getString(R.string.pref_app_id_key), getString(R.string.app_id)), //Getting key % id from shared preferences
                        preferences.getString(getString(R.string.pref_app_key_key), getString(R.string.app_key)));

        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                Log.i("onResponse", "Enqueue successful");

                try {
                    ArrayList<Show> shows = response.body().getRelevantData(searchTerm); //Search happens here + in BaseData (response)
                    buildList(shows);
                } catch (Exception e) {
                    Log.e("onResponse", "Shows is null");
                    Toast toast = Toast.makeText(getApplicationContext(), "Couldn't connect to the API. Did you input the correct app key & app id? Is there internet access?", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Couldn't connect to the API. Did you input the correct app key & app id? Is there internet access?", Toast.LENGTH_LONG);
                toast.show();
                Log.w("onFailure", "Couldn't enqueue");
            }
        });
    }

    private void buildList(final ArrayList<Show> shows) {

        final String[] titles = new String[shows.size()];

        int i = 0;
        for (Show show : shows) { //Turning show titles into an array
            titles[i] = show.getItemTitle();
            i++;
        } //Has to be an array for ArrayAdapter to work

        if (titles.length == 0) { //Nothing matches search
            Log.i("buildList", "titles was empty");
            listView.setVisibility(View.INVISIBLE);
            nothingFound.setVisibility(View.VISIBLE);
        } else {
            Log.i("buildList", "titles wasn't empty");
            nothingFound.setVisibility(View.INVISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, titles);
            listView.setAdapter(adapter);
            AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detailIntent = new Intent(getApplicationContext(), DetailsActivity.class);

                    Show show = shows.get(position);
                    detailIntent.putExtra("Title", show.getItemTitle());
                    detailIntent.putExtra("Information", show.toStringWithoutTitle());

                    startActivity(detailIntent);
                }
            };
            listView.setOnItemClickListener(clickListener);
            listView.setVisibility(View.VISIBLE);
        }
    }
}
