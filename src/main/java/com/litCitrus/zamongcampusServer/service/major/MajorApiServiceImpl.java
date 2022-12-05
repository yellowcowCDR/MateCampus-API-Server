package com.litCitrus.zamongcampusServer.service.major;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litCitrus.zamongcampusServer.domain.major.Major;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MajorApiServiceImpl implements MajorApiService {

    private final String url;
    private final String majorSearchUrl;
    private final ObjectMapper objectMapper;

    public MajorApiServiceImpl(@Value("${open-api.key}") String apiKey, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        url = "https://www.career.go.kr/cnet/openapi/getOpenApi?apiKey=" + apiKey;
        majorSearchUrl = url + "&svcType=api&svcCode=MAJOR_VIEW&contentType=json&gubun=univ_list&majorSeq=";
    }

    @Override
    public Optional<Major> searchMajor(long majorSeq, String mName) {
        CloseableHttpClient client = HttpClients.createDefault();
        try(client) {
            HttpGet httpGet = new HttpGet(majorSearchUrl + majorSeq);
            CloseableHttpResponse response = client.execute(httpGet);

            try(response) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    String major = new JSONObject(result).getJSONObject("dataSearch").getJSONArray("content")
                            .getJSONObject(0).getString("major");
                    if (major.equals(mName)) {
                        return Optional.of(Major.createMajor(majorSeq, mName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
