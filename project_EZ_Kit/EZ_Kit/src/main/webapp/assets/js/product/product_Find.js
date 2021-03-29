$(document).ready(function() {
	$(".find_menu_btn").click(function() {
		var searchKeyword = $(".find_bar_text").val();

		if (searchKeyword == "") {
			alert("검색할 상품명을 입력해주세요");
			return false;
		} else {
			$(".findMenu_Form").attr('action', '/product/searchKeyword');
			$(".findMenu_Form").submit();
		}
	});
});