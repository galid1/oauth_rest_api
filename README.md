# oauth_login_rest_api
- social 로그인 api를 이용해 (google, kakao, naver)에 등록된 유저의 email을 가져오는 예제입니다.
<br><br>

### Usage
1. 각 social api 사이트에서 app을 생성한 뒤 REDIRECT_URI와 CLIENT_ID(social사 별로 api_key라고도 함)를 가져와
   kotlin/com/galid/oauth_login_rest_api/constants/Constants에 기입합니다.
2. 브라우저에 http://localhost:8080/oauth/{social_name} 으로 요청합니다.
   (e.g. http://localhost:8080/oauth/naver)
  
