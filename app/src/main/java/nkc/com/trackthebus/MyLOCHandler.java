package nkc.com.trackthebus;
public class MyLOCHandler  {
    double lat,lag;
public  MyLOCHandler(){}
    public MyLOCHandler(double lat, double lag) {
        this.lat = lat;
        this.lag = lag;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLag() {
        return lag;
    }

    public void setLag(double lag) {
        this.lag = lag;
    }
}
