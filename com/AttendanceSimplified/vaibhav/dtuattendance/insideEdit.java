package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
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
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class insideEdit extends AppCompatActivity {
    TextView absm;
    NetworkInfo activeNetwork;
    String batch;
    ConnectivityManager conMgr;
    int count;
    EditText etsearch;
    String forrol;
    Button go;
    JSONObject json11;
    JSONObject json12;
    JSONObject json2;
    JSONObject json3;
    JSONObject jsoncheck;
    ProgressBar mProgressBar;
    String message;
    JSONArray myArray3 = null;
    downloaddata myDb;
    String name = "";
    String password2;
    TextView presence;
    Button response;
    RadioGroup[] rg;
    String slotid;
    String ss;
    String subje;
    ScrollView sv;
    TableLayout table;
    String time;
    TextView tvbatch;
    String username2;
    int f27x;

    class C02411 implements OnClickListener {
        C02411() {
        }

        public void onClick(View v) {
            int flag = 0;
            int found = 0;
            String searching = "";
            searching = insideEdit.this.etsearch.getText().toString();
            String forrollcheck = "";
            for (int search = 0; search < insideEdit.this.f27x; search++) {
                try {
                    insideEdit.this.jsoncheck = new JSONObject(insideEdit.this.myArray3.get(search).toString());
                    forrollcheck = insideEdit.this.jsoncheck.get("student_id").toString();
                } catch (Exception e) {
                    insideEdit.this.etsearch.setText("insidecatch");
                }
                if (searching.equals(forrollcheck)) {
                    found = search;
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                insideEdit.this.rg[found].getChildAt(0).setBackgroundColor(Color.parseColor("#ffff00"));
                final View child = insideEdit.this.table.getChildAt(found);
                new Handler().post(new Runnable() {
                    public void run() {
                        insideEdit.this.sv.smoothScrollTo(0, child.getTop());
                    }
                });
                return;
            }
            Toast.makeText(insideEdit.this.getApplicationContext(), "R.No. Not Found", 0).show();
        }
    }

    class CheckedChangeListener implements OnCheckedChangeListener {
        CheckedChangeListener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            insideEdit com_AttendanceSimplified_vaibhav_dtuattendance_insideEdit;
            if (isChecked) {
                com_AttendanceSimplified_vaibhav_dtuattendance_insideEdit = insideEdit.this;
                com_AttendanceSimplified_vaibhav_dtuattendance_insideEdit.count++;
            } else {
                com_AttendanceSimplified_vaibhav_dtuattendance_insideEdit = insideEdit.this;
                com_AttendanceSimplified_vaibhav_dtuattendance_insideEdit.count--;
            }
            insideEdit.this.absm.setText("A :" + insideEdit.this.count);
            insideEdit.this.presence.setText("P :" + (insideEdit.this.f27x - insideEdit.this.count));
        }
    }

    public class attendancerequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 16060;
        String fin = "";
        int id;
        JSONObject json22;
        String message;
        String password2;
        String username2;

        attendancerequest() {
            try {
                this.username2 = insideEdit.this.json2.get("username").toString();
                this.password2 = insideEdit.this.json2.get("password").toString();
            } catch (Exception e) {
            }
        }

        protected Void doInBackground(Void... arg0) {
            Exception e;
            try {
                Socket socket = new Socket(this.dstAddress, this.dstPort);
                Socket socket2;
                try {
                    byte[] s2 = Encrypt3.encrypt2(insideEdit.this.ss);
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
                    insideEdit.this.json3 = new JSONObject();
                    insideEdit.this.json3.put("username", this.username2);
                    insideEdit.this.json3.put("password", this.password2);
                    this.fin = insideEdit.this.json3.toString();
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
            insideEdit.this.response.setText("Update");
            insideEdit.this.mProgressBar.setVisibility(4);
            insideEdit.this.mProgressBar.setVisibility(8);
            if (this.id == 1) {
                Intent intent = new Intent(insideEdit.this, Done.class);
                intent.putExtra("message", this.fin);
                insideEdit.this.finish();
                insideEdit.this.startActivity(intent);
                return;
            }
            new Builder(insideEdit.this).setMessage("Error in Connection").setNegativeButton("Retry", null).create().show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0231R.layout.activity_inside_edit);
        this.message = getIntent().getExtras().getString("message");
        this.myDb = new downloaddata(this);
        this.tvbatch = (TextView) findViewById(C0231R.id.tvbatch);
        TextView tvtime = (TextView) findViewById(C0231R.id.tvtime);
        TextView sub = (TextView) findViewById(C0231R.id.subject);
        this.sv = (ScrollView) findViewById(C0231R.id.scrollView2);
        this.go = (Button) findViewById(C0231R.id.bsearch);
        this.etsearch = (EditText) findViewById(C0231R.id.etsearch);
        this.absm = (TextView) findViewById(C0231R.id.absm);
        this.presence = (TextView) findViewById(C0231R.id.presence);
        this.count = 0;
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 12;
        this.mProgressBar.setVisibility(8);
        try {
            this.json2 = new JSONObject(this.message);
            this.myArray3 = new JSONArray(this.json2.get("tab").toString());
            this.f27x = this.myArray3.length();
            this.slotid = this.json2.get("slot_id").toString();
            this.batch = this.json2.get(downloaddata.COL_4).toString();
            this.time = this.json2.get("time").toString();
            this.subje = this.json2.get(downloaddata.COL_2).toString();
            this.tvbatch.setText(this.batch);
            tvtime.setText(this.time);
            sub.setText(this.subje);
            this.slotid = this.json2.get("slot_id").toString();
            this.go.setOnClickListener(new C02411());
            populate(this.f27x, this.myArray3);
        } catch (Exception e) {
            this.tvbatch.setText("ERROR  " + e.toString() + this.message);
        }
    }

    private void populate(int x, JSONArray myArray3) throws JSONException {
        JSONObject json33;
        Exception e;
        JSONObject json3;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        this.rg = new RadioGroup[x];
        for (int z = 0; z < x; z++) {
            this.rg[z] = new RadioGroup(this);
        }
        this.table = (TableLayout) findViewById(C0231R.id.tableforButtons);
        RadioButton[] rdbtn3 = new RadioButton[x];
        int i = 0;
        JSONObject json332 = null;
        while (i < x) {
            View tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(-1, -1, 1.0f));
            this.table.addView(tableRow);
            this.rg[i].setOrientation(0);
            tableRow = new TextView(this);
            tableRow.setWidth(width / 4);
            tableRow.setGravity(17);
            this.rg[i].addView(tableRow);
            TextView name = new TextView(this);
            name.setWidth(width / 3);
            this.rg[i].addView(name);
            try {
                json33 = new JSONObject(myArray3.get(i).toString());
                try {
                    int v = Integer.parseInt(json33.get("presence").toString());
                    RadioButton rdbtn2 = new RadioButton(this);
                    rdbtn2.setWidth((int) (((double) width) / 6.5d));
                    this.rg[i].addView(rdbtn2);
                    rdbtn3[i] = new RadioButton(this);
                    rdbtn3[i].setWidth((int) (((double) width) / 6.5d));
                    this.rg[i].addView(rdbtn3[i]);
                    if (v == 1) {
                        rdbtn2.setChecked(true);
                    } else {
                        rdbtn3[i].setChecked(true);
                        this.count++;
                    }
                    rdbtn3[i].setOnCheckedChangeListener(new CheckedChangeListener());
                } catch (Exception e2) {
                    e = e2;
                    this.tvbatch.setText("ERROR   " + e.toString() + this.tvbatch.getText());
                    tableRow.addView(this.rg[i]);
                    i++;
                    json332 = json33;
                }
            } catch (Exception e3) {
                e = e3;
                json33 = json332;
                this.tvbatch.setText("ERROR   " + e.toString() + this.tvbatch.getText());
                tableRow.addView(this.rg[i]);
                i++;
                json332 = json33;
            }
            tableRow.addView(this.rg[i]);
            i++;
            json332 = json33;
        }
        this.absm.setText("A :" + this.count);
        this.presence.setText("P :" + (x - this.count));
        int row = 0;
        JSONObject json32 = null;
        while (row < x) {
            TextView setroll = (TextView) this.rg[row].getChildAt(0);
            try {
                json3 = new JSONObject(myArray3.get(row).toString());
                try {
                    setroll.setText(json3.get("student_id").toString());
                    setroll.setTextSize(2, 12.0f);
                    TextView setname = (TextView) this.rg[row].getChildAt(1);
                    setname.setText(json3.get(downloaddata.COL_6).toString());
                    setname.setTextSize(2, 12.0f);
                    ((RadioButton) this.rg[row].getChildAt(2)).setText("P");
                    ((RadioButton) this.rg[row].getChildAt(3)).setText("A");
                } catch (Exception e4) {
                    e = e4;
                }
            } catch (Exception e5) {
                e = e5;
                json3 = json32;
                this.tvbatch.setText("ERROR   " + e.toString() + this.tvbatch.getText());
                row++;
                json32 = json3;
            }
            row++;
            json32 = json3;
        }
        responsive(myArray3, this.rg, x);
    }

    private void responsive(final JSONArray myArray3, final RadioGroup[] rg, final int x) {
        this.response = (Button) findViewById(C0231R.id.response);
        this.response.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String ss1 = "";
                String ss2 = "";
                insideEdit.this.response.setText("Loading");
                insideEdit.this.mProgressBar.setVisibility(0);
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArrayedit = new JSONArray();
                insideEdit.this.json11 = new JSONObject();
                insideEdit.this.json12 = new JSONObject();
                int slot = Integer.valueOf(insideEdit.this.slotid).intValue();
                for (int row = 0; row < x; row++) {
                    insideEdit.this.name = "";
                    JSONObject jason;
                    JSONObject jasonedit;
                    JSONObject json4;
                    if (((RadioButton) rg[row].getChildAt(3)).isChecked()) {
                        jason = new JSONObject();
                        jasonedit = new JSONObject();
                        try {
                            json4 = new JSONObject(myArray3.get(row).toString());
                            insideEdit.this.forrol = json4.get("student_id").toString();
                            insideEdit.this.name = json4.get(downloaddata.COL_6).toString();
                            jason.put("roll", insideEdit.this.forrol);
                            jason.put("presence", 0);
                            jasonedit.put("student_id", insideEdit.this.forrol);
                            jasonedit.put("presence", 0);
                            jasonedit.put(downloaddata.COL_6, insideEdit.this.name);
                            jsonArray.put(jason);
                            jsonArrayedit.put(jasonedit);
                        } catch (Exception e) {
                        }
                    } else {
                        jasonedit = new JSONObject();
                        jason = new JSONObject();
                        try {
                            json4 = new JSONObject(myArray3.get(row).toString());
                            insideEdit.this.forrol = json4.get("student_id").toString();
                            insideEdit.this.name = json4.get(downloaddata.COL_6).toString();
                            jason.put("roll", insideEdit.this.forrol);
                            jason.put("presence", 1);
                            jasonedit.put("student_id", insideEdit.this.forrol);
                            jasonedit.put("presence", 1);
                            jasonedit.put(downloaddata.COL_6, insideEdit.this.name);
                            jsonArray.put(jason);
                            jsonArrayedit.put(jasonedit);
                        } catch (Exception e2) {
                        }
                    }
                }
                try {
                    insideEdit.this.json2.put(downloaddata.TABLE_NAME4, jsonArray.toString());
                    String username = insideEdit.this.json2.get("username").toString();
                    String password = insideEdit.this.json2.get("password").toString();
                    insideEdit.this.json11.put(downloaddata.TABLE_NAME4, jsonArray.toString());
                    insideEdit.this.json11.put("username", username);
                    insideEdit.this.json11.put("password", password);
                    insideEdit.this.json11.put("slot_id", slot);
                    insideEdit.this.json12.put("username", username);
                    insideEdit.this.json12.put("password", password);
                    insideEdit.this.json12.put(downloaddata.COL_4, insideEdit.this.batch);
                    insideEdit.this.json12.put(downloaddata.COL_3, insideEdit.this.time);
                    insideEdit.this.json12.put(downloaddata.COL_2, insideEdit.this.subje);
                    insideEdit.this.json12.put("slot_id", slot);
                    insideEdit.this.json12.put("editattendance", jsonArrayedit.toString());
                } catch (Exception e3) {
                    System.out.println(e3);
                }
                insideEdit.this.ss = insideEdit.this.json2.toString();
                ss1 = insideEdit.this.json11.toString();
                ss2 = insideEdit.this.json12.toString();
                insideEdit.this.conMgr = (ConnectivityManager) insideEdit.this.getApplicationContext().getSystemService("connectivity");
                insideEdit.this.activeNetwork = insideEdit.this.conMgr.getActiveNetworkInfo();
                if (insideEdit.this.activeNetwork == null || !insideEdit.this.activeNetwork.isConnected()) {
                    insideEdit.this.myDb.deleteattendance(slot);
                    insideEdit.this.myDb.insertattendance(slot, ss1, ss2);
                    insideEdit.this.myDb.deleteslot(slot);
                    insideEdit.this.json3 = new JSONObject();
                    String fin2 = "";
                    try {
                        insideEdit.this.username2 = insideEdit.this.json2.get("username").toString();
                        insideEdit.this.password2 = insideEdit.this.json2.get("password").toString();
                        insideEdit.this.json3.put("username", insideEdit.this.username2);
                        insideEdit.this.json3.put("password", insideEdit.this.password2);
                        fin2 = insideEdit.this.json3.toString();
                        Intent intent = new Intent(insideEdit.this, Done.class);
                        intent.putExtra("message", fin2);
                        insideEdit.this.finish();
                        insideEdit.this.startActivity(intent);
                        return;
                    } catch (JSONException e4) {
                        System.out.println(e4);
                        return;
                    }
                }
                new attendancerequest().execute(new Void[0]);
            }
        });
    }
}
