<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/el/resources/css/member/login.css" rel="stylesheet" type="text/css">
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
</head>
<body>
	<c:if test="${error != null}">
		<script type="text/javascript">
			alert("아이디와 비밀번호를 다시 한 번 확인해주세요.")
		</script>
	</c:if>
	
    <br><br><br><br><br>
    <hr>
    <div id="contain">
        <!-- <div id="top-join"> -->
            <div>
                <div class="top-join"><img id="img-logo" src="/el/resources/img/logo/el.png" alt=""></div>
            </div>

            <div id="wrap-join">
                
                <form method="post" id="join-form" onsubmit="loginOk()">

                <h4><label for="">아이디</label></h4>
                <span class="join-box">
                    <input type="text" name="memberId">
                </span>            
                <h4><label for="">비밀번호</label></h4>
                <span class="join-box">
                    <input type="password" name="memberPwd">
                </span>
                <div id="btn-join">
                    <button type="submit" id="btnJoin">
                        <div id="text-join">로그인</div>
                    </button>
                </div>
                
                </form>

                
                
                <script type="text/javascript">
                    function loginOk() {
                        return true;
					}
                    </script>
                
                
            </div>

            <ul>
                <li onclick="kakaoLogin();">
                  <a href="javascript:void(0)">
                      <span>카카오 로그인</span>
                  </a>
                </li>
                <li onclick="kakaoLogout();">
                  <a href="javascript:void(0)">
                      <span>카카오 로그아웃</span>
                  </a>
                </li>
            </ul>

            
            <script>
            Kakao.init('83eaa8e14588c0d880e90900bad36a53'); //발급받은 키 중 javascript키를 사용해준다.
            console.log(Kakao.isInitialized()); // sdk초기화여부판단
            //카카오로그인
            function kakaoLogin() {
                Kakao.Auth.login({
                  success: function (response) {
                    Kakao.API.request({
                      url: '/v2/user/me',
                      success: function (response) {
                          console.log("response" , response)
                      },
                      fail: function (error) {
                        console.log(error)
                      },
                    })
                  },
                  fail: function (error) {
                    console.log(error)
                  },
                })
              }
            //카카오로그아웃  
            function kakaoLogout() {
                if (Kakao.Auth.getAccessToken()) {
                  Kakao.API.request({
                    url: '/v1/user/unlink',
                    success: function (response) {
                        console.log(response)
                    },
                    fail: function (error) {
                      console.log(error)
                    },
                  })
                  Kakao.Auth.setAccessToken(undefined)
                }
              }  
            </script>

            <div id="find">
                <a href="http://127.0.0.1:8888/el/member/idFind">아이디 찾기</a> | <a href="http://127.0.0.1:8888/el/member/pwFind">비밀번호 찾기</a> | <a href="http://127.0.0.1:8888/el/member/join">회원가입</a>
            </div>
    </div>

</body>
</html>