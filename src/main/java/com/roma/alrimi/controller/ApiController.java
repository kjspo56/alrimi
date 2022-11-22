package com.roma.alrimi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("allow_info/basic")
    public String allowBasic(){

        StringBuffer result = new StringBuffer();
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openAPI.seoul.go.kr:8088/"+"인증키"+"/json/YSListPublicReservationSport/1/5/"); //url
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "인증키"); //Service Key
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); //페이지 번호
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); //한 페이지 결과수
            urlBuilder.append("&type=json"); //결과 json 포맷

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result + "";
    }
}
