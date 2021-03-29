$(document).ready(function() {

    var total_order_price = []
    var total_product_quantity = 0
        // 천단위 콤마
    var allProductPrice = $(".price_td")
    for (i = 0; i < allProductPrice.length; i++) {
        var price = Number(allProductPrice[i].innerText.replace("원", "")).toLocaleString()
        allProductPrice[i].innerText = price + " 원"
    }



    // 전체 선택
    $(".all_check_btn").click(function() {
        var checkBtnAll = $(".checkbox");
        var totalCheckedTr = checkBtnAll.parent().parent()
        var product_num = $(this).data("product_num")
		console.log(product_num)
        for (i = 0; i < totalCheckedTr.length; i++) {

            var checkedTr = totalCheckedTr[i];
            var checkedbtn = $(checkedTr).children().children().first()
            if (checkedbtn.is(":checked") == false) {
                var sumprice = sumProductPrice(checkedbtn)
                var checkIdx = productIndx(checkedbtn)
                var checkedMenuName = $(checkedTr).children().eq(1).text().trim()
                var quantity = totalQuantity(checkedbtn)
                var printArea = $(".product_name_list_area")
                var product_price = sumProductPrice(checkedbtn)
                var checkedMenu = `
                    <div class="side_menu_product_name" data-sumprice="${sumprice}" id="side_menu_${checkIdx}">${checkedMenuName}</div>
					<input type="hidden" id="product_name_${checkIdx}" name="product_name" value="${checkedMenuName}"/>                    
					<input type="hidden" id="product_num_${checkIdx}" name="product_num" value="${product_num}"/>
                    <input type="hidden" id="product_quantity_${checkIdx}" name="product_quantity" value="${quantity}"/>
                    <input type="hidden" id="product_price_${checkIdx}" name="product_price" value="${product_price}"/>
                    `
                $(printArea).append(checkedMenu);
                checkedbtn.prop("checked", true);
            }
        }
        a = totalPrice()
        console.log(a)
    });

    // 선택 취소
    $(".all_check_cancel_btn").click(function() {
        var checkBtnAll = $(".checkbox");
        var totalCheckedTr = checkBtnAll.parent().parent()

        for (i = 0; i < totalCheckedTr.length; i++) {

            var checkedTr = totalCheckedTr[i];
            var checkedbtn = $(checkedTr).children().children().first()
            if (checkedbtn.is(":checked") == true) {

                var checkIdx = productIndx(checkedbtn)
                var checkedMenuName = $(checkedTr).children().eq(1).text().trim()
                $("#side_menu_" + checkIdx).remove()
				$("#product_name_" + checkIdx).remove()
                $("#product_num_" + checkIdx).remove()
                $("#product_quantity_" + checkIdx).remove()
                $("#product_price_" + checkIdx).remove()
                checkedbtn.prop("checked", false);
            }
        }
        a = totalPrice()
        console.log(a)
    });

    // 선택 삭제
    $(".select_del_btn").click(function() {
        var con = confirm("목록에서 삭제하시겠습니까?")

        if (con) {
            var checkedAllbtn = $('input[type="checkbox"]:checked')
            var totalCheckedTr = checkedAllbtn.parent().parent()

            for (i = 0; i < totalCheckedTr.length; i++) {
                var checkedTr = totalCheckedTr[i];
                var checkedbtn = $(checkedTr).children().children().first()
                if (checkedbtn.is(":checked") == true) {
                    var checkIdx = productIndx(checkedbtn)
                    $("#side_menu_" + checkIdx).remove()
					$("#product_name_" + checkIdx).remove()
                    $("#product_num_" + checkIdx).remove()
                    $("#product_quantity_" + checkIdx).remove()
                    $("#product_price_" + checkIdx).remove()
                }
            }
            totalCheckedTr.remove();

        } else {
            return false
        }
        a = totalPrice()
        console.log(a)
    });

    // 삭제 버튼 클릭시
    $(".cancel_btn").click(function() {
        var con = confirm("목록에서 삭제하시겠습니까?")
        if (con) {
            var cancelbtn = $(this)
            var selectedTr = $(cancelbtn).parent().parent()
            var checkIdx = $("tr").index(selectedTr)
            $("#side_menu_" + checkIdx).remove()
			$("#product_name_" + checkIdx).remove()
            $("#product_num_" + checkIdx).remove()
            $("#product_quantity_" + checkIdx).remove()
            $("#product_price_" + checkIdx).remove()
            selectedTr.remove()
            var user_id = $("#user_id").val()
            console.log(user_id)
            var product_num = $(selectedTr).children().children().data("product_num")
            product_num = $(product_num).data("product_num")
            console.log(product_num)
     	  
            $.post("/order/cart_delete", {
            	user_id : user_id,
            	product_num : product_num
            })
            .done(function(data) {
					var data = eval("(" + data + ")")
					
					if (data.result == 1) {
						alert("해당 상품이 삭제되었습니다");
			            $("#side_menu_" + checkIdx).remove()
			            selectedTr.remove()
					}
				});
        } else {
            return false;
        }
        totalPrice()
    });
    
    
    
    
    


    // 선택시 주문리스트에 상품명 나타나기
    $(".checkbox").click(function() {

        var checkedbtn = $(this)
            // var a = sumProductPrice(checkedbtn)
        var checkIdx = productIndx(checkedbtn)
        var printArea = $(".product_name_list_area")
        var checkedMenuName = checkedbtn.parent().next().text().trim()
        var sumprice = sumProductPrice(checkedbtn)
        var quantity = totalQuantity(checkedbtn)
            // var quantity_area = $(checkedbtn).parent().nextAll()[2]
            // console.log(quantity_area)
            // var quantity = $(quantity_area).children(".quantity_result").text()
        console.log(quantity)
        var product_num = $(this).data("product_num")
		console.log(product_num)
        var product_price = sumProductPrice(checkedbtn)
        var checkedMenu = `
            <div class="side_menu_product_name" data-sumprice="${sumprice}" id="side_menu_${checkIdx}">${checkedMenuName}</div>
 			<input type="hidden" id="product_name_${checkIdx}" name="product_name" value="${checkedMenuName}"/>    
            <input type="hidden" id="product_num_${checkIdx}" name="product_num" value="${product_num}"/>
            <input type="hidden" id="product_quantity_${checkIdx}" name="product_quantity" value="${quantity}"/>
            <input type="hidden" id="product_price_${checkIdx}" name="product_price" value="${product_price}"/>
        `
        var quantity_area = $(checkedbtn).parent().nextAll()[2].children[1]
            // console.log(quantity_area)
        var quantity = Number(quantity_area.innerText)
            // console.log(quantity)
        var price = $(checkedbtn).parent().nextAll()[1].innerText
        price = Number(price.replace("원", "").replace(",", ""))
            // console.log(price)
        var total_price = 0
        total_price = price * quantity
            // console.log(total_price)
        if (checkedbtn.is(":checked") == true) {
            $(printArea).append(checkedMenu);
        } else if (checkedbtn.is(":checked") == false) {
            $("#side_menu_" + checkIdx).remove()
        }
        a = totalPrice()
        console.log(a)
    });


    // 수량 클릭
    $(".quantity_btn").click(function() {
        var btn = $(this)
        var btnText = $(btn).text()
        var quantity = totalQuantity(btn)
        var quantity_area = $(this).parent().children(".quantity_result")

        // var quantity = Number(quantity_area.text())
        // var printArea = $(".product_name_list_area")
        var idx = productIndx(btn)
            // var checkedMenu = $("#side_menu_" + idx)

        // max_quantity : 최대수량  변수 필요함
        if (btnText === "+") {
            quantity += 1
                // if문 최대수량 써주기




            quantity_area.text(quantity)
        } else if (btnText === "-") {
            quantity -= 1

            if (quantity > 0) {
                quantity_area.text(quantity)

            } else if (quantity <= 0) {
                alert("최소 수량입니다")
                return false
            }
        }
        var sumprice = sumProductPrice(btn)
        var sumprice_area = $("#side_menu_" + idx).data("sumprice", null)
            // productQuantity = totalQuantity(btn)
        console.log(quantity)
        sumprice_area = $("#side_menu_" + idx).data("sumprice", `${sumprice}`)

        $("#product_quantity_" + idx).val(quantity)
        $("#product_price_" + idx).val(sumprice)
        console.log($("#product_quantity_" + idx).val())

        a = totalPrice()
        console.log(a)
    });

    // 포인트 전액 사용 클릭
    $("#total_use").click(function() {
        user_allPoint = getAllPoint();
        $(".used_point").data("used_point", user_allPoint);

        user_allPoint = pointComma(user_allPoint)
        $(".used_point").text(`사용한 포인트 : ${user_allPoint} p`)
        $("#used_point").val(user_allPoint)
        getUsedPoint()
        a = totalPrice()
        console.log(a)
    });

    // 포인트 사용 취소 클릭
    $("#use_cancel").click(function() {
        $(".used_point").removeData("used_point");
        getUsedPoint()
        $(".used_point").text(`사용한 포인트 :  0 p`)
        $("#used_point").val(0)
        a = totalPrice()
        console.log(a)
    });

    // 포인트 사용 클릭 : 포인트 입력 안되면 실행 x
    $("#use_btn").click(function() {
        var user_point = $(".point_textBox").val()
        if (checkNumber(user_point)) {
            user_point = Number(user_point)
            $(".used_point").data("used_point", user_point);
            user_point = pointComma(user_point)
            $(".used_point").text(`사용한 포인트 : ${user_point} p`)
            $("#used_point").val(user_point)
        }
        a = totalPrice()
        console.log(a)
    });


    // 주문하기 클릭
    $(".order_btn").click(function() {

        $("#cart_form").submit();
    });
});

// 포인트 입력 숫자만
function checkNumber(point) {
    var regexp = /^[0-9]*$/
    if (!regexp.test(point)) {
        alert("숫자만 입력하세요");
        return false;
    }
    return true;
}

// 포인트에 천단위 comma
function pointComma(point) {
    var point = point.toLocaleString()
    return point
}

// 현재 유저의 전체 포인트 가져오고 인트로 리턴
function getAllPoint() {
    var allPoint = Number($(".user_point").data("user_point"))
        // var usePoint = $(".used_point").data("used_point", allPoint)
    console.log(allPoint)
        // console.log(usePoint)
    return allPoint;
}

// 결제에 사용한 포인트
function getUsedPoint() {
    var usedPoint = Number($(".used_point").data("used_point"))
    console.log(usedPoint)
    return usedPoint;
}

// idx return
function productIndx(btn) {
    var checkedbtn = btn;
    var checkedTr = $(checkedbtn).parent().parent();
    var checkIdx = $("tr").index(checkedTr)
    return checkIdx;
}


// 수량 return
function totalQuantity(btn) {

    var checkedbtn = btn;

    var checkedTr = $(checkedbtn).parent().parent();

    var quantity_area = $(checkedTr).children().children(".quantity_result");

    var productQuantity = Number(quantity_area.text());
    return productQuantity;

}

// 수량 * 가격 : 상품당 가격 합계
function sumProductPrice(btn) {
    var checkedbtn = btn;

    var checkIdx = $("tr").index($(checkedbtn).parent().parent());

    var checkedTr = $(checkedbtn).parent().parent();
    var price_area = $(checkedTr).children(".price_td")

    var price = Number($(price_area).text().replace("원", "").replace(",", ""))

    var quantity = totalQuantity(btn)

    var productPrice = price * quantity
    return productPrice;
}

// 전체 가격 : 총 상품 가격
function totalPrice() {

    // 전체 선택 메뉴
    var selected_menu_list = $(".side_menu_product_name")

    var price_result_area = $(".side_menu_price_result")

    var productPrice = 0
    var total_productPrice = 0
    var user_used_point = getUsedPoint()
        // 하나씩 가격 가져오기
    for (i = 0; i < selected_menu_list.length; i++) {
        var selected_menu = selected_menu_list[i]

        productPrice = Number($(selected_menu).data("sumprice"))
        total_productPrice += productPrice
    }
    total_productPrice = total_productPrice - user_used_point
    $(price_result_area).text(total_productPrice.toLocaleString() + " 원")
    return total_productPrice
}