package ir.ashkanabd.lineup;

import android.app.*;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class DownloadActivity extends AppCompatActivity {

    public static int GET_ALL_TEAMS = 1;
    public static int GET_TEAM = 2;
    public static String MAIN_ADDRESS = "http://ashkanabd.ir/lineup/";
    public static String NETWORK_ERROR = "network";
    public static String JSON_ERROR = "json";
    public static String SUCCESSFUL = "successful";
    public static String UNKNOWN_ERROR = "other";
    String line, selectedLeague;
    Scanner scn;
    ListView leagueListView;
    ArrayAdapter<String> leagueAdapter;
    File parent, file;
    InputStream is;
    FileOutputStream fos;
    Iterator iterator;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProgressDialog loadDialog;
    DownloadImage downloadImage;
    List<String> leagueList, teamList;
    GetData getData;
    AlertDialog.Builder alertDialog;
    boolean taskEnded = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);
        widget();
        leagueList = new ArrayList<>();
        teamList = new ArrayList<>();
        alertDialog = new AlertDialog.Builder(this);
        loadDialog = new ProgressDialog(this);
        parent = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/" + getPackageName() + "/download");
        if (!parent.exists()) {
            parent.mkdirs();
        }
        setListView(leagueList);
        getData = new GetData(GET_ALL_TEAMS);
        getData.execute();
    }

    private void widget() {
        leagueListView = (ListView) findViewById(R.id.leagueListView);
    }

    private void setListView(final List<String> leagueList) {
        leagueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, leagueList);
        leagueListView.setAdapter(leagueAdapter);
        leagueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLeague = leagueList.get(position);
                getData = new GetData(GET_TEAM);
                getData.execute(selectedLeague);
            }
        });
    }

    private class DownloadImage extends AsyncTask<String, String, String> {

        List<String> urlList;
        List<String> imgList;
        String leagueName;

        DownloadImage(List<String> urlList, String leagueName) {
            this.urlList = urlList;
            imgList = new ArrayList<>();
            this.leagueName = leagueName;
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            try {
                Log.w("LOG", "download img");
                for (String string : urlList) {
                    Log.w("LOG", string);
                    url = new URL(MAIN_ADDRESS + leagueName + "/" + string);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    is = connection.getInputStream();
                    file = new File(parent, string);
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                    }
                    fos.close();
                    is.close();
                    publishProgress(urlList.size() + "", imgList.size() + "");
                    imgList.add(string);
                }
                return SUCCESSFUL;
            } catch (IOException e) {
                e.printStackTrace();
                return UNKNOWN_ERROR;
            }
        }

        @Override
        protected void onPreExecute() {
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(false);
            loadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            loadDialog.setTitle("Downloading...");
            loadDialog.setMessage("Please wait...");
            loadDialog.show();
            taskEnded = false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(false);
            loadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            loadDialog.setTitle("Downloading...");
            loadDialog.setMessage("Please wait...");
            loadDialog.setMax(Integer.parseInt(values[0]));
            loadDialog.setProgress(Integer.parseInt(values[1]));
            loadDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals(UNKNOWN_ERROR)) {
                loadDialog.dismiss();
                alertDialog.setMessage("Please check your network and permission").setTitle("Unknown error")
                        .setCancelable(false).setIcon(android.R.drawable.stat_notify_error).setPositiveButton("OK", null).show();
            }
            taskEnded = true;
            loadDialog.dismiss();
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {

        int flag;
        String leagueName = "";

        GetData(int flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            if (flag == GET_ALL_TEAMS) {
                Log.w("LOG", "get league info");
                leagueList.clear();
                try {
                    url = new URL(MAIN_ADDRESS);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    scn = new Scanner(connection.getInputStream());
                } catch (IOException e) {
                    return NETWORK_ERROR;
                }
                try {
                    line = scn.nextLine();
                    jsonObject = new JSONObject(line);
                    iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        leagueList.add(iterator.next() + "");
                    }
                    return SUCCESSFUL;
                } catch (JSONException e) {
                    Log.w("LOG", "json error");
                    e.printStackTrace();
                    return JSON_ERROR;
                }
            } else {
                teamList.clear();
                try {
                    Log.w("LOG", "get team urls");
                    leagueName = params[0];
                    url = new URL(MAIN_ADDRESS + leagueName);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    scn = new Scanner(connection.getInputStream());
                } catch (IOException e) {
                    return NETWORK_ERROR;
                }
                try {
                    jsonArray = new JSONArray(scn.nextLine());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        teamList.add(jsonArray.get(i) + "");
                    }
                    return SUCCESSFUL;
                } catch (JSONException e) {
                    Log.w("LOG", "json error");
                    e.printStackTrace();
                    return JSON_ERROR;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(false);
            loadDialog.setTitle("Please wait...");
            loadDialog.setMessage("Get data from server");
            loadDialog.show();
            taskEnded = false;
        }

        @Override
        protected void onPostExecute(String s) {
            taskEnded = true;
            loadDialog.dismiss();
            if (s.equals(NETWORK_ERROR)) {
                alertDialog.setMessage("Can't connect to server\nPlease check your network").setTitle("Network Error")
                        .setCancelable(false).setIcon(android.R.drawable.stat_notify_error).setPositiveButton("OK", null).show();
            } else if (s.equals(JSON_ERROR)) {
                alertDialog.setMessage("Something wrong\nPlease try agian later").setTitle("Internal Error")
                        .setCancelable(false).setIcon(android.R.drawable.stat_notify_error).setPositiveButton("OK", null).show();
            }
            if (flag == GET_ALL_TEAMS) {
                Log.w("LOG", "update listview");
                setListView(leagueList);
            } else {
                Log.w("LOG", "start downloading");
                downloadImage = new DownloadImage(teamList, leagueName);
                downloadImage.execute();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (taskEnded)
            super.onBackPressed();
    }
}
