package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
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

public class front extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    String f24a = "";
    String aa;
    Button blogin;
    Button breg;
    Context f25c;
    int dcheck;
    EditText etpass;
    EditText etuser;
    String fin;
    int id;
    ProgressBar mProgressBar;
    String message;
    downloaddata myDb;
    getsyncdata myDb2;
    getsyncdata myDb3;
    ProgressDialog progressDialog2;
    TextView tvload;
    Button uppass;
    int f26x;

    class C02362 implements OnClickListener {
        C02362() {
        }

        public void onClick(View v) {
            front.this.startActivity(new Intent(front.this, bregact.class));
        }
    }

    class C02383 implements OnClickListener {
        C02383() {
        }

        public void onClick(View v) {
            front.this.mProgressBar.setVisibility(0);
            front.this.tvload.setText("loading...");
            front.this.blogin.setEnabled(false);
            final String usern = front.this.etuser.getText().toString();
            final String password = front.this.etpass.getText().toString();
            front.this.myDb2 = new getsyncdata(front.this.f25c, usern, password);
            NetworkInfo activeNetwork = ((ConnectivityManager) front.this.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                new Builder(front.this).setMessage("Offline").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -2:
                                try {
                                    front.this.offlinepopulate(usern, password);
                                    return;
                                } catch (JSONException e) {
                                    new Builder(front.this).setMessage((CharSequence) e).setNegativeButton("Error Occured", null).create().show();
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                }).create().show();
                front.this.mProgressBar.setVisibility(4);
                front.this.blogin.setEnabled(true);
                front.this.tvload.setText("");
                return;
            }
            front.this.myDb2.senddata();
            new loginrequest(usern, password).execute(new Void[0]);
        }
    }

    class C02394 implements OnClickListener {
        C02394() {
        }

        public void onClick(View v) {
            front.this.startActivity(new Intent(front.this, UpdatePassword.class));
        }
    }

    public class loginrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 4444;
        String message;
        String name2;
        String password2;
        TextView status2;
        String username2;

        loginrequest(String usern, String password) {
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
                front.this.id = 0;
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
                        front.this.json2db(message);
                        System.out.println(" ---------  POST JSON  CHECKPOINT ----------------------- ");
                        JSONObject json2 = new JSONObject(message);
                        json2.put("username", this.username2);
                        json2.put("password", this.password2);
                        String js = json2.get("status").toString();
                        front.this.id = Integer.parseInt(js);
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
            if (front.this.dcheck == 1) {
                front.this.progressDialog2.dismiss();
            }
            if (front.this.id == 1) {
                if (front.this.f26x != 1) {
                    Editor editor = front.this.getSharedPreferences(front.PREFS_NAME, 0).edit();
                    editor.putString("logged", "logged");
                    editor.putString("username", this.username2);
                    editor.putString("password", this.password2);
                    editor.commit();
                }
                try {
                    front.this.offlinepopulate(this.username2, this.password2);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            new Builder(front.this).setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
            front.this.mProgressBar.setVisibility(4);
            front.this.blogin.setEnabled(true);
            front.this.tvload.setText("");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0231R.layout.activity_front);
        this.dcheck = 0;
        this.myDb = new downloaddata(this);
        this.f25c = this;
        this.blogin = (Button) findViewById(C0231R.id.done);
        this.uppass = (Button) findViewById(C0231R.id.uppass);
        this.tvload = (TextView) findViewById(C0231R.id.tvload);
        int bgnt = Color.parseColor("#1111cc");
        ImageView yourimg = (ImageView) findViewById(C0231R.id.aaa);
        this.etuser = (EditText) findViewById(C0231R.id.etuser);
        this.etpass = (EditText) findViewById(C0231R.id.etpass);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        float a = (float) (((double) width) / 1.4d);
        float d = (float) (((double) width) / 1.2d);
        yourimg.getLayoutParams().height = (int) ((float) (((double) height) / 1.8d));
        yourimg.getLayoutParams().width = (int) a;
        this.etuser.getLayoutParams().width = width / 2;
        this.etpass.getLayoutParams().width = width / 2;
        this.blogin.getLayoutParams().width = width / 2;
        this.blogin.getLayoutParams().height = height / 10;
        this.uppass.getLayoutParams().width = width / 2;
        this.uppass.getLayoutParams().height = height / 10;
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar);
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 12;
        this.mProgressBar.setVisibility(8);
        this.f26x = 0;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            this.f26x = 1;
            this.progressDialog2 = new ProgressDialog(this, C0231R.style.AppTheme.Dark.Dialog);
            this.progressDialog2.setIndeterminate(true);
            this.progressDialog2.setMessage("Autologging in");
            this.progressDialog2.show();
            this.dcheck = 1;
            String username = settings.getString("username", "").toString();
            String password = settings.getString("password", "").toString();
            this.etuser.setText(username);
            this.etpass.setText(password);
            NetworkInfo activeNetwork = ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                final String str = username;
                final String str2 = password;
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -2:
                                try {
                                    front.this.offlinepopulate(str, str2);
                                    return;
                                } catch (JSONException e) {
                                    new Builder(front.this).setMessage((CharSequence) e).setNegativeButton("Error Occured", null).create().show();
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                };
                this.progressDialog2.dismiss();
                new Builder(this).setMessage("Offline Mode").setNegativeButton("OK", dialogClickListener).create().show();
                this.mProgressBar.setVisibility(4);
                this.blogin.setEnabled(true);
                this.tvload.setText("");
            } else {
                this.myDb3 = new getsyncdata(this.f25c, username, password);
                this.myDb3.senddata();
                new loginrequest(username, password).execute(new Void[0]);
            }
        }
        this.breg = (Button) findViewById(C0231R.id.breg);
        this.breg.setOnClickListener(new C02362());
        this.blogin.setOnClickListener(new C02383());
        this.uppass.setOnClickListener(new C02394());
    }

    private void offlinepopulate(String username, String password) throws JSONException {
        String col1;
        String col2;
        String col3;
        String col4;
        JSONArray jsonArray = new JSONArray();
        Cursor res = this.myDb.getAllData();
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

    public void showMessage(String title, String Message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void json2db(String json_data) {
        System.out.println("JSON DATA SYNCING IS : " + json_data);
        try {
            downloaddata dbin = new downloaddata(this);
            JSONObject j = new JSONObject(json_data);
            if (j.get("status").toString().equals("1")) {
                int i;
                this.myDb.truncatestudent();
                this.myDb.truncateslotstudent();
                this.myDb.truncateslots();
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
