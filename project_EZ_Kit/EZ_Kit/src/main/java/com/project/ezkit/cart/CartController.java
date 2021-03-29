package com.project.ezkit.cart;

import java.net.http.HttpRequest;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.ezkit.product.ProductService;
import com.project.ezkit.user.User;
import com.project.ezkit.user.UserService;

@Controller
public class CartController {

   @Autowired
   private CartService cart_Service;

   @Autowired
   private ProductService product_Service;
   
   @Autowired
   private UserService user_Service;

//   @GetMapping(value = "/order/cart")
//   public void cartForm() {
//      
//   }

   // 장바구니 등록 (디테일 페이지에서 장바구니 버튼 클릭 시)
   @RequestMapping(value = "/order/cartInsert")
   public String cartInsert(RedirectAttributes redirect, @RequestParam(value = "user_id") String user_id,
         @RequestParam(value = "product_num") int product_num, @RequestParam(value = "product_quantity") int product_quantity) {
      System.out.println("cartInsert 디테일 페이지에서 온 컨트롤러 시작 \n");
      int cart_num = cart_Service.getNum();
      try {
         Cart c = new Cart(cart_num, product_num, product_quantity, user_id);
         System.out.println("cartInsert c : \n" + c.toString());
         
         cart_Service.insertCart(c);
         
      } catch(Exception e) {
         e.printStackTrace();
      }

      redirect.addAttribute("user_id", user_id);
      System.out.println("cartInsert 디테일 페이지에서 온 컨트롤러 끝 \n");
      return "redirect:/order/cart_list";
   }

   // 장바구니 등록 (상품 리스트 페이지에서 장바구니 버튼 클릭 시)
   @RequestMapping(value = "/order/cart_Insert")
   public ModelAndView cart_Insert(@RequestParam(value = "user_id") String user_id,
         @RequestParam(value = "product_num") int product_num, @RequestParam(value = "product_quantity") int product_quantity) {
      System.out.println("cart_Insert 리스트페이지에서 온 컨트롤러 시작 \n");
      ModelAndView mav = new ModelAndView("/order/cartJSON");

      try {
         
         int cart_num = cart_Service.getNum();
         Cart c = new Cart(cart_num, product_num, product_quantity, user_id);
         System.out.println("cart_Insert c :  \n" + c);
         int rs = cart_Service.selectProductCount(product_num);
         
         int result = 0;
         
         if (c != null) {
            if (rs > 0) {
               result = 2;
               mav.addObject("result", result);
            } else {
               cart_Service.insertCart(c);
               result = 1;
               mav.addObject("result", result);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println("cart_Insert 리스트페이지에서 온 컨트롤러 시작 \n");
      return mav;
   }

   // 장바구니 리스트 출력
   @RequestMapping(value = "/order/cart_list")
   public ModelAndView cartList(@RequestParam(value = "user_id") String user_id) {
      System.out.println("cartList 컨트롤러 시작");
      ModelAndView mav = new ModelAndView("/order/cart");

      ArrayList<Cart> myCartList = (ArrayList<Cart>) cart_Service.selectCartProductById(user_id);
      ArrayList<Cart> cartList = cart_Service.selectCartById(user_id);
      User user = user_Service.selectUserById(user_id);
      System.out.println("cartList 컨트롤러 myCartList : \n" + myCartList + "\n");
      System.out.println("cartList 컨트롤러 user : \n" + user + "\n");
      System.out.println("cartList 컨트롤러 cartList : \n" + cartList + "\n");

      mav.addObject("myCartList", myCartList);
      mav.addObject("cartList", cartList);
      mav.addObject("u", user);

      System.out.println("cartList 컨트롤러 끝 \n");
      return mav;
   }

   // 장바구니 리스트 삭제
   @RequestMapping(value = "/order/cart_delete")
   public ModelAndView cartDelete(HttpServletRequest request) {
      System.out.println("cart_delete 컨트롤러 시작 \n");
      ModelAndView mav = new ModelAndView("/order/cartJSON");
      int result = 0;

      String user_id = request.getParameter("user_id");
      String[] productNum_list = request.getParameterValues("productNum_list");
      System.out.println("cart_delete user_id : \n" + user_id);
      System.out.println("cart_delete productNum_list : \n" + productNum_list);
      try {
         for (int i = 0; i < productNum_list.length; i++) {
            int product_num = Integer.parseInt(productNum_list[i]);
            System.out.println("cart_delete product_num : \n" + product_num);
            cart_Service.deleteCart(product_num, user_id);
         }
         result = 1;
         System.out.println("cart_delete result : \n" + result);
         mav.addObject("result", result);
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println("cart_delete 컨트롤러 끝 \n");
      return mav;
   }
}