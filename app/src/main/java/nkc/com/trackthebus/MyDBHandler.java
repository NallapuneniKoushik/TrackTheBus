package nkc.com.trackthebus;

public class MyDBHandler {

    String user_name,busName;


    public MyDBHandler(String user_name, String busName) {
        this.user_name = user_name;
        this.busName = busName;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
