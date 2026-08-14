package za.ac.mycput.studentregistrationsystembackend.Util;

public class Helper {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidId(int id) {
        return id > 0;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }
}