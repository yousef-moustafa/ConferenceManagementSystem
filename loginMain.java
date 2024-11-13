public class loginMain {
    public static void main(String[] args) {
        IDandPasswords idandPasswords = new IDandPasswords();

        LoginUI loginUI = new LoginUI(idandPasswords.getLoginInfo());
    }
}
