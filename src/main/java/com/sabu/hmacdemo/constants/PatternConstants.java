package com.sabu.hmacdemo.constants;

import java.util.regex.Pattern;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
public class PatternConstants {
    public static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile("^(\\w+) (\\S+):(\\S+):([\\S]+)$");
    public static final Pattern AUTHORIZATION_TIMESTAMP_PATTERN = Pattern.compile("^[0-9]*$");

}
