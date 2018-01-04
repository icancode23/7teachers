package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.json.JSONArray;
import org.json.JSONObject;

public class getsyncdata {
    static Context f11c;
    static Context c1;
    int id;
    downloaddata myDb3;
    String pass;
    int scheck;
    String username;

    public class attendancerequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 16060;
        String fin = "";
        int id;
        JSONObject json22;
        String message;
        String password2;
        String send;
        int slot;
        String username2;

        attendancerequest(String username, String pass, String ss, int s) {
            this.username2 = username;
            this.password2 = pass;
            this.send = ss;
            this.slot = s;
        }

        protected Void doInBackground(Void... arg0) {
            Exception e;
            try {
                Socket socket = new Socket(this.dstAddress, this.dstPort);
                Socket socket2;
                try {
                    byte[] s2 = Encrypt3.encrypt2(this.send);
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
            if (this.id == 1) {
                getsyncdata.this.myDb3.deleteattendance(this.slot);
            } else {
                getsyncdata.this.scheck = 0;
            }
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
            Exception e;
            String message = "";
            String testServerName = "attendancesimplified.com";
            JSONObject json = new JSONObject();
            try {
                getsyncdata.this.id = 0;
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
                        getsyncdata.json2db(message);
                        JSONObject json2 = new JSONObject(message);
                        json2.put("username", this.username2);
                        json2.put("password", this.password2);
                        String js = json2.get("status").toString();
                        getsyncdata.this.id = Integer.parseInt(js);
                    }
                    socket.close();
                    socket2 = socket;
                } catch (Exception e2) {
                    e = e2;
                    socket2 = socket;
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
            if (getsyncdata.this.id == 1) {
                System.out.println("success");
            } else {
                System.out.println("FAIL");
            }
        }
    }

    public getsyncdata(Context context, String usern, String password) {
        f11c = context;
        this.username = usern;
        this.pass = password;
    }

    public void getdata() {
        new loginrequest(this.username, this.pass).execute(new Void[0]);
    }

    public void senddata() {
        this.myDb3 = new downloaddata(f11c);
        Cursor res = this.myDb3.getattendance();
        this.scheck = 1;
        if (res.getCount() != 0) {
            while (res.moveToNext() && this.scheck == 1) {
                String id = "";
                String send = "";
                id = res.getString(0);
                new attendancerequest(this.username, this.pass, res.getString(1), Integer.parseInt(id)).execute(new Void[0]);
            }
        }
    }

    public static void json2db(String json_data) {
        try {
            downloaddata dbin = new downloaddata(f11c);
            JSONObject j = new JSONObject(json_data);
            if (j.get("status").toString().equals("1")) {
                int i;
                Boolean t;
                JSONArray slots = new JSONArray(j.get(downloaddata.TABLE_NAME1).toString());
                for (i = 0; i < slots.length(); i++) {
                    JSONObject slot_i = new JSONObject(slots.get(i).toString());
                    System.out.println(i + " " + slot_i.get("slot_id") + " " + slot_i.get(downloaddata.COL_2) + " " + slot_i.get(downloaddata.COL_3) + " " + slot_i.get(downloaddata.COL_4));
                    t = Boolean.valueOf(dbin.insertslots(slot_i.get("slot_id").toString(), slot_i.get(downloaddata.COL_2).toString(), slot_i.get(downloaddata.COL_3).toString(), slot_i.get(downloaddata.COL_4).toString()));
                }
                JSONArray slot_student = new JSONArray(j.get(downloaddata.TABLE_NAME3).toString());
                for (i = 0; i < slot_student.length(); i++) {
                    JSONObject slot_student_i = new JSONObject(slot_student.get(i).toString());
                    System.out.println(i + " " + slot_student_i.get("slot_id") + " " + slot_student_i.get("student_id"));
                    t = Boolean.valueOf(dbin.insertslotid(slot_student_i.get("slot_id").toString(), slot_student_i.get("student_id").toString()));
                }
                JSONArray student = new JSONArray(j.get(downloaddata.TABLE_NAME2).toString());
                for (i = 0; i < student.length(); i++) {
                    JSONObject student_i = new JSONObject(student.get(i).toString());
                    System.out.println(i + " " + student_i.get("student_id") + " " + student_i.get(downloaddata.COL_6));
                    t = Boolean.valueOf(dbin.insertstudent(student_i.get("student_id").toString(), student_i.get(downloaddata.COL_6).toString()));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
