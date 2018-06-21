public class Constraints {

    public static boolean ifNameFormattingWrong(String name) {
        name.trim();
        if (!name.contains(" ")) return true;
        for (int i = 0; i < name.length(); i++)
            if (Character.isDigit(name.charAt(i)))
                return true;
        return false;
    }

    public static boolean ifPhoneFormatWrong(String number) {
        if (number.length() != 12) return true;
        if (number.charAt(3) != '-') return true;
        if (number.charAt(7) != '-') return true;
        String newString = number.replace("-", "");
        for (int i = 0; i < newString.length(); i++)
            if (!Character.isDigit(newString.charAt(i)))
                return true;
        return false;
    }

    public static boolean ifIDFormatCorrect(String id) {
        if (id.length() == 0) return false;
        for (int i = 0; i < id.length(); i++)
            if (!Character.isDigit(id.charAt(i)))
                return false;
        return true;
    }

    public static boolean ifCorrectDateFormat(String date) {
        int year, month, day;
        date = date.trim();
        if (date.length() != 8) return false;
        if (!Character.isDigit(date.charAt(0))) return false;
        if (!Character.isDigit(date.charAt(1))) return false;
        if (date.charAt(2) != '-') return false;
        if (!Character.isDigit(date.charAt(3))) return false;
        if (!Character.isDigit(date.charAt(4))) return false;
        if (date.charAt(5) != '-') return false;
        if (!Character.isDigit(date.charAt(6))) return false;
        if (!Character.isDigit(date.charAt(7))) return false;
        year = Integer.parseInt(date.substring(0, 2));
        month = Integer.parseInt(date.substring(3, 5));
        day = Integer.parseInt(date.substring(6, 8));
        if (month > 12 || month < 1) return false;
        if (day < 1 || day > 31) return false;
        if (month == 2) {
            if (year%4 == 0 && day > 29) return false;
            if (year%4 != 0 && day > 28) return false;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11)
            if (day > 30) return false;
        return true;
    }

}
