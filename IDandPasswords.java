import java.util.*;

public class IDandPasswords {
    HashMap<String, String> logininfo = new HashMap<String, String>();

    IDandPasswords() {
        logininfo.put("yousef", "123");
    }

    protected HashMap getLoginInfo() {
        return logininfo;
    }
}
