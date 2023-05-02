//package Musicalendar.musicalendarproject.service;
//
//import Musicalendar.musicalendarproject.domain.Member;
//import Musicalendar.musicalendarproject.dto.TokenDto;
//import Musicalendar.musicalendarproject.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class KakaoService {
//
//    public TokenDto getKakaoAccessToken(String code){
//
//        String accessToken="";
//        String refreshToken="";
//        String reqURL= "https://kauth.kakao.com/oauth/token";
//
//        try{
//            URL url=new URL(reqURL);
//            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
//
//            // POST 요청 위해 setDoOutput true 로 설정
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            // POST 요청 시 파라미터를 스트림을 통해 전송
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            StringBuilder sb = new StringBuilder();
//            sb.append("grant_type=authorization_code");
//            sb.append("&client_id=c9aa41683e225545c6c18c1c9cb1a791"); // TODO REST_API_KEY 입력
//            sb.append("&redirect_uri=http://localhost:8080/login/kakao");  // 콜백 경로
//            sb.append("&code=" + code);
//            bw.write(sb.toString());
//            bw.flush();
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("KakaoAccessToken responseCode : " + responseCode);
//
//            // JSON타입의 Response 메세지
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = "";
//            String result = "";
//
//            while ((line = br.readLine()) != null) {
//                result += line;
//            }
//            System.out.println("response body : " + result);
//
//            // JSON 파싱 객체 생성
//            JSONParser parser=new JSONParser();
//            JSONObject jsonObject=(JSONObject) parser.parse(result);
//
//            accessToken=(String) jsonObject.get("access_token");
//            refreshToken=(String) jsonObject.get("refresh_token");
//
//            System.out.println("access_token : " + accessToken);
//            System.out.println("refresh_token : " + refreshToken);
//
//            br.close();
//            bw.close();
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//
//        TokenDto tokenDto=new TokenDto(accessToken, refreshToken);
//        return tokenDto;
//    }
//
//    public Map<String, Object> getMemberInfo(String accessToken){
//        HashMap<String, Object> resultMap = new HashMap<>();
//        String reqURL="https://kapi.kakao.com/v2/user/me";
//
//        try {
//            URL url=new URL(reqURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//
//            // 요청 헤더 내용
//            conn.setRequestProperty("Authorization", "Bearer "+accessToken);
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("MemberInfo responseCode : " + responseCode);
//
//            // JSON타입의 Response 메세지 읽어오기
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = "";
//            String result = "";
//
//            while ((line = br.readLine()) != null) {
//                result += line;
//            }
//            System.out.println("response body : " + result);
//
//            // JSON 파싱 객체 생성
//            JSONParser parser=new JSONParser();
//            JSONObject jsonObject=(JSONObject) parser.parse(result);
//
////            log.warn("element:: " + jsonObject);
//
//            JSONObject properties=(JSONObject) jsonObject.get("properties");
//            JSONObject kakaoAccount=(JSONObject) jsonObject.get("kakao_account");
//
//            System.out.println("properties\n============\n"+properties);
//            System.out.println("kakaoAccount\n============\n"+ kakaoAccount);
//
//            String nickname=(String) properties.get("nickname");
//            String email=(String) kakaoAccount.get("email");
//
//            resultMap.put("nickname", nickname);
//            resultMap.put("email", email);
//
//            br.close();
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//
//        return resultMap;
//    }
//
//    public void kakaoLogout(String accessToken){
//        String reqURL = "https://kapi.kakao.com/v1/user/logout";
//
//        try {
//            URL url = new URL(reqURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//
//            // 요청 헤더 내용
//            conn.setRequestProperty("Authorization", "Bearer "+accessToken);
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode : " + responseCode);
//
//            // JSON타입의 Response 메세지 읽어오기
//            // 로그아웃한 멤버의 id 반환
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = "";
//            String result = "";
//
//            while ((line = br.readLine()) != null) {
//                result += line;
//            }
//            System.out.println("response body : " + result);
//
//            br.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
