package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
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

public class Unmarked_slots extends AppCompatActivity implements OnClickListener {
    Button[] f22b;
    Button back;
    int bgint = Color.parseColor("#206757");
    int btint = Color.parseColor("#206757");
    String fin;
    JSONObject json2;
    JSONObject json3;
    ProgressBar mProgressBar;
    String mes;
    String message2;
    String password;
    int f23s;
    int slots2;
    TextView tvloading;
    String username;
    int whiteInt = Color.parseColor("#ffffff");

    class C02321 implements OnClickListener {
        C02321() {
        }

        public void onClick(View v) {
            Unmarked_slots.this.mProgressBar.setVisibility(0);
            Unmarked_slots.this.tvloading.setText("Loading");
            Unmarked_slots.this.back.setEnabled(false);
            new loginrequest1().execute(new Void[0]);
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
            this.username2 = Unmarked_slots.this.username;
            this.password2 = Unmarked_slots.this.password;
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
                    Unmarked_slots.this.fin = json2.toString();
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
            Unmarked_slots.this.mProgressBar.setVisibility(4);
            Unmarked_slots.this.back.setEnabled(true);
            Unmarked_slots.this.tvloading.setText("");
            if (this.id == 0) {
                new Builder(Unmarked_slots.this).setMessage("RETRY").setNegativeButton("OK", null).create().show();
                return;
            }
            Intent intent = new Intent(Unmarked_slots.this, GetSlots.class);
            intent.putExtra("message", Unmarked_slots.this.fin);
            Unmarked_slots.this.finish();
            Unmarked_slots.this.startActivity(intent);
        }
    }

    public class loginrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 17070;
        int id;
        String message;
        String name2;
        String password2;
        TextView status2;
        String username2;

        loginrequest() {
            try {
                this.username2 = Unmarked_slots.this.json2.get("username").toString();
                this.password2 = Unmarked_slots.this.json2.get("password").toString();
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
                    json.put("slot_id", Unmarked_slots.this.f23s);
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
                    Unmarked_slots.this.mes = Encrypt3.decrypt2(messages);
                    JSONObject json4 = new JSONObject(Unmarked_slots.this.mes);
                    String tab = json4.get("table").toString();
                    String js = json4.get("status").toString();
                    String batch = json4.get(downloaddata.COL_4).toString();
                    String time = json4.get("time").toString();
                    String subject = json4.get(downloaddata.COL_2).toString();
                    Unmarked_slots.this.json2.put("time", time);
                    Unmarked_slots.this.json2.put(downloaddata.COL_4, batch);
                    Unmarked_slots.this.json2.put(downloaddata.COL_2, subject);
                    Unmarked_slots.this.json2.put("tab", tab);
                    Unmarked_slots.this.fin = Unmarked_slots.this.json2.toString();
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
            Unmarked_slots.this.mProgressBar.setVisibility(4);
            Unmarked_slots.this.tvloading.setText("");
            for (int row = 0; row < Unmarked_slots.this.slots2; row++) {
                Unmarked_slots.this.f22b[row].setEnabled(true);
            }
            if (this.id == 1) {
                Intent intent = new Intent(Unmarked_slots.this, insidelogin_test.class);
                intent.putExtra("message", Unmarked_slots.this.fin);
                Unmarked_slots.this.finish();
                Unmarked_slots.this.startActivity(intent);
                return;
            }
            new Builder(Unmarked_slots.this).setMessage("Connection Prob").setNegativeButton("Retry", null).create().show();
        }
    }

    public void onClick(View view) {
        this.mProgressBar.setVisibility(0);
        this.tvloading.setText("Loading");
        this.tvloading.setGravity(17);
        this.f23s = view.getId();
        for (int row = 0; row < this.slots2; row++) {
            this.f22b[row].setEnabled(false);
        }
        try {
            this.json2.put("slot_id", this.f23s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new loginrequest().execute(new Void[0]);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0231R.layout.activity_unmarked_slots);
        String mess = getIntent().getExtras().getString("message");
        this.back = (Button) findViewById(C0231R.id.back);
        try {
            this.json2 = new JSONObject(mess);
            this.username = this.json2.get("username").toString();
            this.password = this.json2.get("password").toString();
        } catch (Exception e) {
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        this.mProgressBar.setVisibility(8);
        this.tvloading = (TextView) findViewById(C0231R.id.tvloading);
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 13;
        this.back.setOnClickListener(new C02321());
        TableLayout layout = (TableLayout) findViewById(C0231R.id.tableforslots2);
        try {
            this.json2 = new JSONObject(mess);
            JSONArray jarray = new JSONArray(this.json2.get("table").toString());
            int slots = jarray.length();
            this.slots2 = slots;
            TextView[] t = new TextView[slots];
            this.f22b = new Button[slots];
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
                this.f22b[row] = new Button(this);
                this.f22b[row].setWidth(width);
                this.f22b[row].setHeight(height / 8);
                this.f22b[row].setTextColor(this.btint);
                this.f22b[row].setBackgroundColor(this.whiteInt);
                this.f22b[row].setText(js.get(downloaddata.COL_4) + "  | " + js.get(downloaddata.COL_2).toString());
                this.f22b[row].setGravity(17);
                this.f22b[row].setId(Integer.parseInt(js.get("slot_id").toString()));
                this.f22b[row].setOnClickListener(this);
                tr.addView(this.f22b[row]);
                layout.addView(t[row]);
                layout.addView(tr);
            }
        } catch (Exception e2) {
        }
    }
}
