package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Done extends AppCompatActivity {
    NetworkInfo activeNetwork;
    ConnectivityManager conMgr;
    Button done;
    String fin;
    int id;
    JSONObject json2;
    ProgressBar mProgressBar;
    getsyncdata myDb2;
    downloaddata myDb3;
    String password;
    String username;

    class C02251 implements OnClickListener {
        C02251() {
        }

        public void onClick(View v) {
            Done.this.mProgressBar.setVisibility(0);
            Done.this.done.setEnabled(false);
            Done.this.conMgr = (ConnectivityManager) Done.this.getApplicationContext().getSystemService("connectivity");
            Done.this.activeNetwork = Done.this.conMgr.getActiveNetworkInfo();
            if (Done.this.activeNetwork == null || !Done.this.activeNetwork.isConnected()) {
                try {
                    Done.this.offlinepopulate(Done.this.username, Done.this.password);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Done.this.myDb2.senddata();
            try {
                Done.this.offlinepopulate(Done.this.username, Done.this.password);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
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
            Socket socket;
            Exception e;
            String message = "";
            String testServerName = "attendancesimplified.com";
            JSONObject json = new JSONObject();
            try {
                Done.this.id = 0;
                SocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName(testServerName), this.dstPort);
                Socket socket2 = new Socket();
                try {
                    socket2.connect(inetSocketAddress, 10000);
                    json.put("username", this.username2);
                    json.put("password", this.password2);
                    String s = json.toString();
                    System.out.println(s + "\n");
                    Object s2 = Encrypt3.encrypt2(s);
                    System.out.println(" Encrypted : " + s2 + "\n");
                    OutputStream os = socket2.getOutputStream();
                    DataOutputStream dOut = new DataOutputStream(os);
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    os.flush();
                    dOut.flush();
                    DataInputStream dIn = new DataInputStream(socket2.getInputStream());
                    int length = dIn.readInt();
                    if (length > 0 && length < 204800) {
                        byte[] data = new byte[length];
                        dIn.readFully(data, 0, data.length);
                        message = Encrypt3.decrypt2(data);
                        System.out.println(" ---------  PRE JSON  CHECKPOINT ----------------------- ");
                        Done.this.json2db(message);
                        System.out.println(" ---------  POST JSON  CHECKPOINT ----------------------- ");
                        JSONObject json2 = new JSONObject(message);
                        json2.put("username", this.username2);
                        json2.put("password", this.password2);
                        String js = json2.get("status").toString();
                        Done.this.id = Integer.parseInt(js);
                    }
                    socket2.close();
                    socket = socket2;
                } catch (Exception e2) {
                    e = e2;
                    socket = socket2;
                    System.out.println(e);
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                System.out.println(e);
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Done.this.mProgressBar.setVisibility(4);
            if (Done.this.id == 1) {
                try {
                    Done.this.offlinepopulate(this.username2, this.password2);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            new Builder(Done.this).setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
            Done.this.mProgressBar.setVisibility(4);
            System.out.println("FAIL");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0231R.layout.activity_done);
        String mess = getIntent().getExtras().getString("message");
        this.myDb3 = new downloaddata(this);
        try {
            this.json2 = new JSONObject(mess);
            this.username = this.json2.get("username").toString();
            this.password = this.json2.get("password").toString();
            this.myDb2 = new getsyncdata(this, this.username, this.password);
        } catch (Exception e) {
        }
        this.done = (Button) findViewById(C0231R.id.done);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ImageView yourimg = (ImageView) findViewById(C0231R.id.aaa);
        TextView tvanim = (TextView) findViewById(C0231R.id.tvanim);
        this.done.getLayoutParams().width = width;
        this.done.getLayoutParams().height = height / 10;
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 11;
        this.mProgressBar.setVisibility(8);
        yourimg.getLayoutParams().height = height / 5;
        yourimg.getLayoutParams().width = width / 3;
        Animation anim = AnimationUtils.loadAnimation(this, C0231R.anim.alpha_anim);
        Animation anim2 = AnimationUtils.loadAnimation(this, C0231R.anim.rotate_anim);
        Animation anim3 = AnimationUtils.loadAnimation(this, C0231R.anim.scale_anim);
        Animation anim4 = AnimationUtils.loadAnimation(this, C0231R.anim.rotate_anim2);
        AnimationSet s = new AnimationSet(false);
        AnimationSet s2 = new AnimationSet(false);
        s.addAnimation(anim);
        s.addAnimation(anim2);
        s.addAnimation(anim3);
        s2.addAnimation(anim);
        s2.addAnimation(anim4);
        s2.addAnimation(anim3);
        yourimg.startAnimation(s);
        tvanim.startAnimation(s2);
        this.done.startAnimation(s);
        this.done.setOnClickListener(new C02251());
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
            System.out.println(e);
        }
    }
}
