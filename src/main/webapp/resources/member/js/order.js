$(document).ready(function() {
	
	$("#checkoutBtn").on("click", function() {
		// $("#checkoutForm").submit();
		let receiver_name = $("#receiver_name").val();
		let receiver_tel = $("#receiver_tel").val();
		let receiver_addr = $("#receiver_addr").val();
		let order_request_msg = $("#order_request_msg").val();
		let param = {
			"receiver_name": receiver_name,
			"receiver_tel": receiver_tel,
			"receiver_addr": receiver_addr,
			"order_request_msg": order_request_msg
		}
		$.ajax({
			url: "/order/checkout",
			async: true,
			type: "post",
			dataType: "json",
			data: JSON.stringify(param),
			contentType: "application/json; charset=UTF-8",
			success: function(data) {
				alert(data);
				let chkAmount = $(".sub_total_td").length
				console.log(chkAmount);


				let count = 0;
				let orderDetailArr = [];

				let chkNameArr = new Array(chkAmount);
				let chkQtyArr = new Array(chkAmount);
				let chkTotalArr = new Array(chkAmount);
				let form_content = ''
				$(".productDetail").each(function(index, element) {

					let chkProductName = $(element).find("#product_name").val();
					let chkProductQty = $(element).find("#order_qty").val();
					let chkSubTotal = $(element).find("#sub_total").val();

					chkNameArr[count] = chkProductName;
					chkQtyArr[count] = chkProductQty;
					chkTotalArr[count] = chkSubTotal;
					let params = {
						"order_no": data,
						"product_name": chkProductName,
						"order_qty": chkProductQty,
						"sub_total": chkSubTotal
					}

					orderDetailArr.push(params);
					count++;

				})
				let paramList = {
					"paramList": JSON.stringify(orderDetailArr)
				}
				$.ajax({

					type: "POST",
					url: "/order/orderDetails",
					data: paramList,
					success: window.location.replace('/order/orderList')

				})
			}
		})




	})

	$("#buyNowBtn").on("click", function() {
		let product_name = $("#product_name").text();
		let order_qty = $("#cart_qty").val();
		let product_price = $("#product_price").text();
		let sub_total = parseInt(order_qty) * parseInt(product_price);
		console.log("order_qty : " + order_qty);
		console.log("product_name : " + product_name);
		console.log("product_price : " + product_price);
		console.log("sub_total : " + sub_total);
		let form_content = '';
		let productName_input =
			"<input name='orderDetailList[0].product_name' type='hidden' value='" + product_name + "'> ";
		form_content += productName_input;
		let productQty_input =
			"<input name='orderDetailList[0].order_qty' type='hidden' value='" + order_qty + "'> ";
		form_content += productQty_input;
		let subTotal_input =
			" <input name='orderDetailList[0].sub_total' type='hidden' value='" + sub_total + "'> ";
		form_content += subTotal_input;
		$("#orderForm").html(form_content);
		$("#orderForm").submit();

	});
	

});
setTotalInfo()

function setTotalInfo() {

	let totalPrice = 0; // ??? ??????
	let deliveryPrice = 0; // ?????????
	let finalTotalPrice = 0; // ?????? ??????(??? ?????? + ?????????)

	$(".sub_total_td").each(function(index, element) {

		totalPrice += parseInt($(element).find("#sub_total").val());

	});

	/* ????????? ?????? */
	if (totalPrice >= 30000) {
		deliveryPrice = 0;
	} else if (totalPrice == 0) {
		deliveryPrice = 0;
	} else {
		deliveryPrice = 3000;
	}

	finalTotalPrice = totalPrice + deliveryPrice;

	// ??? ??????
	$(".totalPrice").text(totalPrice.toLocaleString());
	// ?????????
	$(".deliveryPrice").text(deliveryPrice);
	// ?????? ??????(??? ?????? + ?????????)
	$(".finalTotalPrice").text(finalTotalPrice.toLocaleString());
}
function orderDeleteFun(order_status, order_no) {

	let answer = confirm("???????????? ??????????????? ?????????????????????????");
	if (answer) {
		if (order_status.value == "?????? ??????") {
			let param = { "order_no": order_no.value }
			$.ajax({
				url: "/order/orderDelete",
				type: "POST",
				data: param,
				success: function(data) {
					alert("??????????????? ?????????????????????.");
					location.reload();
				}
			})
		}
		else {
			alert("????????? ???????????? ?????? ??????????????? ??????????????? ????????????.");
			return false;
		}
	}
}
function orderDetailViewFun(count){
	$("#orderDetailViewForm"+count).submit();
}





