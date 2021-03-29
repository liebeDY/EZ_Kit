<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>


   <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/inc/top.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/inc/footer.css">
    
   <!-- Jquery -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    
    <!-- js -->
    <script type="text/javascript" src="${pageConext.request.contextPath }/assets/js/index_img.js"></script>

   <script type="text/javascript">
//       $(document).ready(function() {
//             // 장바구니 버튼 눌렀을 때
//             $(".cart_btn").click(function() {
//                var btn = $(this);
//                console.log(btn);
//                var product_num = $(btn).nextAll()[1];
//                product_num = $(product_num).val();
//                console.log(product_num);
//                var user_id = $("#user_id").val();
//                console.log(user_id);
               
//                if (user_id == "" || user_id == undefined) {
//                   console.log("if로 들어왔다");
//                alert("로그인 후 이용이 가능합니다");
//                $(".cart_add_form").attr('action', '/user/login');
//                $(".cart_add_form").submit();
//                } else {
//                   $.post("/order/cart_Insert", {
//                      user_id : user_id,
//                      product_num : product_num
//                   }) 
//                   .done(function(data) {
//                     var data = eval("(" + data + ")");
                    
//                     if (data.result == 1) {
//                         alert("상품이 장바구니에 담겼습니다");
//                     } else if (data.result == 2) {
//                        alert("이미 담겨있는 상품입니다");
//                     }
//                   });
//                }
//             });
//       });
   </script>
</head>
<body>
	<div class="top_bar_area">
        <div class="user_util_div">
            <c:if test="${empty sessionScope.user_type }">
                <a class="user_util_link" href="${pageContext.request.contextPath }/user/login">로그인</a>
                <a class="user_util_link" href="${pageContext.request.contextPath }/user/agreement">회원가입</a>
            </c:if>

             <c:if test="${sessionScope.user_type == 'user' }">
                ${sessionScope.user_nick} 회원님 환영합니다.
                <a class="user_util_link" href="${pageContext.request.contextPath }/user/logout" onClick="alert('로그아웃 되었습니다');">로그아웃</a>
            </c:if>

            <c:if test="${sessionScope.user_type == 'admin' }">
                ${sessionScope.admin_nick} 회원님 환영합니다.
                <a class="user_util_link" href="${pageContext.request.contextPath }/user/logout" onClick="alert('로그아웃 되었습니다');">로그아웃</a>
                <a class="user_util_link" href="${pageContext.request.contextPath }/product/product_Add"> 제품등록 </a>
                <a class="user_util_link" href="${pageContext.request.contextPath }/admin/main"> 관리자페이지 </a>
            </c:if> 

            <a class="user_util_link" href="${pageContext.request.contextPath }/qna/QnA_Main">고객센터</a>
        </div>

        <div class="user_menu_div">
            <div class="top_bar_logo_div">
                <a class="top_menu_link" href="${pageContext.request.contextPath }/">
                    <img class="EZ_Kit_logo_set" src="${pageContext.request.contextPath }/assets/img/logo/EZ_Kit_logo.png">
                </a>
            </div>
            <div class="EZKit_info_div">
                <a class="top_menu_link" href="">EZKit 소개</a>
            </div>
            <div class="menu_list_div">
                <a class="top_menu_link" href="${pageContext.request.contextPath }/product/product_list">메뉴</a>
            </div>
            <div class="notice_div">
                <a class="top_menu_link" href="${pageContext.request.contextPath }/admin/notice_List">공지사항</a>
            </div>

	<c:choose>
		<c:when  test="${empty sessionScope.user_type }">
			<div class="my_profile_div">
				<a class="top_menu_link" href="${pageContext.request.contextPath }/user/login" onClick="alert('로그인 후 이용가능합니다.');">내정보</a>
			</div>
			<div class="cart_div">
				<a class="top_menu_link" href="${pageContext.request.contextPath }/user/login" onClick="alert('로그인 후 이용가능합니다.');">장바구니</a>
			</div>
		</c:when>

		<c:when test="${user_type == 'user' }">
			<div class="my_profile_div">
				<a class="top_menu_link" href="${pageContext.request.contextPath }/user/userProfile?user_id=${sessionScope.user_id}">내정보</a>
			</div>	
			<div class="cart_div">	
				<a class="top_menu_link" href="${pageContext.request.contextPath }/order/cart_list?user_id=${sessionScope.user_id }">장바구니</a>
			</div>
		</c:when>
		
		<c:when test="${user_type =='admin' }">
			<div class="my_profile_div">
				<a class="top_menu_link" href="#" onClick="alert('관리자는 정보를 수정할 수 없습니다.'); return false;">내정보</a>
			</div>	
			<div class="cart_div">	
				<a class="top_menu_link" href="#" onClick="alert('관리자는 장바구니를 이용할 수 없습니다.'); return false;">장바구니</a>
			</div>
		</c:when>
	</c:choose>
            <div class="find_menu_div">
                <a class="top_menu_link" href="${pageContext.request.contextPath }/product/findProduct">메뉴찾기</a>
            </div>
        </div>
    </div>
</body>
</html>