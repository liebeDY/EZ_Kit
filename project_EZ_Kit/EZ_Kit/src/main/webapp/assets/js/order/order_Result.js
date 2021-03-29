$(document).ready(function () {
    
   var now = new Date();
   var year = now.getFullYear()
   var month = now.getMonth() + 1
   var day = now.getDate()

   var str = `승인일시 : ${year}-${month}-${day}`

   $('.result_date').text(str);

	
	var productPrice_list = $(".product_price")
	var totalPrice = $(".all_price")
	console.log(productPrice_list)
	console.log(totalPrice)
	for (i = 0; i < productPrice_list.length; i++) {
		var productPrice = productPrice_list[i];
		console.log(productPrice)
		price = Number($(productPrice).text().replace("원", ""));
		price = price.toLocaleString();
		$(productPrice).text(price + " 원");
	}
	
	var allPrice = Number($(totalPrice).text().replace("원", ""));
	console.log(allPrice)
	allPrice = allPrice.toLocaleString();
	$(totalPrice).text(allPrice);




});