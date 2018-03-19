package Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vinay on 18-03-2018.
 */

public class User {
    private int uid;
    private String Name;
    private String email;
    private String password;

    public User(int uid, String name, String email, String password) {
        this.uid = uid;
        Name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return this.uid+"=>"+this.Name+"\t"+this.email;
    }
    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",this.uid);
            jsonObject.put("Name",this.Name);
            jsonObject.put("email",this.email);
            jsonObject.put("password",this.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public User(JSONObject jsonObject){
        try {
            uid=jsonObject.getInt("uid");
            Name=jsonObject.getString("Name");
            email=jsonObject.getString("email");
            password=jsonObject.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
