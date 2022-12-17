package com.roma.alrimi.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

        @GetMapping("/read")
        public void getJsonInfo(){
            System.out.println("Get JSON Data");

            // 인증키 (개인이 받아와야함)
            String key = "";
            // 파싱한 데이터를 저장할 변수
            String result = "";

            try {
                URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key="
                        + key + "&movieCd=20124039");

                BufferedReader bf;

                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                result = bf.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
                JSONObject movieInfoResult = (JSONObject)jsonObject.get("movieInfoResult");
                JSONObject movieInfo = (JSONObject)movieInfoResult.get("movieInfo");

                JSONArray nations = (JSONArray)movieInfo.get("nations");
                JSONObject nations_nationNm = (JSONObject)nations.get(0);

                JSONArray directors = (JSONArray)movieInfo.get("directors");
                JSONObject directors_peopleNm = (JSONObject)directors.get(0);

                JSONArray genres = (JSONArray)movieInfo.get("genres");

                JSONArray actors = (JSONArray)movieInfo.get("actors");

                System.out.println("영화코드 : " + movieInfo.get("movieCd"));
                System.out.println("영화명(한글) : " + movieInfo.get("movieNm"));
                System.out.println("영화명(영문) : " + movieInfo.get("movieNmEn"));
                System.out.println("재생시간 : " + movieInfo.get("showTm"));
                System.out.println("개봉일 : " + movieInfo.get("openDt"));
                System.out.println("영화유형 : " + movieInfo.get("typeNm"));
                System.out.println("제작국가명 : " + nations_nationNm.get("nationNm"));

                String genreNm = "";

                for(int i = 0; i < genres.size(); i++) {
                    JSONObject genres_genreNm = (JSONObject)genres.get(i);
                    genreNm += genres_genreNm.get("genreNm") + " ";
                }

                System.out.println("장르 : " + genreNm);

                System.out.println("감독명 : " + directors_peopleNm.get("peopleNm"));

                String peopleNm = "";

                for(int i = 0; i < actors.size(); i++) {
                    JSONObject actors_peopleNm = (JSONObject)actors.get(i);
                    peopleNm += actors_peopleNm.get("peopleNm") + " ";
                    peopleNm = peopleNm + actors_peopleNm.get("peopleNm") + " ";
                }

                System.out.println("출연배우 : " + peopleNm);

            }catch(Exception e) {
                e.printStackTrace();
            }
        }

}
