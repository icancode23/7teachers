package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class insidelogin_test extends AppCompatActivity {
    private static final int pstatus = 101;
    TextView absence;
    NetworkInfo activeNetwork;
    BluetoothAdapter adapter;
    String ba = "";
    Button bmarkpres;
    Button brfrsh;
    ConnectivityManager conMgr;
    int count;
    EditText etsearch;
    String fin2 = "";
    int flagbt;
    String forrol;
    Button go;
    JSONObject json11;
    JSONObject json12;
    JSONObject json2;
    JSONObject json3;
    JSONArray jsonArray_student;
    JSONObject jsoncheck;
    JSONObject jsonlog;
    JSONObject f28k;
    JSONObject kk;
    ProgressBar mProgressBar;
    private final BroadcastReceiver mReceiver = new C02485();
    ArrayList<String> mac;
    String message;
    final String mrk = "MARK";
    JSONArray myArray3 = null;
    JSONArray myArray4 = null;
    downloaddata myDb;
    String name = "";
    String password = "";
    String password2;
    private boolean permissionisgranted = true;
    TextView presence;
    int presencelog = 1;
    RadioButton[] rdbtn3;
    Button response;
    RadioGroup[] rg;
    Button scan;
    String sedit = "";
    String slist = "";
    String slot_id = "";
    Socket socketlog;
    String ss;
    String student_idcheck;
    int students = 0;
    String sub = "";
    ScrollView sv;
    TableLayout table;
    TableRow tablerow;
    String tests = "";
    String ti = "";
    String username = "";
    String username2;
    int f29x;
    int f30z = 0;

    class C02431 implements OnClickListener {
        C02431() {
        }

        public void onClick(View v) {
            try {
                insidelogin_test.this.myArray4 = new JSONArray();
                Sort(insidelogin_test.this.myArray4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void Sort(JSONArray myArray4) throws JSONException {
            int i;
            int x2 = 0;
            for (i = 0; i < insidelogin_test.this.myArray3.length(); i++) {
                if (((RadioButton) insidelogin_test.this.rg[i].getChildAt(3)).isChecked()) {
                    insidelogin_test.this.kk = new JSONObject();
                    JSONObject json4 = new JSONObject(insidelogin_test.this.myArray3.get(i).toString());
                    String forrol2 = json4.get("student_id").toString();
                    String forname = json4.get(downloaddata.COL_6).toString();
                    insidelogin_test.this.kk.put("student_id", forrol2);
                    insidelogin_test.this.kk.put(downloaddata.COL_6, forname);
                    myArray4.put(insidelogin_test.this.kk);
                    x2 = myArray4.length();
                }
            }
            for (i = 0; i < insidelogin_test.this.myArray3.length(); i++) {
                if (!((RadioButton) insidelogin_test.this.rg[i].getChildAt(3)).isChecked()) {
                    insidelogin_test.this.kk = new JSONObject();
                    json4 = new JSONObject(insidelogin_test.this.myArray3.get(i).toString());
                    forrol2 = json4.get("student_id").toString();
                    forname = json4.get(downloaddata.COL_6).toString();
                    insidelogin_test.this.kk.put("student_id", forrol2);
                    insidelogin_test.this.kk.put(downloaddata.COL_6, forname);
                    myArray4.put(insidelogin_test.this.kk);
                }
            }
            insidelogin_test.this.myArray3 = myArray4;
            insidelogin_test.this.populate(myArray4.length(), myArray4, x2);
        }
    }

    class C02442 implements OnClickListener {
        C02442() {
        }

        public void onClick(View v) {
            try {
                new resetrequest().execute(new Void[0]);
            } catch (Exception e) {
            }
        }
    }

    class C02453 implements OnClickListener {
        C02453() {
        }

        public void onClick(View v) {
            insidelogin_test.this.mac = new ArrayList();
            if (insidelogin_test.this.permissionisgranted) {
                if (insidelogin_test.this.f30z % 2 == 0) {
                    insidelogin_test.this.f30z++;
                    insidelogin_test.this.scan.setText("Scanning");
                    insidelogin_test.this.jsonArray_student = new JSONArray();
                } else {
                    insidelogin_test.this.f30z++;
                    Toast.makeText(insidelogin_test.this.getApplicationContext(), "Scan Process Turned Off", 1).show();
                    insidelogin_test.this.adapter.cancelDiscovery();
                    insidelogin_test.this.scan.setText("scan off");
                }
                if (!insidelogin_test.this.adapter.isEnabled()) {
                    insidelogin_test.this.f30z = 0;
                    insidelogin_test.this.scan.setText("Turn BT On");
                }
                if (insidelogin_test.this.f30z == 1) {
                    for (int z2 = 0; z2 < insidelogin_test.this.students; z2++) {
                        ((RadioButton) insidelogin_test.this.rg[z2].getChildAt(3)).setChecked(true);
                    }
                }
                if (insidelogin_test.this.f30z % 2 != 0) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("android.bluetooth.device.action.FOUND");
                    filter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
                    filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
                    insidelogin_test.this.registerReceiver(insidelogin_test.this.mReceiver, filter);
                    insidelogin_test.this.adapter.startDiscovery();
                }
            }
        }
    }

    class C02474 implements OnClickListener {
        C02474() {
        }

        public void onClick(View v) {
            int flag = 0;
            int found = 0;
            String searching = "";
            searching = insidelogin_test.this.etsearch.getText().toString();
            String forrollcheck = "";
            for (int search = 0; search < insidelogin_test.this.f29x; search++) {
                try {
                    insidelogin_test.this.jsoncheck = new JSONObject(insidelogin_test.this.myArray3.get(search).toString());
                    forrollcheck = insidelogin_test.this.jsoncheck.get("student_id").toString();
                } catch (Exception e) {
                    insidelogin_test.this.etsearch.setText("insidecatch");
                }
                if (forrollcheck.contains(searching)) {
                    found = search;
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                insidelogin_test.this.rg[found].getChildAt(0).setBackgroundColor(Color.parseColor("#ffff00"));
                final View child = insidelogin_test.this.table.getChildAt(found);
                new Handler().post(new Runnable() {
                    public void run() {
                        insidelogin_test.this.sv.smoothScrollTo(0, child.getTop());
                    }
                });
                return;
            }
            Toast.makeText(insidelogin_test.this.getApplicationContext(), "R.No. Not Found", 0).show();
        }
    }

    class C02485 extends BroadcastReceiver {
        C02485() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.adapter.action.DISCOVERY_STARTED".equals(action)) {
                Toast.makeText(insidelogin_test.this.getApplicationContext(), "Scan Process Running", 1).show();
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                new resetrequest().execute(new Void[0]);
                if (insidelogin_test.this.f30z % 2 != 0) {
                    insidelogin_test.this.adapter.startDiscovery();
                }
            } else if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                String s = device.getName();
                String dev_addr = device.getAddress() + "";
                insidelogin_test.this.f28k = new JSONObject();
                for (int z2 = 0; z2 < insidelogin_test.this.students; z2++) {
                    if (((TextView) insidelogin_test.this.rg[z2].getChildAt(0)).getText().equals(s)) {
                        int flag = 1;
                        for (int mac_i = 0; mac_i < insidelogin_test.this.mac.size(); mac_i++) {
                            if (((String) insidelogin_test.this.mac.get(mac_i)).equals(dev_addr)) {
                                flag = 0;
                                break;
                            }
                        }
                        if (flag == 1) {
                            ((RadioButton) insidelogin_test.this.rg[z2].getChildAt(2)).setChecked(true);
                            insidelogin_test.this.mac.add(dev_addr);
                            try {
                                insidelogin_test.this.f28k.put("roll", s);
                                insidelogin_test.this.jsonArray_student.put(insidelogin_test.this.f28k);
                            } catch (JSONException e) {
                            }
                            new datarequest(s, dev_addr).execute(new Void[0]);
                        }
                    }
                }
            }
        }
    }

    class CheckedChangeListener implements OnCheckedChangeListener {
        CheckedChangeListener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            insidelogin_test com_AttendanceSimplified_vaibhav_dtuattendance_insidelogin_test;
            if (isChecked) {
                com_AttendanceSimplified_vaibhav_dtuattendance_insidelogin_test = insidelogin_test.this;
                com_AttendanceSimplified_vaibhav_dtuattendance_insidelogin_test.count++;
            } else {
                com_AttendanceSimplified_vaibhav_dtuattendance_insidelogin_test = insidelogin_test.this;
                com_AttendanceSimplified_vaibhav_dtuattendance_insidelogin_test.count--;
            }
            insidelogin_test.this.absence.setText("A :" + insidelogin_test.this.count);
            insidelogin_test.this.presence.setText("P :" + (insidelogin_test.this.f29x - insidelogin_test.this.count));
        }
    }

    public class attendancerequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 16060;
        String fin = "";
        int id;
        JSONObject json22;
        String message;

        attendancerequest() {
            try {
                insidelogin_test.this.username2 = insidelogin_test.this.json2.get("username").toString();
                insidelogin_test.this.password2 = insidelogin_test.this.json2.get("password").toString();
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
                    byte[] s2 = Encrypt3.encrypt2(insidelogin_test.this.ss);
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    int length = is.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0 && length < 102400) {
                        is.readFully(messages, 0, messages.length);
                    }
                    this.message = Encrypt3.decrypt2(messages);
                    this.json22 = new JSONObject(this.message);
                    this.id = Integer.parseInt(this.json22.get("Status").toString());
                    insidelogin_test.this.json3 = new JSONObject();
                    insidelogin_test.this.json3.put("username", insidelogin_test.this.username2);
                    insidelogin_test.this.json3.put("password", insidelogin_test.this.password2);
                    this.fin = insidelogin_test.this.json3.toString();
                    socket.close();
                    socket2 = socket;
                } catch (Exception e2) {
                    e = e2;
                    socket2 = socket;
                    this.fin = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                this.fin = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            insidelogin_test.this.mProgressBar.setVisibility(4);
            insidelogin_test.this.response.setText("MARK");
            if (this.id == 1) {
                insidelogin_test.this.adapter.cancelDiscovery();
                insidelogin_test.this.adapter.disable();
                Intent intent = new Intent(insidelogin_test.this, Done.class);
                intent.putExtra("message", this.fin);
                insidelogin_test.this.finish();
                insidelogin_test.this.startActivity(intent);
                return;
            }
            new Builder(insidelogin_test.this).setMessage("Error in Connection").setNegativeButton("Retry", null).create().show();
        }
    }

    public class datarequest extends AsyncTask<Void, Void, Void> {
        String address;
        int logport = 19099;
        String student_id;
        String testServerNamelog = dbcred.dstAddress1;

        datarequest(String s, String dev_add) {
            this.student_id = s;
            this.address = dev_add;
        }

        protected Void doInBackground(Void... arg0) {
            try {
                insidelogin_test.this.socketlog = new Socket(this.testServerNamelog, this.logport);
                PrintStream printStream = new PrintStream(insidelogin_test.this.socketlog.getOutputStream());
                insidelogin_test.this.jsonlog = new JSONObject();
                insidelogin_test.this.jsonlog.put("username", insidelogin_test.this.username);
                insidelogin_test.this.jsonlog.put("password", insidelogin_test.this.password);
                insidelogin_test.this.jsonlog.put("student_id", this.student_id);
                insidelogin_test.this.jsonlog.put("MAC", this.address);
                insidelogin_test.this.jsonlog.put("slot_id", insidelogin_test.this.slot_id + "");
                insidelogin_test.this.jsonlog.put("presence", insidelogin_test.this.presencelog + "");
                byte[] s2 = Encrypt3.encrypt2(insidelogin_test.this.jsonlog.toString());
                DataOutputStream dOut = new DataOutputStream(insidelogin_test.this.socketlog.getOutputStream());
                dOut.writeInt(s2.length);
                dOut.write(s2);
                insidelogin_test.this.socketlog.close();
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
        }
    }

    public class resetrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = "54.149.115.243";
        int dstPort = 10008;
        int id;
        String list;
        String message;
        String name2;
        TextView status2;

        resetrequest() {
        }

        protected Void doInBackground(Void... arg0) {
            try {
                Socket socket = new Socket(this.dstAddress, this.dstPort);
                Socket socket2;
                try {
                    JSONObject json = new JSONObject();
                    json.put("studentlist", insidelogin_test.this.jsonArray_student);
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
                    insidelogin_test.this.slist = json2.getString("table").toString();
                    socket2 = socket;
                } catch (Exception e) {
                    socket2 = socket;
                }
            } catch (Exception e2) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                insidelogin_test.this.MarkPresent(insidelogin_test.this.slist);
            } catch (JSONException e) {
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0231R.layout.activity_insidelogin_test);
        this.message = getIntent().getExtras().getString("message");
        this.myDb = new downloaddata(this);
        this.flagbt = 0;
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.brfrsh = (Button) findViewById(C0231R.id.badv);
        this.bmarkpres = (Button) findViewById(C0231R.id.bmarkpres);
        this.bmarkpres.setOnClickListener(new C02431());
        this.brfrsh.setOnClickListener(new C02442());
        this.sv = (ScrollView) findViewById(C0231R.id.scrollView2);
        this.count = 0;
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        this.mProgressBar.setVisibility(8);
        TextView tvb = (TextView) findViewById(C0231R.id.tvb);
        TextView tvt = (TextView) findViewById(C0231R.id.tvtime);
        TextView subj = (TextView) findViewById(C0231R.id.subject);
        this.absence = (TextView) findViewById(C0231R.id.absence);
        this.presence = (TextView) findViewById(C0231R.id.presence);
        this.scan = (Button) findViewById(C0231R.id.scan);
        this.go = (Button) findViewById(C0231R.id.bsearch);
        this.etsearch = (EditText) findViewById(C0231R.id.etsearch);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 12;
        this.absence.setText("A :0");
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            if (VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 101);
            } else {
                this.permissionisgranted = true;
            }
        }
        this.scan.setOnClickListener(new C02453());
        try {
            this.json2 = new JSONObject(this.message);
            String js = this.json2.get("tab").toString();
            this.ba = this.json2.get(downloaddata.COL_4).toString();
            this.ti = this.json2.get("time").toString();
            this.sub = this.json2.get(downloaddata.COL_2).toString();
            this.slot_id = this.json2.get("slot_id").toString();
            this.username = this.json2.get("username").toString();
            this.password = this.json2.get("password").toString();
            tvb.setText(this.ba);
            tvt.setText(this.ti);
            subj.setText(this.sub);
            this.myArray3 = new JSONArray(js);
            this.f29x = this.myArray3.length();
            this.presence.setText("P :" + this.f29x);
            this.go.setOnClickListener(new C02474());
            populate(this.f29x, this.myArray3, 0);
        } catch (Exception e) {
        }
    }

    void MarkPresent(String slist) throws JSONException {
        JSONArray json_slist = new JSONArray(slist);
        for (int i = 0; i < json_slist.length(); i++) {
            this.student_idcheck = "";
            this.student_idcheck = json_slist.getJSONObject(i).get("roll_scanned").toString();
            for (int z2 = 0; z2 < this.students; z2++) {
                if (((TextView) this.rg[z2].getChildAt(0)).getText().equals(this.student_idcheck)) {
                    ((RadioButton) this.rg[z2].getChildAt(3)).setChecked(false);
                    ((RadioButton) this.rg[z2].getChildAt(2)).setChecked(true);
                    break;
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults[0] == 0) {
                    this.permissionisgranted = true;
                    return;
                } else {
                    this.permissionisgranted = false;
                    return;
                }
            default:
                return;
        }
    }

    private void populate(int x, JSONArray myArray3, int x2) throws JSONException {
        int z;
        this.students = x;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        this.rg = new RadioGroup[x];
        for (z = 0; z < x; z++) {
            this.rg[z] = new RadioGroup(this);
        }
        this.table = (TableLayout) findViewById(C0231R.id.tableforButtons);
        this.rdbtn3 = new RadioButton[x];
        int count = this.table.getChildCount();
        for (z = 0; z < count; z++) {
            View child = this.table.getChildAt(z);
            if (child instanceof TableRow) {
                ((ViewGroup) child).removeAllViews();
            }
        }
        for (int i = 0; i < x; i++) {
            this.tablerow = new TableRow(this);
            this.tablerow.setLayoutParams(new LayoutParams(-1, -1, 1.0f));
            this.table.addView(this.tablerow);
            this.rg[i].setOrientation(0);
            TextView roll = new TextView(this);
            roll.setWidth(width / 3);
            roll.setGravity(17);
            this.rg[i].addView(roll);
            TextView name = new TextView(this);
            name.setWidth(width / 3);
            this.rg[i].addView(name);
            RadioButton rdbtn2 = new RadioButton(this);
            rdbtn2.setWidth((int) (((double) width) / 6.5d));
            this.rg[i].addView(rdbtn2);
            rdbtn2.setChecked(true);
            this.rdbtn3[i] = new RadioButton(this);
            this.rdbtn3[i].setWidth((int) (((double) width) / 6.5d));
            this.rg[i].addView(this.rdbtn3[i]);
            if (i < x2) {
                this.rdbtn3[i].setChecked(true);
            } else {
                rdbtn2.setChecked(true);
            }
            this.rdbtn3[i].setOnCheckedChangeListener(new CheckedChangeListener());
            this.tablerow.addView(this.rg[i]);
        }
        int row = 0;
        JSONObject json3 = null;
        while (row < x) {
            JSONObject json32;
            TextView setroll = (TextView) this.rg[row].getChildAt(0);
            try {
                json32 = new JSONObject(myArray3.get(row).toString());
            } catch (JSONException e) {
                e.printStackTrace();
                json32 = json3;
            }
            setroll.setText(json32.get("student_id").toString());
            setroll.setTextSize(2, 12.0f);
            TextView setname = (TextView) this.rg[row].getChildAt(1);
            setname.setText(json32.get(downloaddata.COL_6).toString());
            setname.setTextSize(2, 12.0f);
            ((RadioButton) this.rg[row].getChildAt(2)).setText("P");
            ((RadioButton) this.rg[row].getChildAt(3)).setText("A");
            row++;
            json3 = json32;
        }
        responsive(myArray3, this.rg, x);
    }

    private void responsive(final JSONArray myArray3, final RadioGroup[] rg, final int x) {
        this.response = (Button) findViewById(C0231R.id.response);
        this.response.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                insidelogin_test.this.response.setText("Loading");
                insidelogin_test.this.mProgressBar.setVisibility(0);
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArrayedit = new JSONArray();
                insidelogin_test.this.json11 = new JSONObject();
                insidelogin_test.this.json12 = new JSONObject();
                final int slot = Integer.valueOf(insidelogin_test.this.slot_id).intValue();
                for (int row = 0; row < x; row++) {
                    insidelogin_test.this.name = "";
                    JSONObject jason;
                    JSONObject jasonedit;
                    JSONObject json4;
                    if (((RadioButton) rg[row].getChildAt(3)).isChecked()) {
                        jason = new JSONObject();
                        jasonedit = new JSONObject();
                        try {
                            json4 = new JSONObject(myArray3.get(row).toString());
                            insidelogin_test.this.forrol = json4.get("student_id").toString();
                            insidelogin_test.this.name = json4.get(downloaddata.COL_6).toString();
                            jason.put("roll", insidelogin_test.this.forrol);
                            jason.put("presence", 0);
                            jasonedit.put("student_id", insidelogin_test.this.forrol);
                            jasonedit.put("presence", 0);
                            jasonedit.put(downloaddata.COL_6, insidelogin_test.this.name);
                            jsonArray.put(jason);
                            jsonArrayedit.put(jasonedit);
                        } catch (Exception e) {
                        }
                    } else {
                        jason = new JSONObject();
                        jasonedit = new JSONObject();
                        try {
                            json4 = new JSONObject(myArray3.get(row).toString());
                            insidelogin_test.this.forrol = json4.get("student_id").toString();
                            insidelogin_test.this.name = json4.get(downloaddata.COL_6).toString();
                            jason.put("roll", insidelogin_test.this.forrol);
                            jason.put("presence", 1);
                            jason.put("MAC", "--");
                            jasonedit.put("student_id", insidelogin_test.this.forrol);
                            jasonedit.put("presence", 1);
                            jasonedit.put(downloaddata.COL_6, insidelogin_test.this.name);
                            jsonArray.put(jason);
                            jsonArrayedit.put(jasonedit);
                        } catch (Exception e2) {
                        }
                    }
                }
                try {
                    insidelogin_test.this.json11.put(downloaddata.TABLE_NAME4, jsonArray.toString());
                    insidelogin_test.this.json11.put("username", insidelogin_test.this.username);
                    insidelogin_test.this.json11.put("password", insidelogin_test.this.password);
                    insidelogin_test.this.json11.put("slot_id", slot);
                    insidelogin_test.this.json12.put("username", insidelogin_test.this.username);
                    insidelogin_test.this.json12.put("password", insidelogin_test.this.password);
                    insidelogin_test.this.json12.put(downloaddata.COL_4, insidelogin_test.this.ba);
                    insidelogin_test.this.json12.put(downloaddata.COL_3, insidelogin_test.this.ti);
                    insidelogin_test.this.json12.put(downloaddata.COL_2, insidelogin_test.this.sub);
                    insidelogin_test.this.json12.put("slot_id", slot);
                    insidelogin_test.this.json12.put("editattendance", jsonArrayedit.toString());
                    insidelogin_test.this.ss = insidelogin_test.this.json11.toString();
                    insidelogin_test.this.sedit = insidelogin_test.this.json12.toString();
                    insidelogin_test.this.socketlog.close();
                } catch (Exception e3) {
                }
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -2:
                                insidelogin_test.this.mProgressBar.setVisibility(4);
                                insidelogin_test.this.response.setText("MARK");
                                return;
                            case -1:
                                insidelogin_test.this.myDb.deleteattendance(slot);
                                insidelogin_test.this.myDb.insertattendance(slot, insidelogin_test.this.ss, insidelogin_test.this.sedit);
                                insidelogin_test.this.myDb.deleteslot(slot);
                                insidelogin_test.this.json3 = new JSONObject();
                                try {
                                    insidelogin_test.this.username2 = insidelogin_test.this.json2.get("username").toString();
                                    insidelogin_test.this.password2 = insidelogin_test.this.json2.get("password").toString();
                                    insidelogin_test.this.json3.put("username", insidelogin_test.this.username2);
                                    insidelogin_test.this.json3.put("password", insidelogin_test.this.password2);
                                    insidelogin_test.this.fin2 = insidelogin_test.this.json3.toString();
                                    insidelogin_test.this.flagbt = 1;
                                    insidelogin_test.this.adapter.cancelDiscovery();
                                    insidelogin_test.this.adapter.disable();
                                    Intent intent = new Intent(insidelogin_test.this, Done.class);
                                    intent.putExtra("message", insidelogin_test.this.fin2);
                                    insidelogin_test.this.finish();
                                    insidelogin_test.this.startActivity(intent);
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                };
                new Builder(insidelogin_test.this).setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
}
