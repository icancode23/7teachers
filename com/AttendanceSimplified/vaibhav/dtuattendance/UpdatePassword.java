package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.json.JSONObject;

public class UpdatePassword extends AppCompatActivity {
    Button bpass;
    EditText ettest;
    ProgressBar mProgressBar;
    TextView tvloading;

    public class serverConnect extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 15150;
        int id;
        String js;
        String message;
        String npass;
        String pass;
        String response = "";
        String user;

        serverConnect(String username, String password, String m) {
            this.user = username;
            this.pass = password;
            this.npass = m;
        }

        protected Void doInBackground(Void... arg0) {
            Exception e;
            Throwable th;
            Socket socket = null;
            try {
                Socket socket2 = new Socket(this.dstAddress, this.dstPort);
                try {
                    JSONObject json = new JSONObject();
                    json.put("username", this.user);
                    json.put("password", this.pass);
                    json.put("new_password", this.npass);
                    byte[] s2 = Encrypt3.encrypt2(json.toString());
                    DataOutputStream dOut = new DataOutputStream(socket2.getOutputStream());
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream dIn = new DataInputStream(socket2.getInputStream());
                    int length = dIn.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0 && length < 102400) {
                        dIn.readFully(messages, 0, messages.length);
                    }
                    this.message = Encrypt3.decrypt2(messages);
                    this.js = new JSONObject(this.message).get("status").toString();
                    this.id = Integer.parseInt(this.js);
                    if (socket2 != null) {
                        try {
                            socket2.close();
                            socket = socket2;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            socket = socket2;
                        }
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    socket = socket2;
                    try {
                        this.message += " error " + e2;
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (Exception e22) {
                                e22.printStackTrace();
                            }
                        }
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (Exception e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    socket = socket2;
                    if (socket != null) {
                        socket.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e222 = e4;
                this.message += " error " + e222;
                if (socket != null) {
                    socket.close();
                }
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            UpdatePassword.this.mProgressBar.setVisibility(4);
            UpdatePassword.this.tvloading.setText("");
            if (this.id == 0) {
                new Builder(UpdatePassword.this).setMessage((CharSequence) "WRONG USERNAME/PASSWORD").setNegativeButton((CharSequence) "Retry", null).create().show();
            } else {
                new Builder(UpdatePassword.this).setMessage((CharSequence) "PASSWORD CHANGED").setNegativeButton((CharSequence) "OK", null).create().show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0231R.layout.activity_update_password);
        TextView tvtitle = (TextView) findViewById(C0231R.id.tvtitle);
        final EditText etus = (EditText) findViewById(C0231R.id.etusername);
        final EditText etpa = (EditText) findViewById(C0231R.id.etpassword);
        final EditText etnewpass = (EditText) findViewById(C0231R.id.etnewpass);
        this.bpass = (Button) findViewById(C0231R.id.bpass);
        this.tvloading = (TextView) findViewById(C0231R.id.tvloading);
        this.mProgressBar = (ProgressBar) findViewById(C0231R.id.progressBar2);
        this.mProgressBar.setVisibility(8);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        this.mProgressBar.getLayoutParams().width = displaymetrics.widthPixels / 4;
        this.mProgressBar.getLayoutParams().height = height / 13;
        this.bpass.getLayoutParams().height = height / 10;
        this.bpass.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UpdatePassword.this.mProgressBar.setVisibility(0);
                UpdatePassword.this.tvloading.setText("Loading");
                new serverConnect(etus.getText().toString(), etpa.getText().toString(), etnewpass.getText().toString()).execute(new Void[0]);
            }
        });
    }
}
