package validator;

import bean.Validation;
import utils.DateUtil;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author lomofu
 * @desc
 * @create 08/Dec/2021 15:46
 */
public enum ValidationEnum {
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

    HAS_LEN() {
        @Override
        public boolean isValid(Object value) {
            if(Objects.isNull(value)) {
                return false;
            }
            if(value instanceof String val) {
                return ! "".equals(val);
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "can not be empty";
        }
    },

    NOT_EMPTY() {
        @Override
        public boolean isValid(Object value) {
            if(Objects.isNull(value)) {
                return false;
            }

            if(value instanceof Collection val) {
                return ! val.isEmpty();
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "can not be empty";
        }
    },

    NOT_INVALIDATE_DATE() {
        @Override
        public boolean isValid(Object value) {
            if(Objects.isNull(value)) {
                return false;
            }
            if(value instanceof String val) {
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

    GREATER_THAN_12() {
        @Override
        public boolean isValid(Object value) {
            if(Objects.isNull(value)) {
                return false;
            }
            if(value instanceof String val) {
                if("".equals(val))
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

    GREATER_THAN_18() {
        @Override
        public boolean isValid(Object value) {
            if(Objects.isNull(value)) {
                return false;
            }
            if(value instanceof String val) {
                if("".equals(val))
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

    NOT_GREATER_THAN_NOW() {
        @Override
        public boolean isValid(Object value) {
            if(Objects.isNull(value)) {
                return false;
            }
            if(value instanceof Date val) {
                return DateUtil.isBefore(val, LocalDate.now());
            }
            return false;
        }

        @Override
        public String errorMsg() {
            return "should not greater than today";
        }
    },

    UK_PHONE_NUMBER() {
        @Override
        public boolean isValid(Object value) {
            if(value instanceof String val) {
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

    public static List<String> valid(Map<String, Validation> map) {
        List<String> list = new ArrayList<>();
        if(Objects.isNull(map) || map.isEmpty()) {
            list.add("Validators should not be empty");
            return list;
        }
        for(Map.Entry<String, Validation> entry : map.entrySet()) {
            ValidationEnum[] validationEnums = entry.getValue().validationEnum();
            List<String> temp = new ArrayList<>();
            for(ValidationEnum validationEnum : validationEnums) {
                boolean valid = validationEnum.isValid(entry.getValue().value());
                if(valid) {
                    continue;
                }
                temp.add(validationEnum.errorMsg());
            }
            if(temp.size() != 0) {
                list.add(MessageFormat.format("{0} {1}\n", entry.getKey(), temp.get(0)));
            }
            if(list.size() == 1) {
                return list;
            }
        }
        return list;
    }

    public abstract boolean isValid(Object value);

    public abstract String errorMsg();
}
