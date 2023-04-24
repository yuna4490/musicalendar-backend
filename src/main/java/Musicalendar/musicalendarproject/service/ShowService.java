//package Musicalendar.musicalendarproject.service;
//
//import Musicalendar.musicalendarproject.domain.*;
//import Musicalendar.musicalendarproject.repository.ShowRepository;
//import Musicalendar.musicalendarproject.repository.ShowScheduleRepository;
//import lombok.RequiredArgsConstructor;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.Reader;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class ShowService {
//
//    private final ShowRepository showRepository;
//    private final ShowScheduleRepository showScheduleRepository;
//
//    public void saveTicketingData(){
//
//        // 로컬 테스트용
//        readJsonFile("C:\\Users\\82102\\Desktop\\논병아리\\인크\\instagram_extract.json");
////        readJsonFile("/var/current/instagram_extract.json");
//    }
//
//    public void readJsonFile(String filePath){
//
//        JSONParser parser=new JSONParser();
//
//        // JSON 파일 읽기
//        try {
//            Reader reader=new FileReader(filePath);
//            JSONArray jsonArray=(JSONArray) parser.parse(reader);
//
//            parsingData(jsonArray);
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void parsingData(JSONArray jsonArray){
//
////        List<String> img_list=new ArrayList<>();
//        List<ImageEntity> img_list=new ArrayList<>();
//        List<SiteEntity> pre_site_list=new ArrayList<>();
//        List<SiteEntity> site_list=new ArrayList<>();
//
//        // 데이터 파싱
//        for (int i=0; i<jsonArray.size(); i++) {
//            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//            String title = (String) jsonObject.get("title");
//            Category category = Category.valueOf((String) jsonObject.get("category"));
//            String pre_rsv = (String) jsonObject.get("pre_rsv");
//            String rsv = (String) jsonObject.get("rsv");
////            List<String> pre_site = (List<String>) jsonObject.get("pre_site");
//            List<String> pre_site = (List<String>) jsonObject.get("pre_site");
//            if (pre_site!=null){
//                for (Object ps : pre_site) {
//                    pre_site_list.add(new SiteEntity(((String) ps)));
//                }
//            }
////            List<String> site = (List<String>) jsonObject.get("site");
//            List<String> site = (List<String>) jsonObject.get("site");
//            if (site!=null){
//                for (Object s : site) {
//                    site_list.add(new SiteEntity(((String) s)));
//                }
//            }
//            List<String> img = (List<String>) jsonObject.get("img");
//            for (Object s : img) {
////                img_list.add((String) s);
//                img_list.add(new ImageEntity(((String) s)));
//            }
//
//            Show show=checkDuplicateTitle(title, category);
//
//            if (pre_rsv!=null){
////                ShowSchedule preShowSchedule=new ShowSchedule(true, pre_rsv, pre_site, img, show);
//                ShowSchedule preShowSchedule=new ShowSchedule(true, pre_rsv, pre_site_list, img_list, show);
//                showScheduleRepository.save(preShowSchedule);
//            } else{
////                ShowSchedule showSchedule=new ShowSchedule(false, rsv, site, img, show);
//                ShowSchedule showSchedule=new ShowSchedule(false, rsv, site_list, img_list, show);
//                showScheduleRepository.save(showSchedule);
//            }
//        }
//    }
//
//    public Show checkDuplicateTitle(String title, Category category){
//        if (showRepository.existsByTitle(title)){
//            System.out.println("이미 저장되어있는 공연입니다.");
//            return showRepository.findByTitle(title);
//        } else{
//            return showRepository.save(new Show(title, category));
//        }
//    }
//}
