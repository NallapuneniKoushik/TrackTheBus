package nkc.com.trackthebus;

public class  MyFBHandler {
    String dfname,dlname,dcno,demail,dob,dpass,dbname;
    public MyFBHandler(String dfname, String dlname, String dcno, String demail, String dob, String dpass) {
        this.dfname = dfname;
        this.dlname = dlname;
        this.dcno = dcno;
        this.demail = demail;
        this.dob = dob;
        this.dpass = dpass;
    }
    public MyFBHandler()
    {}




    public String getDfname() {
        return dfname;
    }

    public void setDfname(String dfname) {
        this.dfname = dfname;
    }

    public String getDlname() {
        return dlname;
    }

    public void setDlname(String dlname) {
        this.dlname = dlname;
    }

    public String getDcno() {
        return dcno;
    }

    public void setDcno(String dcno) {
        this.dcno = dcno;
    }

    public String getDemail() {
        return demail;
    }

    public void setDemail(String demail) {
        this.demail = demail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDpass() {
        return dpass;
    }

    public void setDpass(String dpass) {
        this.dpass = dpass;
    }
}

