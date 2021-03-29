package com.project.ezkit.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.ezkit.cart.CartService;
import com.project.ezkit.user.User;
import com.project.ezkit.user.UserService;

@Controller
public class OrderController {

   @Autowired
   private OrderService order_service;

   @Autowired
   private UserService user_service;
   
   @Autowired
   private OrderProductService orderProduct_service;
   
   @Autowired
   private CartService cart_service;
   
//   @Autowired
//   private ProductService product_service;
   
   
   // 결제창으로 이동 : order_Payment.jsp
   // 필요한 데이터 : user_id, product_num, product_quantity, used_point
   // 페이지에 보낼 데이터 : user_id (User), productNum_list(선택한 상품 번호 리스트)
   //               productQuantity_list(선택한 각 상품 수량 리스트)
   //               productPriceSum_list(선택한 각 상품 합계 : 이미 계산 되어 있다)
   //               used_point (사용한 포인트) : 결제완료후 user_point - used_point 계산
   @RequestMapping(value = "/order/payment")
   public ModelAndView orderPayment(HttpServletRequest request) {
      System.out.println("orderPayment 컨트롤러 시작 \n");
      ModelAndView mav = new ModelAndView("/order/order_Payment");
      String user_id = request.getParameter("user_id");
      System.out.println("orderPayment user_id : \n" + user_id);
      try {
         User u = user_service.selectUserById(user_id);
         
         String[] productName_Array = request.getParameterValues("product_name");
         System.out.println("orderPayment productName_Array : \n" + productName_Array);
         ArrayList<String> productname_list = new ArrayList<String>(Arrays.asList(productName_Array));
         
         ArrayList<Integer> productNum_list = returnList(request.getParameterValues("product_num"));
         ArrayList<Integer> productQuantity_list = returnList(request.getParameterValues("product_quantity"));
         ArrayList<Integer> productPriceSum_list = returnList(request.getParameterValues("product_price"));
         int used_point = Integer.parseInt(request.getParameter("used_point"));
         System.out.println("orderPayment productNum_list : \n" + productNum_list);
         System.out.println("orderPayment productname_list : \n" + productname_list);
         System.out.println("orderPayment productQuantity_list : \n" + productQuantity_list);
         System.out.println("orderPayment productPriceSum_list : \n" + productPriceSum_list);
         System.out.println("orderPayment u : \n" + u);
         System.out.println("orderPayment used_point : \n" + used_point);
         mav.addObject("u", u);
         mav.addObject("used_point", used_point);
         mav.addObject("productNum_list", productNum_list);
         mav.addObject("productname_list", productname_list);
         mav.addObject("productQuantity_list", productQuantity_list);
         mav.addObject("productPriceSum_list", productPriceSum_list);
         
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println("orderPayment 컨트롤러 끝 \n");
      return mav;
   }
   
   
   
   
   // 주문하기
   // order / orderProduct 테이블에 동시에 들어가야함
   @RequestMapping(value = "/order/insertOrder")
   public ModelAndView insertOrder(HttpServletRequest request) {
      System.out.println("insertOrder 컨트롤러 시작");
      ModelAndView mav = new ModelAndView("/order/order_Result");

      try {
         int order_num = order_service.getNum();

         String user_id = request.getParameter("user_id");
         
         String[] productName_Array = request.getParameterValues("product_name");
         
         System.out.println("insertOrder productName_Array : \n" +  productName_Array);
         ArrayList<Integer> productNum_list = returnList(request.getParameterValues("product_num"));
         ArrayList<Integer> productQuantity_list = returnList(request.getParameterValues("product_quantity"));
         ArrayList<Integer> productPriceSum_list = returnList(request.getParameterValues("product_price"));

         ArrayList<String> user_addrInfo = new ArrayList<String>();
         String order_user_addr1 = request.getParameter("edited_user_addresse1");
         String order_user_addr2 = request.getParameter("edited_user_addresse2");
         String order_user_postcode = request.getParameter("edited_user_postcode");
         System.out.println("insertOrder order_user_addr1 : \n" +  order_user_addr1);
         if (order_user_addr1 == null || order_user_addr1.equals("")) {
            System.out.println("if 문");
            user_addrInfo = null;
         } else {
        	 System.out.println("else 문");
            user_addrInfo.add(order_user_addr1);
            user_addrInfo.add(order_user_addr2);
            user_addrInfo.add(order_user_postcode);
         }
         
         // 사용 포인트 
         int order_used_point = Integer.parseInt(request.getParameter("used_point"));

         // order에 넣기
         Order order = orderInsert(order_num, user_id, productQuantity_list, productPriceSum_list, order_used_point, user_addrInfo);
         System.out.println("insertOrder try order : \n" + order);
         // orderProduct에 넣기
         ArrayList<OrderProduct> orderProduct_list = orderProductInsert(productNum_list, productName_Array, productQuantity_list, 
               productPriceSum_list, order_num, user_id);
         System.out.println("insertOrder try orderProduct_list : \n" + orderProduct_list);
         
         // 장바구니에서 해당 상품 삭제
         deleteCart(user_id, productNum_list);
         
         // 사용한 point 차감
         userPointMinus(user_id, order_used_point);
         
         // 적립 point 넣기
         int sumTotalPrice = sumTotalPrice(productPriceSum_list);
         User u = user_service.selectUserById(user_id);
         String user_rating = u.getUser_rating();
         int save_point = save_point(user_rating, sumTotalPrice);
         userPointPlus(user_id, save_point);
         
         mav.addObject("used_point", order_used_point);
         mav.addObject("save_point", save_point);
         mav.addObject("orderProduct_list", orderProduct_list);
         mav.addObject("order", order);
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      
      System.out.println("insertOrder 컨트롤러 끝");
      return mav;
   }
   
   // productNum_list, productQuantity_list, productPrice_list 배열로 바꾸는 메서드
   public ArrayList<Integer> returnList(String[] list) {
      ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(list));
      ArrayList<Integer> integerList = new ArrayList<Integer>();
      for (int i = 0; i < stringList.size(); i++) {
         String str = stringList.get(i);
         int num = Integer.parseInt(str);
         integerList.add(num);
      }
      return integerList;
   }
   
   // product_price : js로 이미 계산 되어진 각 상품 가격 ( 상품 수량 * 상품 단가 )
   // 상품 전체 가격 계산 하기 : product_price + product_price .... = sumTotalPrice
   public int sumTotalPrice(ArrayList<Integer> productPriceSum_list) {
      int sumTotalPrice = 0;

      for (int i = 0; i < productPriceSum_list.size(); i++) {
         int price = productPriceSum_list.get(i);
         sumTotalPrice += price;
      }
      return sumTotalPrice;
   }
   
   // 상품 전체 수량
   public int sumTotalQuantity(ArrayList<Integer> productQuantity_list) {
      int sumTotalQuantity = 0;
      
      for (int i = 0; i < productQuantity_list.size(); i++) {
         int quantity = productQuantity_list.get(i);
         sumTotalQuantity += quantity;
      }
      return sumTotalQuantity;
   }
   
   
      // user_rating, 총 결제금액 받아서 save_point 리턴
   public int save_point(String user_rating, int sumTotalPrice) {
      float userRating = 0f;
      float save_point = 0f;

      switch (user_rating) {
      case "Silver":
         userRating = 0.002f;
         break;

      case "Gold":
         userRating = 0.003f;
         break;

      case "Diamond":
         userRating = 0.004f;
         break;

      case "VIP":
         userRating = 0.005f;
         break;
      }
      save_point =  (float) sumTotalPrice * userRating;
      return (int) save_point;
   }
   
   // 선택한 상품들 order_prdocut 테이블에 넣고 op 리스트로 리턴
   public ArrayList<OrderProduct> orderProductInsert(ArrayList<Integer> productNum_list, String[] productName_Array, ArrayList<Integer> productQuantity_list, 
         ArrayList<Integer> productPriceSum_list, int order_num, String user_id) {

      int order_status = 1;
      
      OrderProduct op = new OrderProduct();
      
      for (int i = 0; i < productNum_list.size(); i++) {
         int product_num = productNum_list.get(i);
         String product_name = productName_Array[i];
         System.out.println("insertOrder 메서드 : \n" + product_name);
         int product_quantity = productQuantity_list.get(i);
         int product_price = productPriceSum_list.get(i);
         
         op.setOrder_num(order_num);
         op.setUser_id(user_id);
         op.setProduct_num(product_num);
         op.setProduct_name(product_name);
         op.setProduct_quantity(product_quantity);
         op.setProduct_price(product_price);
         op.setOrder_status(order_status);
         
         orderProduct_service.insertOrderProduct(op);
         
      }
      ArrayList<OrderProduct> orderProduct_list = orderProduct_service.selectOrderProductByOrderNum(order_num);
      return orderProduct_list;
   }
   
   // order 테이블에 넣고 o 리턴
   public Order orderInsert(int order_num, String user_id,  ArrayList<Integer> productQuantity_list, ArrayList<Integer> productPriceSum_list, int order_used_point,
                     ArrayList<String> user_addrInfo) {
      
      Order o = new Order();
      
      User u = user_service.selectUserById(user_id);
      String user_rating = u.getUser_rating();
      String order_user_tel;
      String order_user_addr1;
      String order_user_addr2;
      String order_user_postcode;
      
      if (user_addrInfo == null) {
         order_user_tel = u.getUser_tel();
         order_user_addr1 = u.getUser_addr1();
         order_user_addr2 = u.getUser_addr2();
         order_user_postcode = u.getUser_postcode();
      } else {
         order_user_tel = u.getUser_tel();
         order_user_addr1 = user_addrInfo.get(0);
         order_user_addr2 = user_addrInfo.get(1);
         order_user_postcode = user_addrInfo.get(2);
         
      }
      
      int sumTotalQuantity = sumTotalQuantity(productQuantity_list);
      int sumTotalPrice = sumTotalPrice(productPriceSum_list);
      
      int save_point = save_point(user_rating, sumTotalPrice);
      int order_payment_price = sumTotalPrice - order_used_point;
      
      o.setOrder_num(order_num);
      o.setUser_id(user_id);
      o.setOrder_total_quantity(sumTotalQuantity);
      o.setOrder_payment_price(order_payment_price);
      o.setOrder_save_point(save_point);
      o.setOrder_used_point(order_used_point);
      o.setOrder_user_tel(order_user_tel);
      o.setOrder_user_address1(order_user_addr1);
      o.setOrder_user_address2(order_user_addr2);
      o.setOrder_user_postcode(order_user_postcode);
      
      order_service.insertOrder(o);
      
      return o;
   }
   
   // 장바구니 삭제
   public void deleteCart(String user_id, ArrayList<Integer> productNum_list) {
      for (int i = 0; i < productNum_list.size(); i++) {
         int product_num = productNum_list.get(i);
         cart_service.deleteCart(product_num, user_id);
      }
   }
   
   // 포인트 사용
   public void userPointMinus(String user_id, int used_point) {
      User u = user_service.selectUserById(user_id);
      int userPoint = u.getUser_point();
      int resultPoint = userPoint - used_point;
      
      u.setUser_point(resultPoint);
      user_service.updateUserPoint(u);
   }
   
   // 포인트 적립
   public void userPointPlus(String user_id, int save_point) {
      User u = user_service.selectUserById(user_id);
      int userPoint = u.getUser_point();
      int resultPoint = userPoint + save_point;
      
      u.setUser_point(resultPoint);
      user_service.updateUserPoint(u);
   }
   
   
   
}