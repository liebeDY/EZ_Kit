<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>

   <!-- JQuery -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
   <!-- CSS -->
   <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/user_Info.css">
   <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/inc/QnA_SideMenu.css">
   <!-- js -->
   <script src="${pageContext.request.contextPath }/assets/js/admin/user_Info.js"></script>

</head>

<body>

	<!-- top ���� ���� -->
	<c:import url="${pageContext.request.contextPath }/assets/inc/top.jsp" />
	<!-- top ���� �� -->
    
    <c:import url="${pageContext.request.contextPath }/assets/inc/admin_SideMenu.jsp" />
    
    <form id="out_form" method="POST">
    <div class="user_out_div">
        <button class="user_out_btn">Ż�� ó��</button>
    </div>
   <input type="hidden" id="user_id" name="user_id" value="${u.user_id }"/>
    </form>
    
    <form id="search_form" method="POST">
    <div class="user_find_area">
        <div>
            <input class="user_find_text" id="search_id" name="user_id" type="text" placeholder="�˻��� ȸ���� �Է��ϼ���.">
        </div>
        <div class="find_btn_div">
            <button class="find_btn">�˻�</button>
        </div>
    </div>
    </form>

    <div class="user_id_area">
        <div class="user_id_div">
            <div>���̵� : ${u.user_id }</div>
            <div>�г��� : ${u.user_nick }</div>
            <div>��� : <span id="user_rating">${u.user_rating }</span></div>
        </div>

        <div class="rating_btn_div">
            <div>��޺��� : </div>
            <button class="rating_btn" value="Silver">Silver</button>
            <button class="rating_btn" value="Gold">Gold</button>
            <button class="rating_btn" value="Diamond">Diamond</button>
            <button class="rating_btn" value="VIP">VIP</button>
        </div>
    </div>

    <div class="user_point_area">
        <div class="point_div">
            ���� ����Ʈ : 
            <input class="point_text_box" type="text" id="user_point" value="${u.user_point}">P
            <button class="point_del_btn">����Ʈ ȸ��</button>
            <button class="point_edit_btn">����Ʈ ����</button>
        </div>

        <div class="point_btn_area">
            ����Ʈ �߰� : 
            <button class="point_btn" value="100">100P</button>
            <button class="point_btn" value="300">300P</button>
            <button class="point_btn" value="500">500P</button>
            <button class="point_btn" value="1000">1,000P</button>
        </div>

        <div class="point_btn_area">
            ����Ʈ ���� : 
            <button class="point_btn" value="-100">- 100P</button>
            <button class="point_btn" value="-300">- 300P</button>
            <button class="point_btn" value="-500">- 500P</button>
            <button class="point_btn" value="-1000">- 1,000P</button>
        </div>
    </div>

    <div class="user_info_area">

        <table class="user_info_table">

            <tr>
                <th class="user_info_th">��ȭ��ȣ</th>
                <td class="user_info_td">${u.user_tel }</td>
            </tr>

            <tr>
                <th class="user_info_th">�����ȣ</th>
                <td class="user_info_td">${u.user_postcode }</td>
            </tr>

            <tr>
                <th class="user_info_th">�ּ�</th>
                <td class="user_info_td">${u.user_addr1 }</td>
            </tr>

            <tr>
                <th class="user_info_th">���ּ�</th>
                <td class="user_info_td">${u.user_addr2 }</td>
            </tr>

            <tr>
                <th class="user_info_th">���� ������</th>
                <td class="user_info_td">${u.user_regdate }</td>
            </tr>
        </table>
    </div>
</body>
</html>