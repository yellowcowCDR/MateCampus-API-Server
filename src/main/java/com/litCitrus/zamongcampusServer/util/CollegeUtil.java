package com.litCitrus.zamongcampusServer.util;

public class CollegeUtil {
    public static String extractCollegeName(String name){
        String campusNameFilterStr = "";
        int searchedCollegeNameIndex = 0;
        int collegeNameLastIndex = 0;
        if(name.contains("대학교")){
            campusNameFilterStr="대학교";
            searchedCollegeNameIndex = name.indexOf(campusNameFilterStr);
        }else if(name.contains("대학")){
            campusNameFilterStr="대학";
            searchedCollegeNameIndex = name.indexOf(campusNameFilterStr);
        }
        collegeNameLastIndex = searchedCollegeNameIndex+campusNameFilterStr.length();
        name =name.substring(0, collegeNameLastIndex);

        return name;
    }
}
