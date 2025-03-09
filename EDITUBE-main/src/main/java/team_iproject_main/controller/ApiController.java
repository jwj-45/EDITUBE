package team_iproject_main.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team_iproject_main.data.dto.JusoDto;
import team_iproject_main.data.dto.YoutubeChannel;
import team_iproject_main.data.request.GoogleOAuthRequest;
import team_iproject_main.data.request.RequestId;
import team_iproject_main.data.response.GoogleLoginResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@Controller
public class ApiController {

    @Value("${mygoogle.apiKey}")
    private String apiKey;

    @Value("${mygoogle.clientid}")
    private String CLIENT_ID ;

    @Value("${mygoogle.clientsecret}")
    private String CLIENT_SECRETS;

    @Value("${mygoogle.clientredirectyoutube}")
    private String clientredirectyoutube;

    @Value("${mygoogle.authurl}")
    private String authUrl;

    @GetMapping("address-pop")
    public String addressPop(){
        return "addresspop";
    }

    @GetMapping("/address-search")
    public String addressSearch(){
        return "addressSearch";
    }

    @GetMapping(value = "/road-api")
    public ResponseEntity<Object> rodeApi() {
        String authUrl = "https://business.juso.go.kr/addrlink/addrLinkUrl.do?confmKey=U01TX0FVVEgyMDIzMDUwMjE0MTI1MDExMzczNzM=&returnUrl=http://localhost:3030/road-return&resultType=4";
        URI redirectUri;

        System.out.println("요청 url : "+ authUrl);

        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);

            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }


    @PostMapping(value = "/road-return")
    @CrossOrigin(origins="*")
    public String postRodeReturn(HttpServletRequest request,
                                 @RequestBody String body,
                                 Model model) throws JsonProcessingException, UnsupportedEncodingException {

        System.out.println("포스트");
        System.out.println("headers : "+ request.getHeaderNames());
        System.out.println("body : "+ body);

        // ObjectMapper를 통해 String to Object로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)

        System.out.println("decode : "+ URLDecoder.decode(body, "UTF-8"));

        // URL 디코딩
        String decoded = URLDecoder.decode(body, "UTF-8");

        // 파라미터 분리
        String[] params = decoded.split("&");

        // key-value 쌍으로 저장할 Map 생성
        Map<String, String> paramMap = new HashMap<>();

        // 각 파라미터의 값을 Map에 저장
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                paramMap.put(keyValue[0], keyValue[1]);
            }
        }

        JusoDto jusoDto = new JusoDto();

        jusoDto.setInputYn(paramMap.get("inputYn"));
        jusoDto.setRoadFullAddr(paramMap.get("roadFullAddr"));

        System.out.println("최종:");
        System.out.println(jusoDto.getRoadFullAddr());
        System.out.println(jusoDto.getInputYn());

        System.out.println(paramMap.values());

        model.addAttribute("address", jusoDto.getRoadFullAddr());

        return "join";
    }


    @GetMapping(value = "answer")
    public ResponseEntity youtubeCheck(){
        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=profile%20email%20openid%20https://www.googleapis.com/auth/youtube.readonly" +
                "&client_id="+CLIENT_ID+
                "&redirect_uri="+clientredirectyoutube+
                "&response_type=code&mine=true";
        URI redirectUri;

        System.out.println("요청 url : "+ authUrl);

        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            log.info("check login");
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    // 로그인 성공후 , 코드
    @GetMapping(value = "/oauth/youtube/redirect")
    public String redirectGoogleYoutube(
            @RequestParam(value = "code") String authCode,
            Model model
    ) {
        // HTTP 통신을 위해 RestTemplate 활용

        RestTemplate restTemplate = new RestTemplate();

        // 이 템플릿을 통해 전달할때 필요한 자료를 담아줄려고 만든 클래스
        GoogleOAuthRequest requestParams = GoogleOAuthRequest.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRETS)
                .code(authCode)
                .redirectUri(clientredirectyoutube)
                .grantType("authorization_code")
                .build();

        log.info("login try....");
        log.info(requestParams.getCode());

        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleOAuthRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);

            // Post 요청
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(this.authUrl + "/token", httpRequestEntity, String.class);

            System.out.println("크리덴셜 확인");
            System.out.println(apiResponseJson.getBody());

            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponse>() {});

            // 사용자의 정보는 JWT Token 으로 저장되어 있고, Id_Token 에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            String accessToken = googleLoginResponse.getAccessToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(authUrl + "/tokeninfo")
                                    .queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            // Authorization 헤더에 Bearer 토큰 포함시켜서 요청
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

//          uri 잘못 입력하거나 유지보수 좀 더 편하게 String 으로 직접 작성안하고
//          UriComponentBuilder 사용해서 직관적으로 uri 작성
            String reqUrl = UriComponentsBuilder.fromHttpUrl("https://youtube.googleapis.com/youtube/v3/channels")
                                .queryParam("part", "snippet,contentDetails,statistics")
                                .queryParam("mine", true)
                                .queryParam("key", apiKey)
                            .toUriString();

            // 채널 정보 요청
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    reqUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("response : {}", response.getBody());

//          response 반환값이 바디에 있는 값 그대로 갖고옴 readValue 에서 오류나기 때문에 바디영역 String 값으로 변환
//          httpEntity 에서 String 으로 지정
            String responseBody = response.getBody();

            ObjectMapper mapper = new ObjectMapper();

            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            YoutubeChannel channelList = mapper.readValue(responseBody, YoutubeChannel.class);
//          readValue -> Json 값을 String 으로 받아서 YoutubeChannel 객체로 변환해주는 메서드
//          String 값이 아니면 JsonParseException 예외 뜬다.
//          YoutubeChannelList 정상 작동 but, JsonIgnoreProperties 썼는데 YoutubeChannel 객체는 왜 에러? => 필요한곳에 안써서 에러

            System.out.println("변환 결과값");
            System.out.println(channelList);
            System.out.println(channelList.toString());


            if(channelList.getItems().equals("")) {
                return "signup_youtuber";
            }

            if(channelList.getItems().size() >0){
                System.out.println("원하는 값 id : "+ channelList.getItems().get(0).getId());
            }


            if(resultJson != null) {
                RequestId requestId = new RequestId();
                requestId.setChannelId(channelList.getItems().get(0).getId());
                requestId.setSubscribe((long) channelList.getItems().get(0).getStatistics().getSubscriberCount());
                requestId.setVideoCount((long) channelList.getItems().get(0).getStatistics().getVideoCount());
                requestId.setViewCount((long) channelList.getItems().get(0).getStatistics().getViewCount());
                requestId.setChannelName(channelList.getItems().get(0).getSnippet().getTitle());
                requestId.setChannelPhoto(channelList.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());


                model.addAttribute("channelId",requestId.getChannelId());
                model.addAttribute("subscribe",requestId.getSubscribe());
                model.addAttribute("videoCount",requestId.getVideoCount());
                model.addAttribute("viewCount",requestId.getViewCount());
                model.addAttribute("channelName",requestId.getChannelName());
                model.addAttribute("channelPhoto", requestId.getChannelPhoto());
                model.addAttribute("channel_certificate_button",true);
                model.addAttribute("channel_photo_subscribe",false);
                model.addAttribute("channel_errorMsg_hidden", true);
                return "signup_youtuber";
            }
            else {
                throw new Exception("Google OAuth failed!");

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("channel_certificate_button",false);
        model.addAttribute("channel_photo_subscribe", true);
        model.addAttribute("channel_errorMsg", "채널 인증이 되지 않았습니다.");
        model.addAttribute("channel_errorMsg_hidden", false);

        return "signup_youtuber";
    }

    @GetMapping("/oauth/youtube/answer")
    public String clearSession(HttpSession session) {
        session.invalidate();
        return "redirect:/signup_youtuber";
    }
}
