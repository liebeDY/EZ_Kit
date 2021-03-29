$(document).ready(function() {



   // 페이지 시작하면 금액 정규화
   var quantity = Number($("#quantity_result").val());
   var product_price = $(".price_span").text();
   product_price = Number(product_price.replace("원", ""));
   var result = quantity * product_price;
   product_price = product_price.toLocaleString();
   result = result.toLocaleString();

   $(".price_span").text(product_price + " 원");
   $(".quantity_result_span").text(result + " 원");

   // 이미지 관련 변수
   var mainImg = $(".detail_img_set");
   var mainImgA = $(".detail_img_set_a");


   // 위 아래 버튼
   $(".top_warp_btn").bind('click', function() {
      $('html, body').animate({ scrollTop: '0' }, 600);
   });

   $(".bottom_warp_btn").on('click', function() {
      $("html, body").animate({ scrollTop: $(document).height() }, 600);
   });

	lightbox.option({
         resizeDuration: 800,
         wrapAround: false,
         disableScrolling: false,
         fitImagesInViewport: false,
         maxHeight: 400
    });
   // 메인 사진 클릭
//   $(".detail_img_set_a").click(function() {
//
//      // 메인 사진 클릭시 확대 옵션 : lightbox 라이브러리
//      
//   })

   // 서브 사진 클릭 : 메인사진 바꾸기
   $(".detail_sub_img_set").click(function() {
      var subImg = $(this).attr("src");
      console.log(subImg)
      $(mainImg).attr("src", subImg)
      $(mainImgA).attr("href", subImg)

   });


 $(".quantityBtn").click(function() {
      console.log('.quntityBtn 시작')
      var btn = $(this);
      console.log(btn)
      var btnValue = $(btn).val();
      console.log(btnValue)
      var priceSpan = $(".price_span");
      console.log(priceSpan)
      var price = $(priceSpan).text().replace("원","").replace(",","");
      console.log(price)
      price = Number(price)
      var quantityArea = $(".quantity_result")
      var quantityResult = Number($(".quantity_result").val());
      var maxQuantity = $(".quantity_result").data("max-quantity");
      var priceResult = 0;
      if (btnValue === "-") {
         if(quantityResult <= 1) {
            alert("주문가능한 최소 수량입니다")
            return false
         } else {
            quantityResult = quantityResult - 1
         }
         $(quantityArea).val(quantityResult)
         priceResult = price * quantityResult;
         priceResult = priceResult.toLocaleString()
         $(".quantity_result_span").text(priceResult + " 원");
      } else if (btnValue === "+") {
         if(quantityResult >= maxQuantity) {
            alert("주문가능한 최대 수량입니다")
            return false
         } else {
            quantityResult = quantityResult + 1
            $(quantityArea).val(quantityResult)
            priceResult = price * quantityResult;
            priceResult = priceResult.toLocaleString()
            $(".quantity_result_span").text(priceResult + " 원");
         };
      };
   });


   // 장바구니 버튼 눌렀을 때
   $(".cart_btn").click(function() {
      var btn = $(this)
      var user_id = $("#user_id").val();
      var product_num = $("#product_num").val();
      var product_quantity = $("#quantity_result").val();
      console.log(product_quantity)
      if (user_id == "" || user_id == undefined) {
         alert("로그인 후 이용가능합니다")
         $("#cart_add_form").attr('action', '/user/login');
         $("#cart_add_form").submit();
      } else {
         $.post("/order/cart_Insert", {
            user_id: user_id,
            product_num: product_num,
         product_quantity: product_quantity
            
         })
            .done(function(data) {
               var data = eval("(" + data + ")");

               if (data.result == 1) {
                  	alert("상품이 장바구니에 담겼습니다");
				  	$("#cart_add_form").attr('action', '/order/cart_list');
         			$("#cart_add_form").submit();
               } else if (data.result == 2) {
                  alert("이미 담겨있는 상품입니다");
               }
            })
      }
   });






// 주문자 닉네임 클릭
$(".order_nick_btn").click(function() {
   var order_nick = this.value;
   $(".review_writing_text").text(order_nick + "님, ");
});

// (관리자) 상품수정하기 버튼 눌렀을 때
$(".edit_btn").click(function() {
   $("#cart_add_form").attr('action', '/product/productEdit');
   $("#cart_add_form").attr('method', 'POST');
   $("#cart_add_form").submit();
});

// (관리자) 상품삭제하기 버튼 눌렀을 때
$(".del_btn").click(function() {
   var con = confirm("해당 상품을 삭제하시겠습니까?");

   if (con == true) {
      alert("해당 상품이 삭제되었습니다");
      $("#cart_add_form").attr('action', '/product/product_Delete');
      $("#cart_add_form").submit();
   }
});
    // ★★★☆☆★★★ 실행 안해본 코드 ★★★☆☆★★★ //
    
    

});