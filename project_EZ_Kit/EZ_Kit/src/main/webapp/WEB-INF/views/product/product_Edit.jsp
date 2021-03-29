<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

	<!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/product/product_Add.css">
    
    <!-- Jquery -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    
	<!-- js -->
    <script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/product/product_Edit.js"></script>

</head>

<body>
      <form method="post" enctype="multipart/form-data" id="product_add_form">

        <input type="file" name="product_main_img" style="display:none" accept="image/*" onchange="setPreview(event);" multiple required />
   		<input type="file" name="product_detail_img" style="display:none" accept="image/*" onchange="setPreviewDetail(event);" required />

        <div class="product_add_area">
            <div class="header_text_div">상품 수정</div>
            <div class="main_img_add_label">상품 이미지 등록 (최대 4개)</div>
            <div class="main_img_area">
                <div class="main_img_view_div" id="main_img_view_div">
                    <!-- 이미지 영역 -->
                </div>
                
                <div class="main_img_add_btn" onclick="document.all.product_main_img.click()">
                    <div class="add_btn_in_set">
                        <img src="${pageContext.request.contextPath }/assets/img/camera_icon.png">
                        <div class="btn_text">이미지 등록</div>
                    </div>
                </div>
            </div>

            <div class="product_name_div">
                <div class="product_name_label">상품명 : </div>
                <input class="product_name_text" type="text" name="product_name" value="${p.product_name }" placeholder="상품명 입력">
            </div>

            <div class="product_price_div">
                <div class="product_price_label">가격 : </div>
                <input class="product_price_text" type="text" name="product_price" value="${p.product_price }" placeholder="가격 입력">
            </div>

            <div class="prdouct_quantity_div">
                <div class="prdouct_quantity_lebel">상품 수량 : </div>
                <input class="prdouct_quantity_text" type="text" name="product_quantity" value="${p.product_quantity }" placeholder="수량 입력">
            </div>

            <div class="product_classification_div">
                <div class="product_classification_label">분류 : </div>
                <div class="classification_radio_btn">
                    <input class="radio_btn" type="radio" name="product_classification" value="한식"> 한식
                    <input class="radio_btn" type="radio" name="product_classification" value="양식"> 양식
                    <input class="radio_btn" type="radio" name="product_classification" value="중식"> 중식
                    <input class="radio_btn" type="radio" name="product_classification" value="일식"> 일식
                    <input class="radio_btn" type="radio" name="product_classification" value="분식"> 분식
                </div>
            </div>

            <div class="cooking_info_div">
                <div class="cooking_info_label">조리 정보 : </div>
                <div class="cooking_info_1">식수 <input class="cooking_info_text" type="text" name="product_portion" value="${p.product_portion }" placeholder="1"> 인분</div>
                <div class="cooking_info_2">조리 시간 <input class="cooking_info_text" type="text" name="product_time" value="${p.product_time }" placeholder="10"> 분</div>
                <div class="cooking_info_3">
                    조리 난이도 :
                    <input class="radio_btn" type="radio" name="product_level" value="상"> 상
                    <input class="radio_btn" type="radio" name="product_level" value="중"> 중
                    <input class="radio_btn" type="radio" name="product_level" value="하"> 하
                </div>
            </div>

            <div class="product_detail_div">
                <div class="product_detail_label">상품 상세정보</div>
                <div class="product_detail_text_area">
                    <textarea class="product_detail_text" id="product_detail_text" name="product_info" placeholder="상세 정보를 작성하세요">${p.product_info }</textarea>
                    <div class="info_img_add_btn" onclick="document.all.product_detail_img.click()">
                        <div class="add_btn_in_set">
                            <img id="info_img_view" src="${pageContext.request.contextPath }/assets/img/camera_icon.png">
                            <div class="btn_text">상세 이미지 등록</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="add_product_btn_div">
                <input class="cancel_btn" type="button" value="수정 취소">
                <input class="add_product_btn" type="button" value="제품 수정">
            </div>
        </div>
        <input type="hidden" name="product_num" value="${p.product_num }" />
    </form>
</body>


</html>