package com.litCitrus.zamongcampusServer.service.college;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litCitrus.zamongcampusServer.domain.user.College;
import com.litCitrus.zamongcampusServer.exception.college.CollegeException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CollegeApiServiceImpl implements CollegeApiService{
    private final String url;
    private final String collegeSearchUrl;
    private final ObjectMapper objectMapper;

    public CollegeApiServiceImpl(@Value("${open-api.key}") String apiKey, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        url = "https://www.career.go.kr/cnet/openapi/getOpenApi?apiKey=" + apiKey;
        collegeSearchUrl = url + "&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&searchSchulNm=";
    }

    @Override
    public Optional<College> searchCollege(Long collegeSeq, String collegeName) {
        List<Object> searchResultList = null;
        CloseableHttpClient client = HttpClients.createDefault();
        boolean isMatched = false;

        try(client) {
            HttpGet httpGet = new HttpGet(collegeSearchUrl + collegeName);
            CloseableHttpResponse response = client.execute(httpGet);

            try(response) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
//                    String major = new JSONObject(result).getJSONObject("dataSearch").getJSONArray("content")
//                            .getJSONObject(0).getString("major");
//                    if (isMatched = major.equals(collegeName)) {
//                        return Optional.of(Major.createMajor(collegeSeq, collegeName));
//                    }

                    searchResultList = new JSONObject(result).getJSONObject("dataSearch").getJSONArray("content")
                            .toList();

                    //HashMap resultMap = (HashMap) searchResultList.stream().filter(college->((HashMap)college).get("schoolName").equals(collegeName)).collect(Collectors.toList()).get(0);
                    for(Object searchResult : searchResultList){
                        HashMap resultMap = (HashMap) searchResult;

                        //학교 Sequence Number
                        double searchedCollegeSeqDouble = Double.parseDouble((String)resultMap.get("seq"));
                        Long searchedCollegeSeq = (long) searchedCollegeSeqDouble;

                        //학교명
                        String searchedCollegeName = (String)resultMap.get("schoolName");


                        if(searchedCollegeSeq.equals(collegeSeq) && searchedCollegeName.equals(collegeName)){
                            isMatched = true;

                            //캠퍼스명
                            String campusName = (String)resultMap.get("campusName");
                            //학교주소
                            String address = (String)resultMap.get("adres");
                            //ex) 서울특별시 관악구 관악로 1 (신림동, 서울대학교) -> 괄호로 감싸진 상세주소 제거
                            address = address.replaceAll("\\s\\(([\\w|\\W]+[\\,\\s+[\\w|\\W]+]+)","");

                            Optional<College> college = Optional.of(College.createCollege(searchedCollegeSeq, collegeName, campusName, address));
                            return college;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isMatched){
            throw CollegeException.NOT_MATCHED;
        }
        //return searchResultList;
        return Optional.empty();
    }
}
