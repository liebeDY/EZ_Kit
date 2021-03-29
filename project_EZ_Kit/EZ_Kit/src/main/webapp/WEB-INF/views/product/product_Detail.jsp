<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>

	<!-- CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.1/css/lightbox.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/product/product_Detail.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/inc/top.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/inc/footer.css">    

	<!-- Jquery -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>

    <!-- 사진 확대 라이브러리 : lightbox -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.1/js/lightbox.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/product/product_Detail.js"></script>

	<script type="text/javascript">
	
 		$(document).ready(function() {
 			
			$(".review_writing_btn").click(function() {
				var user_nick = $("#user_nick").val();
				var user_type = $("#user_type").val();
				var review_content = $("#review_content").val();
				var product_num = $("#product_num").val();
				
				
				$.post("/review/insertReview", {
					user_nick : user_nick,
					review_content : review_content,
					user_type : user_type,
					product_num : product_num
				})
				.done(function(data) {
					var data = eval("(" + data + ")");
					var str = "";
					
					if (data.review_content == "") {
						alert("리뷰를 입력해주세요");
						return false;
					} else {
						for (i = 0; i < data.length; i++) {
							var writeonly="";
								
							if(user_nick != data[i].user_nick && user_type != 'admin'){
								writeonly = "disabled";
							}
							if(user_type == "" || user_type == null || user_type == undefined){
								writeonly = "disabled";
							}
							
							if (data[i].user_type == 'admin') {
								str += "<tr>"
									str += "<td class='seller_review_td'>"
									str += "<textarea class='seller_review_text' id='" + data[i].review_num + "'" + writeonly+ ">" + data[i].review_content + "</textarea>"
									str += "</td>"
									str += "<td class='seller_review_info'>"
									str += "<form method='POST'>"
									str += "<div class='seller_nick'> 작성자 : <span class='seller_nick_span'>EZ kit</span></div>"
									str += "<div class='seller_review_date'> 작성일 : " + data[i].review_date + "</div>"
									str += "<div class='seller_review_btn_div'>"
									if (user_type == 'admin') {
										str += "<input class='order_review_del_btn' type='button' value='삭제' onClick='$.fn.review_del(this.form);'>"
										str += "<input class='order_review_edit_btn' type='button' value='수정' onClick='$.fn.review_edit(this.form);'>"
										str += "<input type='hidden' name='review_num' value='" + data[i].review_num +"'>"
										str += "<input type='hidden' name='review_content'>"
									}
									str += "</div>"
									str += "</form>"
									str += "</td>"
									str += "</tr>"
									
							} else {
									str += "<tr>"
									str += "<td class='order_review_td'>"
									str += "<textarea class='order_review_text' id='" + data[i].review_num + "'" + writeonly+ ">" + data[i].review_content + "</textarea>"
									str += "</td>"
									str += "<td class='order_review_info'>"
									str += "<form method='POST'>"
									str += "<div class='order_nick'> 작성자 : <input class='order_nick_btn' type='button' value='" + data[i].user_nick + "'</div>"
									str += "<div class='order_review_date'> 작성일 : " + data[i].review_date + "</div>"
									str += "<div class='order_review_btn_div'>"
									if (user_nick == data[i].user_nick || user_type == 'admin') {
										str += "<input class='order_review_del_btn' type='button' value='삭제' onClick='$.fn.review_del(this.form);'>"
										str += "<input class='order_review_edit_btn' type='button' value='수정' onClick='$.fn.review_edit(this.form);'>"
										str += "<input type='hidden' name='review_num' value='" + data[i].review_num +"'>"
										str += "<input type='hidden' name='review_content'>"
									}
									str += "</div>"
									str += "</form>"
									str += "</td>"
									str += "</tr>"
							}
						}
					}
						$("#review_content").val("");
					    $("#review_content_table").html(str);
			            // 리뷰 작성이 완료되면 스크롤을 하단으로 내린다.
			            $('html, body').scrollTop(document.body.scrollHeight);
					});
				});
			
			// 댓글 삭제		
			$.fn.review_del = function(f){
				var con = confirm("댓글을 삭제하시겠습니까?");
                
                if (con == true) {
              	alert("댓글이 삭제되었습니다");
					f.action = '/review/deleteReview';
	                f.submit();
                }
			}
			
			// 댓글 수정
			$.fn.review_edit = function(f){
				var con = confirm("댓글을 수정하시겠습니까?");
				//댓글번호
                var idx = f.review_num.value;
                //댓글번호에 해당하는 textarea
                var review_content = document.getElementById(idx).value;
                
                f.review_content.value = review_content;
                
				  if (con == true) {
					  alert("댓글이 수정되었습니다")
					  f.action = '/review/updateReview';
					  f.submit();
				  }
			}

		});
	</script> 
</head>

<body>

	<!-- top 영역 시작 -->
	<c:import url="${pageContext.request.contextPath }/assets/inc/top.jsp" />
	<!-- top 영역 끝 -->

    <div class="warp_btn">
        <div><input class="top_warp_btn" type="button" value="▲"></div>
        <div><input class="bottom_warp_btn" type="button" value="▼"></div>
    </div>
    <table class="product_detail_table">
        <tr>
            <td class="detail_img_td">
               <a class="detail_img_set_a" data-lightbox="img_sizeup" href="${pageContext.request.contextPath }/productImg?fname=${filesname[0] }&product_num=${p.product_num }&type=1">
                      <img class="detail_img_set" data-lightbox="img_sizeup" src="${pageContext.request.contextPath }/productImg?fname=${filesname[0] }&product_num=${p.product_num }&type=1">
                </a>
                <div class="detail_sub_img_div">
   <c:forEach var="i" items="${filesname }">
                     <img class="detail_sub_img_set" src="${pageContext.request.contextPath }/productImg?fname=${i }&product_num=${p.product_num }&type=1" />
   <!--                     <img class="detail_sub_img_set" src="../img/s1.gif"> -->
   <!--                     <img class="detail_sub_img_set" src="../img/s2.gif"> -->
   <!--                     <img class="detail_sub_img_set" src="../img/s3.gif"> -->
   <!--                     <img class="detail_sub_img_set" src="../img/s4.gif"> -->
               
</c:forEach>
                </div>
            </td>
            <td class="detail_info_td">
                <div class="detail_title_div"> ${p.product_name }</div>

                <div class="cooking_info_div">
                    조리 정보 : 
                    <span class="cooking_info_span">${p.product_portion }인분 / ${p.product_time }분</span>
                </div>

                <div class="price_div">
                    판매가 : 
                    <span class="price_span"> ${p.product_price }원</span>
                </div>

                <div class="point_div">포인트 적립 : 
                    <span class="point_span">EZ Kit 포인트 0.2% 적립</span>
                </div>

                <div class="quantity_btn_div">
                    수량 : 
                    <span class="quantity_btn_span">
                        <input class="quantity_minus_btn quantityBtn" type="button" value="-">
                        <input class="quantity_result" data-max-quantity="${p.product_quantity }" id="quantity_result" type="button" value="1">
                        <input class="quantity_plus_btn quantityBtn" type="button" value="+">
                    </span>

                    <span class="quantity_result_span"></span>

                </div>

                <div class="cart_btn_div">
                    <form id="cart_add_form">
                    	<c:if test="${sessionScope.user_type == 'user' }">
		                    <input class="recipe_btn" type="button" value="조리방법 보기">
	                    	<input class="cart_btn" type="button" value="장바구니 담기" />
                    	</c:if>
                    	
                    	<c:if test="${empty sessionScope.user_type }">
							<input class="recipe_btn" type="button" value="조리방법 보기">
	                    	<input class="cart_btn" type="button" value="장바구니 담기" />
                    	</c:if>
                    	
                    	<c:if test="${sessionScope.user_type == 'admin' }">
                    		<input class="edit_btn" type="button" id="edit_btn" value="상품 수정하기">
	                    	<input class="del_btn" type="button" id="del_btn" value="상품 삭제하기" />
                    	</c:if>
						<input type="hidden" name="user_id" id="user_id" value="${sessionScope.user_id }" />
                		<input type="hidden" name="product_num" id="product_num" value="${p.product_num }" />
                		<!-- 수량 value에 입력하기 -->
<!--                 		<input type="hidden" name="product_quantity" value="" /> -->
                    </form>
                </div>

            </td>
        </tr>

        <!-- 상세 이미지, 상세내용 영역 -->
        <tr>
            <td colspan="2">
                <div class="info_img_div">
                    <img class="info_img_set" src="${pageContext.request.contextPath }/productImg?fname=${detailImg }&product_num=${p.product_num }&type=2">
                </div>

                <div class="info_text_div">
                    <textarea class="info_text" name="product_info" disabled>${p.product_info }</textarea>
                </div>
            </td>
        </tr>
    </table>
    
    <!-- 리뷰 영역 -->
    <form method="POST" class="insert_review_form">
	    <table class="review_table">
	        <tr>
	            <td class="review_writing_td">
	                <textarea class="review_writing_text" name="review_content" id="review_content" placeholder="리뷰를 작성해보세요!"></textarea>
	            </td>
	            <td class="review_writing_btn_td">
	                <input class="review_writing_btn" type="button" value="리뷰 작성">
	            </td>
	        </tr>
	    </table>
		<c:if test="${sessionScope.user_type == 'user' }">
	    	<input type="hidden" id="user_nick" value="${sessionScope.user_nick }" />
		</c:if>
		<c:if test="${sessionScope.user_type == 'admin' }">
	    	<input type="hidden" id="user_nick" value="EZKit" />
		</c:if>
	    <input type="hidden" id="product_num" value="${p.product_num }" />
	    <input type="hidden" name="user_type" id="user_type" value="${sessionScope.user_type }" />
    </form>
    
    <table class="review_content_table" id="review_content_table">
<%-- 		<c:if test="${empty list }"> --%>
<!-- 					등록된 리뷰가 없습니다. 리뷰를 작성해주세요! :)	 -->
<%-- 		</c:if> --%>
		
		<c:if test="${not empty list2 }">
				 
			<c:forEach var="r" items="${list2 }">
			<c:set var="writeonly">
					<c:choose>
					<c:when test="${sessionScope.user_nick != r.user_nick && sessionScope.user_type != 'admin' }">
						disabled
					</c:when>
					<c:when test="${empty sessionScope.user_type }">
						disabled
					</c:when>
				</c:choose>
			</c:set>
		        <tr>
		            <td class="order_review_td">
		                <textarea class="order_review_text" id="${r.review_num }" ${ writeonly}>${r.review_content }</textarea>
		            </td>
		            <td class="order_review_info">
						<form method="POST">
							<c:if test="${r.user_nick == 'EZKit' }">
		               			<div class="seller_nick">작성자 : <span class="seller_nick_span">EZ Kit</span></div>
		                	</c:if>
		                	<c:if test="${r.user_nick != 'EZKit' }">
		               			<div class="order_nick">작성자 :<input class="order_nick_btn" type="button" value="${r.user_nick }"></div>
		                	</c:if>
		                	<div class="order_review_date">작성일 : ${r.review_date }</div>
		              	  	<div class="order_review_btn_div">
			               		<c:if test="${sessionScope.user_nick == r.user_nick || sessionScope.user_type == 'admin' }">
				            		<input class="order_review_del_btn" type="button" value="삭제" onClick="$.fn.review_del(this.form);">
				            		<input class="order_review_edit_btn" type="button" value="수정" onClick="$.fn.review_edit(this.form);">
			                	</c:if>
			                	<input type="hidden" name="review_num" value="${r.review_num }" />
			                	<input type="hidden" name="review_content" />
			                </div>
					   </form>
		            </td>
		        </tr>
			</c:forEach>
		</c:if>

    </table>
    <!-- 상품 수량 -->
    <input type="hidden" id="product_quantity" name="" value="10">


	<!-- footer 영역 시작 -->
	<c:import url="${pageContext.request.contextPath }/assets/inc/footer.jsp" />
	<!-- footer 영역 끝 -->
</body>


</html>