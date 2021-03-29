$(document).ready(function() {

   // 천단위 
   var textArea = $(".productPrice");
   console.log(textArea)
   console.log(textArea.length)
   for (i = 0; i < textArea.length; i++) {
      text_area = textArea[i]
      var price = Number($(text_area).text());
      price = price.toLocaleString()
      console.log(price)
      $(text_area).text(price + " 원")
   }

   // 선택한 분류에 밑줄
   var type2 = document.all.type2.value;

   $(".menu_list_btn").removeClass('active');
   $("input[name=" + type2 + "]").toggleClass('active');

   // 분류 선택시
   $(".menu_list_btn").click(function() {

      var btnIdx = this.name;
      document.all.type1.value = btnIdx;

      var classification_name = this.value;
      this.form.classification_name.value = classification_name;

      if (btnIdx == 0) {
         $("#c_form").attr('action', '/product/product_list');
      }

      this.form.submit();
   });

   // 장바구니 버튼 눌렀을 때
   $(".prod_list_cart_btn").click(function() {
      var btn = $(this)
      console.log(btn)
      var user_id = $("#user_id").val();
      var product_num = $(btn).parent().nextAll()[1]
      product_num = $(product_num).val()
      console.log(user_id);
      console.log(product_num);

      if (user_id == "" || user_id == undefined) {
         console.log("if로 들어왔다");
         alert("로그인 후 이용이 가능합니다");
         $(".cart_add_form").attr('action', '/user/login');
         $(".cart_add_form").submit();
      } else {
         console.log(user_id);
         console.log("else로 들어왔다");

         $.post("/order/cart_Insert", {
            user_id: user_id,
            product_num: product_num,
			product_quantity: 1
         })
            .done(function(data) {
               var data = eval("(" + data + ")");

               if (data.result == 1) {
                  alert("상품이 장바구니에 담겼습니다");
               } else if (data.result == 2) {
                  alert("이미 담겨있는 상품입니다");
               }
            })
      }
   });

   // (관리자) 상품삭제 버튼 눌렀을 때
   $(".del_btn").click(function() {
      var product_num = $(this).data("product_num")
      console.log(product_num)

      $.post("/product/productDelete", {
         product_num: product_num
      })
         .done(function(data) {
            var data = eval("(" + data + ")");

            if (data.result == 1) {
               var con = confirm("해당 상품을 삭제하시겠습니까?");

               if (con == true) {
                  alert("해당 상품이 삭제되었습니다");
                  $(".cart_add_form").attr('action', '/product/product_list');
                  $(".cart_add_form").submit();
               }
            }
         });

   });


   // (관리자) 상품수정 버튼 눌렀을 때
   $(".edit_btn").click(function() {
      var thisForm = $(this).parent().parent()
      console.log(thisForm)
      $(thisForm).attr('action', '/product/productEdit');
      $(thisForm).attr('method', 'POST');
      $(thisForm).submit();
   });
});