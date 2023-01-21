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

</head>

<div>
    <h1>로그인</h1>
    <a href="javascript:kakaoLogin()"><img src="/el/resources/img/member/kakao.png" style="width: 200px"></a>
    <button>네이버 로그인</button>
</div>



<script type="text/javascript" src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script type="text/javascript">
    Kakao.init('83eaa8e14588c0d880e90900bad36a53');
    function kakaoLogin() {
        Kakao.Auth.login({
            success: function (response) {
                Kakao.API.request({
                    url: '/v2/user/me',
                    success: function (response) {
                        alert(JSON.stringify(response))
                        let kakao = JSON.stringify(response);
                        console.log(kakao);
                        // location.href = '/el/main';
                        $.ajax({
                          url : "/el/member/login"
                          ,type : "post"
                          ,data : { 
                            "kakao" : kakao
                          }
                          ,success : function name(params) {
                            alert('성공')
                          }
                          ,error : function name(params) {
                            alert('실패')
                          }
                        
                        });

                    },
                    fail: function (error) {
                        alert(JSON.stringify(error))
                    },
                })
            },
            fail: function (error) {
                alert(JSON.stringify(error))
            },
        })
    }
</script>

</body>
</html>