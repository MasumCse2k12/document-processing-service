package com.example.csv.util;

import com.example.csv.core.entities.TempLead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String[] VALID_MOBILE_PREFIX = new String[]{"015", "017", "014", "016", "018", "019", "013"};

    public static boolean isValidMobileNo(String mobileNo) {
        if (mobileNo == null || mobileNo.trim().length() == 0) return false;

        int length = mobileNo.trim().length();

        if (!(length >= 11 && length <= 12)) return false;

        if (!Arrays.asList(VALID_MOBILE_PREFIX).contains(mobileNo.trim().substring(0, 3))) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(mobileNo.trim());
        try {
            return matcher.matches();
        } catch (
                Exception e) {
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().length() == 0) return false;
        Pattern pattern = Pattern.compile("^(.+)@(\\S+)$");
        Matcher matcher = pattern.matcher(email.trim());
        try {
            return matcher.matches();
        } catch (
                Exception e) {
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object object) {
        if (object == null) return true;

        return String.valueOf(object).trim().length() == 0;
    }

    public static List<String> validateTempLead(TempLead lead) {
        List<String> violations = new ArrayList<>();

        if (isNullOrEmpty(lead.getEmail())) {
            violations.add("EMPTY_EMAIL");
        }

        if (isNullOrEmpty(lead.getPhone())) {
            violations.add("EMPTY_PHONE");
        }

        if (isValidEmail(lead.getEmail())) {
            violations.add("INVALID_EMAIL");
        }

        if (isValidMobileNo(lead.getPhone())) {
            violations.add("INVALID_PHONE");
        }

        return violations;
    }
}
