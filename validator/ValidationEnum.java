package validator;

import bean.Validation;
import utils.DateUtil;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author lomofu
 * <p>
 * This enum defines the validation type for the form input
 */
public enum ValidationEnum {
    // cover NPE
    NOT_NULL() {
        @Override
        public boolean isValid(Object value) {
            return Objects.nonNull(value);
        }

        @Override
        public String errorMsg() {
            return "can not be null";
        }
    },

    // cover NPE & string should not be ""
    HAS_LEN() {
        @Override
        public boolean isValid(Object value) {
            if (Objects.isNull(value)) {
                return false;
            }
            if (value instanceof String val) {
                return !"".equals(val);
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "can not be empty";
        }
    },

    // make sure the collection has a size > 0
    NOT_EMPTY() {
        @Override
        public boolean isValid(Object value) {
            if (Objects.isNull(value)) {
                return false;
            }

            if (value instanceof Collection val) {
                return !val.isEmpty();
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "can not be empty";
        }
    },

    // check the date has the correct format
    NOT_INVALIDATE_DATE() {
        @Override
        public boolean isValid(Object value) {
            if (Objects.isNull(value)) {
                return false;
            }
            if (value instanceof String val) {
                Date date = DateUtil.str2Date(val);
                return Optional.ofNullable(date).isPresent();
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "date format is invalid";
        }
    },

    // age > 12
    GREATER_THAN_12() {
        @Override
        public boolean isValid(Object value) {
            if (Objects.isNull(value)) {
                return false;
            }
            if (value instanceof String val) {
                if ("".equals(val))
                    return false;
                int age = Integer.parseInt(val);
                return age >= 12;
            }
            return false;
        }


        @Override
        public String errorMsg() {
            return "should greater than 12";
        }
    },

    // age > 18
    GREATER_THAN_18() {
        @Override
        public boolean isValid(Object value) {
            if (Objects.isNull(value)) {
                return false;
            }
            if (value instanceof String val) {
                if ("".equals(val))
                    return false;
                int age = Integer.parseInt(val);
                return age >= 18;
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "should greater than 18";
        }
    },

    // the date should not over today(now)
    NOT_GREATER_THAN_NOW() {
        @Override
        public boolean isValid(Object value) {
            if (Objects.isNull(value)) {
                return false;
            }
            if (value instanceof Date val) {
                return DateUtil.isBefore(val, LocalDate.now());
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "should not greater than today";
        }
    },

    // the uk phone number regex
    UK_PHONE_NUMBER() {
        @Override
        public boolean isValid(Object value) {
            if (value instanceof String val) {
                return PHONE_PATTERN.matcher(val).matches();
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "is not legal";
        }
    };

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(((\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?#(\\d{4}|\\d{3}))?$");

    /**
     * This method mainly implements the corresponding legitimacy checks on the rules of the configured form fields
     * The order of validation depends on the order of allocation
     *
     * @param map key is the field name and validation contents
     * @return result of error messages
     */
    public static List<String> valid(Map<String, Validation> map) {
        // define an empty list to store the error messages
        List<String> list = new ArrayList<>();
        // cover the NPE
        if (Objects.isNull(map) || map.isEmpty()) {
            list.add("Validators should not be empty");
            return list;
        }
        // iterated the validation rules and validate each input is illegal or not
        for (Map.Entry<String, Validation> entry : map.entrySet()) {
            ValidationEnum[] validationEnums = entry.getValue().validationEnum();
            List<String> temp = new ArrayList<>();
            for (ValidationEnum validationEnum : validationEnums) {
                boolean valid = validationEnum.isValid(entry.getValue().value());
                // if is illegal, go to the next round
                if (valid) {
                    continue;
                }
                // add the error message into the list
                temp.add(validationEnum.errorMsg());
            }
            // format the error messages
            if (temp.size() != 0) {
                list.add(MessageFormat.format("{0} {1}\n", entry.getKey(), temp.get(0)));
            }
            // reduce the loop time if exist error immediately return the list
            if (list.size() == 1) {
                return list;
            }
        }
        return list;
    }

    // define the valid function of the enum
    public abstract boolean isValid(Object value);

    // define the error input
    public abstract String errorMsg();
}
