<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
   <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/admin/inquiry_AdminWriting.css">
   <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/inc/QnA_SideMenu.css">
   <!-- JQuery -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>

	<!-- top 영역 시작 -->
	<c:import url="${pageContext.request.contextPath }/assets/inc/top.jsp" />
	<!-- top 영역 끝 -->

	<c:import url="${pageContext.request.contextPath }/assets/inc/admin_SideMenu.jsp" />
   
    <div class="QnA_admin_view_user_area">
        <div class="view_title_div">
            <div class="num_div">${i.inquiry_num }</div>
            <div class="title_div">${i.inquiry_title }</div>
            <div class="user_id_div">${u.user_nick }</div>
            <div class="date_div">${i.inquiry_date }</div>
        </div>
        
        <div class="user_content_div">
            <textarea class="in_query_content" disabled>${i.inquiry_content }</textarea>
        </div>
    </div>

    <form method="POST" action="/admin/inquiry_AdminWriting">
        <div class="QnA_admin_view_admin_area">
            <div class="in_query_label">※ 문의 답변하기</div>
            <div class="in_query_div">
                <textarea class="in_query_content" name="inquiry_reply" placeholder="답변할 내용을 입력하세요."></textarea>
            </div>
            <div class="in_querywriting_div">
                <button class="in_querywriting_btn">작성완료</button>
            </div>
        </div>
        <input type="hidden" name="inquiry_num" value="${i.inquiry_num }"/>
      <input type="hidden" name="inquiry_title" value="${i.inquiry_title }"/>    
    </form>
    
</body>
</html>