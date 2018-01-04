package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
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
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetSlots extends AppCompatActivity implements OnClickListener {
    NetworkInfo activeNetwork;
    Button[] f20b;
    Button b2;
    int bgint = Color.parseColor("#206757");
    Button blogout;
    Button bsync;
    int btint = Color.parseColor("#206757");
    ConnectivityManager conMgr;
    String fin;
    int id;
    JSONObject json2;
    JSONObject json3;
    ProgressBar mProgressBar;
    String mes;
    String message2;
    getsyncdata myDb;
    downloaddata myDb2;
    downloaddata myDb3;
    int f21s;
    int slots2;
    Button upattend;
    int whiteInt = Color.parseColor("#ffffff");

    class C02271 implements OnClickListener {
        C02271() {
        }

        public void onClick(View view) {
            GetSlots.this.conMgr = (ConnectivityManager) GetSlots.this.getApplicationContext().getSystemService("connectivity");
            GetSlots.this.activeNetwork = GetSlots.this.conMgr.getActiveNetworkInfo();
            if (GetSlots.this.activeNetwork == null || !GetSlots.this.activeNetwork.isConnected()) {
                new Builder(GetSlots.this).setMessage("No Internet").setNegativeButton("OK", null).create().show();
                return;
            }
            GetSlots.this.bsync.setEnabled(false);
            GetSlots.this.b2.setEnabled(false);
            GetSlots.this.blogout.setEnabled(false);
            GetSlots.this.upattend.setEnabled(false);
            GetSlots.this.myDb.senddata();
            GetSlots.this.mProgressBar.setVisibility(0);
            GetSlots.this.upattend.setText(" Syncing ");
            GetSlots.this.b2.setText(" Syncing ");
            String str = "";
            String password2 = "";
            try {
                str = GetSlots.this.json2.get("username").toString();
                password2 = GetSlots.this.json2.get("password").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Syncrequest(str, password2).execute(new Void[0]);
        }
    }

    class C02282 implements OnClickListener {
        C02282() {
        }

        public void onClick(View v) {
            GetSlots.this.myDb2.truncatestudent();
            GetSlots.this.myDb2.truncateslotstudent();
            GetSlots.this.myDb2.truncateslots();
            Editor editor = GetSlots.this.getSharedPreferences(front.PREFS_NAME, 0).edit();
            editor.remove("logged");
            editor.remove("username");
            editor.remove("password");
            editor.remove("token");
            editor.commit();
            Toast.makeText(GetSlots.this.getApplicationContext(), "Logout Successfull", 1).show();
            Intent intent = new Intent(GetSlots.this, front.class);
            GetSlots.this.finish();
            GetSlots.this.startActivity(intent);
        }
    }

    class C02293 implements OnClickListener {
        C02293() {
        }

        public void onClick(View v) {
            GetSlots.this.mProgressBar.setVisibility(0);
            GetSlots.this.upattend.setText(" Loading ");
            GetSlots.this.conMgr = (ConnectivityManager) GetSlots.this.getApplicationContext().getSystemService("connectivity");
            GetSlots.this.activeNetwork = GetSlots.this.conMgr.getActiveNetworkInfo();
            if (GetSlots.this.activeNetwork == null || !GetSlots.this.activeNetwork.isConnected()) {
                try {
                    String finsend = GetSlots.this.dataforedit();
                    System.out.println("string is :" + finsend);
                    Intent intent = new Intent(GetSlots.this, EditAttendance.class);
                    intent.putExtra("message", finsend);
                    GetSlots.this.finish();
                    GetSlots.this.startActivity(intent);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            new updaterequest().execute(new Void[0]);
        }
    }

    class C02304 implements OnClickListener {
        C02304() {
        }

        public void onClick(View v) {
            GetSlots.this.mProgressBar.setVisibility(0);
            GetSlots.this.b2.setText(" Loading ");
            new unmarked_slot_request().execute(new Void[0]);
        }
    }

    public class Syncrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 4444;
        String message;
        String name2;
        String password2;
        TextView status2;
        String username2;

        Syncrequest(String usern, String password) {
            this.username2 = usern;
            this.password2 = password;
        }

        protected Void doInBackground(Void... arg0) {
            String message = "";
            String testServerName = "attendancesimplified.com";
            JSONObject json = new JSONObject();
            try {
                GetSlots.this.id = 0;
                SocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(testServerName), this.dstPort);
                Socket socket = new Socket();
                Socket socket2;
                try {
                    socket.connect(inetSocketAddress, 10000);
                    json.put("username", this.username2);
                    json.put("password", this.password2);
                    String s = json.toString();
                    System.out.println(s + "\n");
                    Object s2 = Encrypt3.encrypt2(s);
                    System.out.println(" Encrypted : " + s2 + "\n");
                    OutputStream os = socket.getOutputStream();
                    DataOutputStream dOut = new DataOutputStream(os);
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    os.flush();
                    dOut.flush();
                    DataInputStream dIn = new DataInputStream(socket.getInputStream());
                    int length = dIn.readInt();
                    if (length > 0 && length < 204800) {
                        byte[] data = new byte[length];
                        dIn.readFully(data, 0, data.length);
                        message = Encrypt3.decrypt2(data);
                        System.out.println(" ---------  PRE JSON  CHECKPOINT ----------------------- ");
                        GetSlots.this.json2db(message);
                        System.out.println(" ---------  POST JSON  CHECKPOINT ----------------------- ");
                        JSONObject json2 = new JSONObject(message);
                        json2.put("username", this.username2);
                        json2.put("password", this.password2);
                        String js = json2.get("status").toString();
                        GetSlots.this.id = Integer.parseInt(js);
                    }
                    socket.close();
                    socket2 = socket;
                } catch (Exception e) {
                    socket2 = socket;
                }
            } catch (Exception e2) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            GetSlots.this.mProgressBar.setVisibility(4);
            GetSlots.this.upattend.setText("Edit Attendance");
            GetSlots.this.b2.setText("Unmarked Slots");
            GetSlots.this.bsync.setEnabled(true);
            GetSlots.this.b2.setEnabled(true);
            GetSlots.this.blogout.setEnabled(true);
            GetSlots.this.upattend.setEnabled(true);
            if (GetSlots.this.id == 1) {
                try {
                    GetSlots.this.offlinepopulate(this.username2, this.password2);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            new Builder(GetSlots.this).setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
            GetSlots.this.mProgressBar.setVisibility(4);
            GetSlots.this.bsync.setEnabled(true);
            System.out.println("FAIL");
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
                this.username2 = GetSlots.this.json2.get("username").toString();
                this.password2 = GetSlots.this.json2.get("password").toString();
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
                    json.put("slot_id", GetSlots.this.f21s);
                    byte[] s2 = Encrypt3.encrypt2(json.toString() + "\n");
                    DataOutputStream dOut = new DataOutputStream(socket2.getOutputStream());
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream is = new DataInputStream(socket2.getInputStream());
                    int length = is.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0) {
                        is.readFully(messages, 0, messages.length);
                    }
                    GetSlots.this.mes = Encrypt3.decrypt2(messages);
                    JSONObject json4 = new JSONObject(GetSlots.this.mes);
                    String tab = json4.get("table").toString();
                    String subject = json4.get(downloaddata.COL_2).toString();
                    String batch = json4.get(downloaddata.COL_4).toString();
                    String time = json4.get("time").toString();
                    String js = json4.get("status").toString();
                    GetSlots.this.json2.put("time", time);
                    GetSlots.this.json2.put(downloaddata.COL_4, batch);
                    GetSlots.this.json2.put(downloaddata.COL_2, subject);
                    GetSlots.this.json2.put("tab", tab);
                    GetSlots.this.fin = GetSlots.this.json2.toString();
                    this.id = Integer.parseInt(js);
                    socket = socket2;
                } catch (Exception e2) {
                    e = e2;
                    socket = socket2;
                    GetSlots.this.fin = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                GetSlots.this.fin = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int row = 0; row < GetSlots.this.slots2; row++) {
                GetSlots.this.f20b[row].setEnabled(true);
            }
            GetSlots.this.mProgressBar.setVisibility(4);
            GetSlots.this.upattend.setText("Edit Attendance");
            GetSlots.this.b2.setText("Unmarked Slots");
            if (this.id == 1) {
                Intent intent = new Intent(GetSlots.this, insidelogin_test.class);
                intent.putExtra("message", GetSlots.this.fin);
                GetSlots.this.finish();
                GetSlots.this.startActivity(intent);
                return;
            }
            new Builder(GetSlots.this).setMessage("Connection Prob").setNegativeButton("Retry", null).create().show();
        }
    }

    public class unmarked_slot_request extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 17072;
        int id;
        String messsage;
        String name2;
        String password2;
        TextView status2;
        String username2;

        unmarked_slot_request() {
            try {
                this.username2 = GetSlots.this.json2.get("username").toString();
                this.password2 = GetSlots.this.json2.get("password").toString();
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
                    this.messsage = Encrypt3.decrypt2(messages);
                    JSONObject json2 = new JSONObject(this.messsage);
                    json2.put("username", this.username2);
                    json2.put("password", this.password2);
                    String js = json2.get("status").toString();
                    GetSlots.this.fin = json2.toString();
                    this.id = Integer.parseInt(js);
                    socket = socket2;
                } catch (Exception e2) {
                    e = e2;
                    socket = socket2;
                    this.messsage = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                this.messsage = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int row = 0; row < GetSlots.this.slots2; row++) {
                GetSlots.this.f20b[row].setEnabled(true);
            }
            GetSlots.this.mProgressBar.setVisibility(4);
            GetSlots.this.upattend.setText("Edit Attendance");
            GetSlots.this.b2.setText("Unmarked Slots");
            if (this.id == 0) {
                new Builder(GetSlots.this).setMessage("Connection Prob").setNegativeButton("Retry", null).create().show();
                return;
            }
            Intent intent = new Intent(GetSlots.this, Unmarked_slots.class);
            intent.putExtra("message", GetSlots.this.fin);
            GetSlots.this.finish();
            GetSlots.this.startActivity(intent);
        }
    }

    public class updaterequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 19900;
        int id;
        String messsage;
        String name2;
        String password2;
        TextView status2;
        String username2;

        updaterequest() {
            try {
                this.username2 = GetSlots.this.json2.get("username").toString();
                this.password2 = GetSlots.this.json2.get("password").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    byte[] s2 = Encrypt3.encrypt2(json.toString() + "\n");
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    int length = is.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0 && length < 102400) {
                        is.readFully(messages, 0, messages.length);
                    }
                    this.messsage = Encrypt3.decrypt2(messages);
                    JSONObject json2 = new JSONObject(this.messsage);
                    json2.put("username", this.username2);
                    json2.put("password", this.password2);
                    String js = json2.get("status").toString();
                    GetSlots.this.fin = json2.toString();
                    this.id = Integer.parseInt(js);
                    socket2 = socket;
                } catch (Exception e2) {
                    e = e2;
                    socket2 = socket;
                    this.messsage = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                this.messsage = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int row = 0; row < GetSlots.this.slots2; row++) {
                GetSlots.this.f20b[row].setEnabled(true);
            }
            GetSlots.this.mProgressBar.setVisibility(4);
            GetSlots.this.upattend.setText("Edit Attendance");
            GetSlots.this.b2.setText("Unmarked Slots");
            if (this.id == 0) {
                new Builder(GetSlots.this).setMessage("Connection Prob").setNegativeButton("Retry", null).create().show();
                return;
            }
            Intent intent = new Intent(GetSlots.this, EditAttendance.class);
            intent.putExtra("message", GetSlots.this.fin);
            GetSlots.this.finish();
            GetSlots.this.startActivity(intent);
        }
    }

    public void onClick(View view) {
        this.mProgressBar.setVisibility(0);
        this.upattend.setText(" Loading ");
        this.b2.setText(" Loading ");
        this.f21s = view.getId();
        for (int row = 0; row < this.slots2; row++) {
            this.f20b[row].setEnabled(false);
        }
        try {
            this.json2.put("slot_id", this.f21s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.conMgr = (ConnectivityManager) getApplicationContext().getSystemService("connectivity");
        this.activeNetwork = this.conMgr.getActiveNetworkInfo();
        try {
            offlinepopulatestudents(this.f21s);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    private void offlinepopulatestudents(int s) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        Cursor res = this.myDb2.getAllDatajoinedname(s);
        if (res.getCount() != 0) {
            String col1 = "slot_id";
            String col2 = "student_id";
            String col3 = downloaddata.COL_6;
            while (res.moveToNext()) {
                JSONObject j = new JSONObject();
                String studentid = res.getString(0);
                String name = res.getString(1);
                j.put(col2, studentid);
                j.put(col3, name);
                jsonArray.put(j);
            }
            String batch = null;
            String time = null;
            String subject = null;
            Cursor res2 = this.myDb2.getAllDataspecificslot(s);
            while (res2.moveToNext()) {
                subject = res2.getString(1);
                time = res2.getString(2);
                batch = res2.getString(3);
            }
            String username2 = this.json2.get("username").toString();
            String password2 = this.json2.get("password").toString();
            JSONObject j2 = new JSONObject();
            j2.put("username", username2);
            j2.put("password", password2);
            j2.put(downloaddata.COL_4, batch);
            j2.put("time", time);
            j2.put(downloaddata.COL_2, subject);
            j2.put("slot_id", s);
            j2.put("tab", jsonArray);
            this.fin = j2.toString();
            Intent intent = new Intent(this, insidelogin_test.class);
            intent.putExtra("message", this.fin);
            finish();
            startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0231R.layout.activity_get_slots);
        String mess = getIntent().getExtras().getString("message");
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        this.mProgressBar.setVisibility(8);
        this.myDb2 = new downloaddata(this);
        this.myDb3 = new downloaddata(this);
        this.blogout = (Button) findViewById(C0231R.id.blogout);
        this.bsync = (Button) findViewById(C0231R.id.bsync);
        this.bsync.setOnClickListener(new C02271());
        this.blogout.setOnClickListener(new C02282());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 13;
        this.upattend = (Button) findViewById(C0231R.id.upattend);
        this.upattend.setOnClickListener(new C02293());
        this.b2 = (Button) findViewById(C0231R.id.b2);
        this.b2.setOnClickListener(new C02304());
        this.upattend.setWidth(width / 2);
        this.b2.setWidth(width / 2);
        TableLayout layout = (TableLayout) findViewById(C0231R.id.tableforSlots);
        try {
            this.json2 = new JSONObject(mess);
            String message = this.json2.get("table").toString();
            this.myDb = new getsyncdata(this, this.json2.get("username").toString(), this.json2.get("password").toString());
            JSONArray jarray = new JSONArray(message);
            int slots = jarray.length();
            this.slots2 = slots;
            TextView[] t = new TextView[slots];
            this.f20b = new Button[slots];
            for (int row = 0; row < slots; row++) {
                View linearLayout = new LinearLayout(this);
                JSONObject js = new JSONObject(jarray.get(row).toString());
                t[row] = new TextView(this);
                t[row].setWidth(width);
                t[row].setHeight(height / 25);
                t[row].setText(js.get(downloaddata.COL_3) + " ");
                t[row].setTextColor(this.whiteInt);
                t[row].setBackgroundColor(this.bgint);
                t[row].setGravity(17);
                this.f20b[row] = new Button(this);
                this.f20b[row].setWidth(width);
                this.f20b[row].setHeight(height / 8);
                this.f20b[row].setTextColor(this.btint);
                this.f20b[row].setBackgroundColor(this.whiteInt);
                this.f20b[row].setText(js.get(downloaddata.COL_4) + "  | " + js.get(downloaddata.COL_2).toString());
                this.f20b[row].setGravity(17);
                this.f20b[row].setId(Integer.parseInt(js.get("slot_id").toString()));
                this.f20b[row].setOnClickListener(this);
                linearLayout.addView(this.f20b[row]);
                layout.addView(t[row]);
                layout.addView(linearLayout);
            }
        } catch (Exception e) {
        }
    }

    private String dataforedit() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jfinal = new JSONObject();
        Cursor res = this.myDb2.getattendance();
        while (res.moveToNext()) {
            JSONObject jsend = new JSONObject();
            String st2 = "";
            String slot_id = "";
            String subject = "";
            String time = "";
            String batch = "";
            slot_id = res.getString(0);
            st2 = res.getString(2);
            System.out.println("st2 " + st2);
            try {
                JSONObject jedit = new JSONObject(st2.toString());
                subject = jedit.get(downloaddata.COL_2).toString();
                time = jedit.get(downloaddata.COL_3).toString();
                batch = jedit.get(downloaddata.COL_4).toString();
            } catch (Exception e) {
                System.out.println("Error in stringedit " + e);
            }
            jsend.put("slot_id", slot_id);
            jsend.put(downloaddata.COL_2, subject);
            jsend.put(downloaddata.COL_3, time);
            jsend.put(downloaddata.COL_4, batch);
            jsonArray.put(jsend);
        }
        String username = this.json2.get("username").toString();
        String password = this.json2.get("password").toString();
        jfinal.put("table", jsonArray);
        jfinal.put("username", username);
        jfinal.put("password", password);
        String pop = jfinal.toString();
        System.out.println("String in func " + pop);
        return pop;
    }

    private void offlinepopulate(String username, String password) throws JSONException {
        String col1;
        String col2;
        String col3;
        String col4;
        JSONArray jsonArray = new JSONArray();
        Cursor res = this.myDb3.getAllData();
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
        String fin = j2.toString();
        Intent intent = new Intent(this, GetSlots.class);
        intent.putExtra("message", fin);
        finish();
        startActivity(intent);
    }

    public void json2db(String json_data) {
        System.out.println("JSON DATA SYNCING IS : " + json_data);
        try {
            downloaddata dbin = new downloaddata(this);
            JSONObject j = new JSONObject(json_data);
            if (j.get("status").toString().equals("1")) {
                int i;
                this.myDb3.truncatestudent();
                this.myDb3.truncateslotstudent();
                this.myDb3.truncateslots();
                JSONArray slots = new JSONArray(j.get(downloaddata.TABLE_NAME1).toString());
                for (i = 0; i < slots.length(); i++) {
                    JSONObject slot_i = new JSONObject(slots.get(i).toString());
                    Boolean t = Boolean.valueOf(dbin.insertslots(slot_i.get("slot_id").toString(), slot_i.get(downloaddata.COL_2).toString(), slot_i.get(downloaddata.COL_3).toString(), slot_i.get(downloaddata.COL_4).toString()));
                }
                JSONArray slot_student = new JSONArray(j.get(downloaddata.TABLE_NAME3).toString());
                for (i = 0; i < slot_student.length(); i++) {
                    JSONObject slot_student_i = new JSONObject(slot_student.get(i).toString());
                    Boolean t2 = Boolean.valueOf(dbin.insertslotid(slot_student_i.get("slot_id").toString(), slot_student_i.get("student_id").toString()));
                }
                JSONArray student = new JSONArray(j.get(downloaddata.TABLE_NAME2).toString());
                for (i = 0; i < student.length(); i++) {
                    JSONObject student_i = new JSONObject(student.get(i).toString());
                    Boolean t3 = Boolean.valueOf(dbin.insertstudent(student_i.get("student_id").toString(), student_i.get(downloaddata.COL_6).toString()));
                }
            }
        } catch (Exception e) {
        }
    }
}
