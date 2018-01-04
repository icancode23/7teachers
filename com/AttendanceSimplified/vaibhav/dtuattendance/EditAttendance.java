package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditAttendance extends AppCompatActivity implements OnClickListener {
    Button[] f18b;
    Button back;
    int bgint = Color.parseColor("#206757");
    int btint = Color.parseColor("#206757");
    String fin;
    int id;
    JSONObject json2;
    JSONObject json3;
    ProgressBar mProgressBar;
    String mes;
    String message2;
    downloaddata myDb2;
    String password;
    int f19s;
    int slots2;
    TextView tvloading;
    String username;
    int whiteInt = Color.parseColor("#ffffff");

    class C02261 implements OnClickListener {
        C02261() {
        }

        public void onClick(View v) {
            EditAttendance.this.mProgressBar.setVisibility(0);
            EditAttendance.this.tvloading.setText("Loading");
            EditAttendance.this.back.setEnabled(false);
            try {
                EditAttendance.this.offlinepopulate(EditAttendance.this.username, EditAttendance.this.password);
            } catch (JSONException e) {
                new Builder(EditAttendance.this).setMessage((CharSequence) e).setNegativeButton("Error Occured", null).create().show();
            }
        }
    }

    public class loginrequest1 extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 19090;
        int id;
        String message;
        String name2;
        String password2;
        TextView status2;
        String username2;

        loginrequest1() {
            this.username2 = EditAttendance.this.username;
            this.password2 = EditAttendance.this.password;
        }

        protected Void doInBackground(Void... arg0) {
            Exception e;
            try {
                Socket socket = new Socket(this.dstAddress, this.dstPort);
                Socket socket2;
                try {
                    JSONObject json = new JSONObject();
                    json.put("username", this.username2);
                    json.put("password", this.password2 + "");
                    String a = json.toString() + "\n";
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    byte[] s2 = Encrypt3.encrypt2(a);
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream dIn = new DataInputStream(socket.getInputStream());
                    int length = dIn.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0 && length < 102400) {
                        dIn.readFully(messages, 0, messages.length);
                    }
                    this.message = Encrypt3.decrypt2(messages);
                    JSONObject json2 = new JSONObject(this.message);
                    json2.put("username", this.username2);
                    json2.put("password", this.password2);
                    String js = json2.get("status").toString();
                    EditAttendance.this.fin = json2.toString();
                    this.id = Integer.parseInt(js);
                    socket2 = socket;
                } catch (Exception e2) {
                    e = e2;
                    socket2 = socket;
                    this.message = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                this.message = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            EditAttendance.this.mProgressBar.setVisibility(4);
            EditAttendance.this.back.setEnabled(true);
            EditAttendance.this.tvloading.setText("");
            if (this.id == 0) {
                new Builder(EditAttendance.this).setMessage("RETRY").setNegativeButton("OK", null).create().show();
                return;
            }
            Intent intent = new Intent(EditAttendance.this, GetSlots.class);
            intent.putExtra("message", EditAttendance.this.fin);
            EditAttendance.this.finish();
            EditAttendance.this.startActivity(intent);
        }
    }

    public class loginrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 17171;
        int id;
        String message;
        String name2;
        String password2;
        TextView status2;
        String username2;

        loginrequest() {
            try {
                this.username2 = EditAttendance.this.json2.get("username").toString();
                this.password2 = EditAttendance.this.json2.get("password").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(Void... arg0) {
            Socket socket;
            Exception e;
            try {
                Socket socket2 = new Socket(this.dstAddress, this.dstPort);
                try {
                    JSONObject json = new JSONObject();
                    json.put("username", this.username2);
                    json.put("password", this.password2 + "");
                    json.put("slot_id", EditAttendance.this.f19s);
                    byte[] s2 = Encrypt3.encrypt2(json.toString() + "\n");
                    DataOutputStream dOut = new DataOutputStream(socket2.getOutputStream());
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream is = new DataInputStream(socket2.getInputStream());
                    int length = is.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0 && length < 102400) {
                        is.readFully(messages, 0, messages.length);
                    }
                    EditAttendance.this.mes = Encrypt3.decrypt2(messages);
                    JSONObject json4 = new JSONObject(EditAttendance.this.mes);
                    String tab = json4.get("table").toString();
                    String js = json4.get("status").toString();
                    String batch = json4.get(downloaddata.COL_4).toString();
                    String time = json4.get("time").toString();
                    String subject = json4.get(downloaddata.COL_2).toString();
                    EditAttendance.this.json2.put("time", time);
                    EditAttendance.this.json2.put(downloaddata.COL_4, batch);
                    EditAttendance.this.json2.put(downloaddata.COL_2, subject);
                    EditAttendance.this.json2.put("tab", tab);
                    EditAttendance.this.fin = EditAttendance.this.json2.toString();
                    this.id = Integer.parseInt(js);
                    socket = socket2;
                } catch (Exception e2) {
                    e = e2;
                    socket = socket2;
                    this.message = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                this.message = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int row = 0; row < EditAttendance.this.slots2; row++) {
                EditAttendance.this.f18b[row].setEnabled(true);
            }
            EditAttendance.this.mProgressBar.setVisibility(4);
            EditAttendance.this.tvloading.setText("");
            if (this.id == 1) {
                Intent intent = new Intent(EditAttendance.this, insideEdit.class);
                intent.putExtra("message", EditAttendance.this.fin);
                EditAttendance.this.finish();
                EditAttendance.this.startActivity(intent);
                return;
            }
            new Builder(EditAttendance.this).setMessage("Connection Prob").setNegativeButton("Retry", null).create().show();
        }
    }

    public void onClick(View view) {
        this.mProgressBar.setVisibility(0);
        this.tvloading.setText("Loading");
        this.f19s = view.getId();
        for (int row = 0; row < this.slots2; row++) {
            this.f18b[row].setEnabled(false);
        }
        try {
            this.json2.put("slot_id", this.f19s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkInfo activeNetwork = ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            try {
                offlinepopulatestudents(this.f19s);
                return;
            } catch (JSONException e2) {
                e2.printStackTrace();
                return;
            }
        }
        new loginrequest().execute(new Void[0]);
    }

    private void offlinepopulatestudents(int s) throws JSONException {
        String finsend = dataforedit(s);
        Intent intent = new Intent(this, insideEdit.class);
        intent.putExtra("message", finsend);
        finish();
        startActivity(intent);
    }

    private String dataforedit(int s) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jfinal = new JSONObject();
        Cursor res = this.myDb2.getattendance();
        JSONObject jsend = new JSONObject();
        if (res.getCount() == 0) {
            return null;
        }
        while (res.moveToNext()) {
            String slot_id = "";
            String st2 = "";
            slot_id = res.getString(0);
            if (Integer.parseInt(slot_id) == s) {
                String dat = "";
                String subject = "";
                String time = "";
                String batch = "";
                JSONObject jedit = new JSONObject(res.getString(2).toString());
                dat = jedit.get("editattendance").toString();
                subject = jedit.get(downloaddata.COL_2).toString();
                time = jedit.get(downloaddata.COL_3).toString();
                batch = jedit.get(downloaddata.COL_4).toString();
                System.out.println(this.username);
                System.out.println(this.password);
                jsend.put(downloaddata.COL_2, subject);
                jsend.put(downloaddata.COL_4, batch);
                jsend.put("time", time);
                jsend.put("tab", dat);
                jsend.put("slot_id", slot_id);
                jsend.put("username", this.username);
                jsend.put("password", this.password);
                break;
            }
        }
        return jsend.toString();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0231R.layout.activity_edit_attendance);
        String mess = getIntent().getExtras().getString("message");
        this.myDb2 = new downloaddata(this);
        try {
            this.json2 = new JSONObject(mess);
            this.username = this.json2.get("username").toString();
            this.password = this.json2.get("password").toString();
            System.out.println("string is " + this.username + " " + this.password);
        } catch (Exception e) {
            System.out.println(e);
        }
        this.tvloading = (TextView) findViewById(C0231R.id.tvloading);
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        this.mProgressBar.setVisibility(8);
        this.back = (Button) findViewById(C0231R.id.back);
        this.back.setOnClickListener(new C02261());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 13;
        TableLayout layout = (TableLayout) findViewById(C0231R.id.tableforslots2);
        try {
            this.json2 = new JSONObject(mess);
            JSONArray jarray = new JSONArray(this.json2.get("table").toString());
            int slots = jarray.length();
            this.slots2 = slots;
            TextView[] t = new TextView[slots];
            this.f18b = new Button[slots];
            for (int row = 0; row < slots; row++) {
                LinearLayout tr = new LinearLayout(this);
                JSONObject js = new JSONObject(jarray.get(row).toString());
                t[row] = new TextView(this);
                t[row].setWidth(width);
                t[row].setHeight(height / 25);
                t[row].setText(js.get(downloaddata.COL_3) + " ");
                t[row].setTextColor(this.whiteInt);
                t[row].setBackgroundColor(this.bgint);
                t[row].setGravity(17);
                this.f18b[row] = new Button(this);
                this.f18b[row].setWidth(width);
                this.f18b[row].setHeight(height / 8);
                this.f18b[row].setTextColor(this.btint);
                this.f18b[row].setBackgroundColor(this.whiteInt);
                this.f18b[row].setText(js.get(downloaddata.COL_4) + "  | " + js.get(downloaddata.COL_2).toString());
                this.f18b[row].setGravity(17);
                this.f18b[row].setId(Integer.parseInt(js.get("slot_id").toString()));
                this.f18b[row].setOnClickListener(this);
                tr.addView(this.f18b[row]);
                layout.addView(t[row]);
                layout.addView(tr);
            }
        } catch (Exception e2) {
        }
    }

    private void offlinepopulate(String username, String password) throws JSONException {
        String col1;
        String col2;
        String col3;
        String col4;
        JSONArray jsonArray = new JSONArray();
        System.out.println("HERE I AM ");
        Cursor res = this.myDb2.getAllData();
        System.out.println("HERE I AM  2");
        if (res.getCount() == 0) {
            col1 = "slot_id";
            col2 = downloaddata.COL_2;
            col3 = downloaddata.COL_3;
            col4 = downloaddata.COL_4;
        } else {
            col1 = "slot_id";
            col2 = downloaddata.COL_2;
            col3 = downloaddata.COL_3;
            col4 = downloaddata.COL_4;
        }
        while (res.moveToNext()) {
            JSONObject j = new JSONObject();
            j.put(col1, res.getString(0));
            j.put(col2, res.getString(1));
            j.put(col3, res.getString(2));
            j.put(col4, res.getString(3));
            jsonArray.put(j);
        }
        JSONObject j2 = new JSONObject();
        j2.put("username", username);
        j2.put("password", password);
        j2.put("table", jsonArray);
        j2.put("init", 0);
        String fin = j2.toString();
        System.out.println("string is " + fin);
        Intent intent = new Intent(this, GetSlots.class);
        intent.putExtra("message", fin);
        finish();
        startActivity(intent);
    }
}
