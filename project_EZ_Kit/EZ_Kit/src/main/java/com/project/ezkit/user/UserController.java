package com.project.ezkit.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.ezkit.admin.Admin;
import com.project.ezkit.admin.AdminService;
import com.project.ezkit.order.OrderProduct;
import com.project.ezkit.order.OrderProductService;
import com.project.ezkit.order.OrderService;

@Controller
public class UserController {

   @Autowired
   private UserService user_Service;

   @Autowired
   private AdminService admin_Service;

   @Autowired
   private OrderService order_service;

   @Autowired
   private OrderProductService orderProduct_service;

   // ==================== ▼ Page Control ▼ ====================

   // index로 이동
   @GetMapping(value = "/index")
   public void index() {

   }

   // loginForm으로 이동
   @GetMapping(value = "/user/login")
   public void login() {

   }

   // agreement로 이동
   @GetMapping(value = "/user/agreement")
   public void agreement() {

   }

   // register로 이동
   @GetMapping(value = "/user/register")
   public void register() {

   }

   // 비밀번호 찾기 페이지로 이동
   @GetMapping(value = "/user/user_FindPw")
   public void findPwForm() {

   }

   // ==================== ▲ Page Control ▲ ====================

   // 사용자 회원 가입
   @RequestMapping(value = "/user/register")
   public String join(User u) {

      user_Service.insertUser(u);

      return "redirect:/";
   }

   // 로그인 후 페이지 이동
   @RequestMapping(value = "/user/user_login")
   public String loginOk(HttpServletRequest request, @RequestParam(value = "user_id") String user_id,
         @RequestParam(value = "user_pw") String user_pw) {

      User u = user_Service.selectUserById(user_id);
      Admin a = admin_Service.selectAdminById(user_id);
      HttpSession session = request.getSession();

      if (u != null) {
         session.setAttribute("user_id", u.getUser_id());
         session.setAttribute("user_nick", u.getUser_nick());
         session.setAttribute("user_type", u.getUser_type());
         session.setAttribute("user_rating", u.getUser_rating());
      } else if (a.getUser_type().equals("admin")) {
         session.setAttribute("admin_id", a.getAdmin_id());
         session.setAttribute("admin_nick", a.getAdmin_nick());
         session.setAttribute("user_type", a.getUser_type());
      }

      return "redirect:/";
   }

   // 로그아웃 인덱스로 이동
   @RequestMapping(value = "user/logout")
   public String logout(HttpServletRequest request) {

      HttpSession session = request.getSession(false);
      session.removeAttribute("user_id");
      session.invalidate();

      return "redirect:/";
   }

   // 프로필 창으로 이동
   @RequestMapping(value = "/user/userProfile")
   public ModelAndView userProfile(@RequestParam(value = "user_id") String user_id) {
      System.out.println("userProfile 컨트롤러 시작 \n");
      ModelAndView mav = new ModelAndView("/user/user_Profile");
      try {

         User u = user_Service.selectUserById(user_id);

         System.out.println("userProfile u : \n" + u + "\n");
         /**
          * OrderList 추가하기
          */
         ArrayList<OrderProduct> OrderProduct_list = orderProduct_service.selectOrderProductByUserId(user_id);
         System.out.println("userProfile OrderProduct_list : \n" + OrderProduct_list + "\n");

         ArrayList<Date> currentOrderStatusDate = currentOrderStatusDate(OrderProduct_list);

         System.out.println("userProfile currentOrderStatusDate : \n" + currentOrderStatusDate + "\n");

         // 이번 달 구매 횟수
         String month_start = getMonthStartDay();
         String month_end = getMonthEndDay();
         System.out.println("userProfile month_start : \n" + month_start);
         System.out.println("userProfile month_end : \n" + month_end);

         int countOrderMonth = order_service.countOrderMonth(month_start, month_end);
         System.out.println("userProfile countOrderMonth : \n" + countOrderMonth);

         // 고객 등급 매달 갱신
         String updatedUserRating = updateUserRating(user_id, countOrderMonth);
         System.out.println("userProfile updateUserRating : \n" + updatedUserRating);

         // 주문 상태 별로 카운트
         ArrayList<Integer> orderState_list = orderStateList(user_id);
         System.out.println("userProfile orderState_list : \n" + orderState_list);
         for (int i = 1; i <= orderState_list.size(); i++) {
            mav.addObject("status_" + i, orderState_list.get(i - 1));
         }

         mav.addObject("u", u);
         mav.addObject("updatedUserRating", updatedUserRating);
         mav.addObject("countOrderMonth", countOrderMonth);
         mav.addObject("OrderProduct_list", OrderProduct_list);
         mav.addObject("currentOrderStatusDate", currentOrderStatusDate);

         System.out.println("userProfile 컨트롤러 끝 \n");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return mav;
   }

   // 프로필 창으로 이동
   @RequestMapping(value = "/user/userProfileStatus")
   public ModelAndView userProfileStatus(@RequestParam(value = "user_id") String user_id,
         @RequestParam(value = "status") int order_status) {
      System.out.println("userProfileStatus 컨트롤러 시작 \n");
      ModelAndView mav = new ModelAndView("/user/user_Profile");
      try {

         User u = user_Service.selectUserById(user_id);

         System.out.println("userProfile u : \n" + u + "\n");
         /**
          * OrderList 추가하기
          */
         ArrayList<OrderProduct> OrderProduct_list = orderProduct_service
               .selectOrderProductByStatusAndUserId(user_id, order_status);
         System.out.println("userProfile OrderProduct_list : \n" + OrderProduct_list + "\n");

         ArrayList<Date> currentOrderStatusDate = currentOrderStatusDate(OrderProduct_list);

         System.out.println("userProfile currentOrderStatusDate : \n" + currentOrderStatusDate + "\n");

         // 이번 달 구매 횟수
         String month_start = getMonthStartDay();
         String month_end = getMonthEndDay();
         System.out.println("userProfile month_start : \n" + month_start);
         System.out.println("userProfile month_end : \n" + month_end);

         int countOrderMonth = order_service.countOrderMonth(month_start, month_end);
         System.out.println("userProfile countOrderMonth : \n" + countOrderMonth);

         // 고객 등급 매달 갱신
         String updatedUserRating = updateUserRating(user_id, countOrderMonth);
         System.out.println("userProfile updateUserRating : \n" + updatedUserRating);

         // 주문 상태 별로 카운트
         ArrayList<Integer> orderState_list = orderStateList(user_id);
         System.out.println("userProfile orderState_list : \n" + orderState_list);
         for (int i = 1; i <= orderState_list.size(); i++) {
            mav.addObject("status_" + i, orderState_list.get(i - 1));
         }

         mav.addObject("u", u);
         mav.addObject("updatedUserRating", updatedUserRating);
         mav.addObject("countOrderMonth", countOrderMonth);
         mav.addObject("OrderProduct_list", OrderProduct_list);
         mav.addObject("currentOrderStatusDate", currentOrderStatusDate);

         System.out.println("userProfile 컨트롤러 끝 \n");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return mav;
   }

   // 회원검색하여 수정창으로 이동
   @RequestMapping(value = "/user/userEdit")
   public ModelAndView userEdit(HttpServletRequest request) {

      ModelAndView mav = new ModelAndView("/user/user_Edit");
      HttpSession session = request.getSession(false);
      String user_id = (String) session.getAttribute("user_id");
      User u = user_Service.selectUserById(user_id);
      System.out.println("Edit 컨트롤러 =========== " + user_id);
      System.out.println(u.toString());

      mav.addObject("u", u);

      return mav;

   }

   // 회원정보 수정 후 프로필창으로 이동
   @RequestMapping(value = "/user/Edit")
   public String edit(HttpServletRequest request, RedirectAttributes redirect, User u,
         @RequestParam(value = "user_id") String user_id) {

      HttpSession session = request.getSession(false);

      user_Service.updateUser(u);

      redirect.addAttribute("user_id", user_id);

      return "redirect:/user/userProfile";
   }

   // 회원 탈퇴를 진행하는 메소드
   @RequestMapping(value = "/user/userDelete")
   public String userDelete(HttpServletRequest request) {

      HttpSession session = request.getSession(false);
      String user_id = (String) session.getAttribute("user_id");
      user_Service.deleteUser(user_id);

      session.removeAttribute("user_id");
      session.invalidate();

      return "redirect:/";
   }

   // 로그인 시 ID & PW 체크진행 메소드
   @RequestMapping(value = "/user/loginCheck")
   public ModelAndView loginCheck(@RequestParam(value = "user_id") String user_id,
         @RequestParam(value = "user_pw") String user_pw) {
	   System.out.println("loginCheck 시작 \n");
      // user_id 가 관리자일 때
      // user_id 가 일반 회원일 때
      
      Admin admin = admin_Service.selectAdminById(user_id);
      User user = user_Service.selectUserById(user_id);
      System.out.println(admin);
      System.out.println(user);
      ModelAndView mav = new ModelAndView("/user/userJSON");
      try {
         
         // 아예 존재하지 않을 때 0
         if (admin == null && user == null) {
        	System.out.println("if 아이디 존재하지 않을 떄 \n");
            mav.addObject("type", 0);
            
            // admin일 때
         } else if (admin != null && user == null) {
        	 System.out.println("if admin일 떄 \n");
            // 비밀번호 체크 : 틀릴때 1 , 맞을때 2
            if (!admin.getAdmin_pw().equals(user_pw)) {
               mav.addObject("type", 1);
            } else {
               mav.addObject("type", 2);
            }
            
         } else if (admin == null && user != null) {
        	 System.out.println("if user일 떄 \n");
            if (!user.getUser_pw().equals(user_pw)) {
               mav.addObject("type", 1);
            } else {
               mav.addObject("type", 2);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return mav;
   }

   /**
    * 존재하는 회원인지 체크 진행하는 메소드 (JSON)
    * 
    * @param user_id - DB와 입력받은 ID가 일치하는지 확인
    * @return
    */
   // 존재하는 회원인지 체크진행 매소드
//   @RequestMapping(value = "/user/userCheck")
//   public ModelAndView userCheck(@RequestParam(value = "user_id") String user_id) {
//
//      ModelAndView mav = new ModelAndView("/user/userJSON");
//      try {
//         
//         User u = user_Service.selectUserById(user_id);
//         String result = "";
//         
//         if (u == null) {
//            result = "일치하는 회원이 없습니다";
//            mav.addObject("result", result);
//         }
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
//      return mav;
//   }

   /**
    * 비밀번호 변경 시 임시비밀번호 발급
    * 
    * @param user_id - 비밀번호 변경하고자 하는 User 아이디
    * @return
    */
   // 비밀번호 변경 시 임시비밀번호 발급
   @RequestMapping(value = "/user/user_Find_Pw")
   public ModelAndView userFindPw(@RequestParam(value = "user_id") String user_id) {

      ModelAndView mav = new ModelAndView("/user/user_FindPw");
      String newPassword = getRandomPassword();

      try {
         user_Service.userPasswordByEmail(user_id, newPassword);
      } catch (Exception e) {
         e.printStackTrace();
      }

      String sender = "EZKitAdmin@Ezkit.com";
      String subject = "EzKit 회원님의 비밀번호 변경 안내드립니다";
      String content = "회원님의 새로운 비밀번호는 <strong> " + newPassword + " </strong>입니다";

      try {
         sendMail(sender, user_id, subject, content);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return mav;
   }

   /**
    * 범위를 갖는 랜덤값을 생성하여 리턴하는 메서드
    * 
    * @param min - 범위 안에서의 최소값
    * @param max - 범위 안에서의 최대값
    * @return min~max 안에서의 랜덤값
    */
   public int random(int min, int max) {
      int num = (int) ((Math.random() * (max - min + 1)) + min);
      return num;
   }

   /**
    * 랜덤한 비밀번호를 생성하여 리턴한다.
    * 
    * @return String
    */
   public String getRandomPassword() {
      // 리턴할 문자열
      String password = "";

      // A~Z, a~z, 1~0
      String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
      // 글자길이
      int words_len = words.length();

      for (int i = 0; i < 8; i++) {
         // 랜덤한 위치에서 한 글자를 추출한다.
         int random = random(0, words_len - 1);
         String c = words.substring(random, random + 1);

         // 추출한 글자를 미리 준비한 변수에 추가한다.
         password += c;
      }

      return password;
   }

   /**
    * 메일을 발송한다.
    * 
    * @param sender   - 발송자 메일 주소
    * @param receiver - 수신자 메일 주소
    * @param subject  - 제목
    * @param content  - 내용
    * @throws MessagingException
    */
   // --> import javax.mail.MessagingException;
   public void sendMail(String sender, String receiver, String subject, String content) throws MessagingException {

      /** 메일 발송 환경설정 */
      // 발송 정보를 담기 위한 객체
      // --> import java.util.Properties;
      Properties p = new Properties();

      // 본인의 Gmail 주소로 계정 설정
      p.put("mail.smtp.user", "EzKitAdmin@EZKit.com");

      // Google SMTP 서버 정보 설정
      p.put("mail.smtp.host", "smtp.gmail.com");
      p.put("mail.smtp.port", "465");

      // 아래 정보는 수정하지 마세요.
      p.put("mail.smtp.starttls.enable", "true");
      p.put("mail.smtp.auth", "true");
      p.put("mail.smtp.debug", "true");
      p.put("mail.smtp.socketFactory.port", "465");
      p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      p.put("mail.smtp.socketFactory.fallback", "false");

      /** 미리 준비한 SMTPAuthenticator클래스에 대한 인증정보 객체 생성 */
      // helper 패키지에 준비한 인증정보 리턴 기능을 갖는 클래스의 객체 생성
      // --> import javax.mail.Authenticator;
      Authenticator auth = new SMTPAuthenticator();

      /** 인증정보와 설정정보를 하나로 묶어서 메일서버에 대한 세션객체 생성 */
      // --> import javax.mail.Session;
      Session ses = Session.getInstance(p, auth);

      // 메일을 전송할 때 상세한 상황을 콘솔에 출력한다.
      ses.setDebug(true);

      /** 세션을 포함하는 메시지 객체 생성 --> 발신주소,수신주소,제목,내용 설정 */
      // 메일의 내용을 담기 위한 객체
      // --> import javax.mail.internet.MimeMessage;
      MimeMessage msg = new MimeMessage(ses);

      // 제목 설정
      msg.setSubject(subject);

      // 보내는 사람의 메일주소
      // --> import javax.mail.Address;
      Address fromAddr = new InternetAddress(sender);
      msg.setFrom(fromAddr);

      // 받는 사람의 메일주소
      Address toAddr = new InternetAddress(receiver);
      // --> import javax.mail.Message;
      msg.addRecipient(Message.RecipientType.TO, toAddr);

      // 메시지 본문의 내용과 형식, 캐릭터 셋 설정
      msg.setContent(content, "text/html;charset=UTF-8");

      /** 메일 발송하기 */
      // --> import javax.mail.Transport;
      Transport.send(msg);

   }

   /**
    * 메일 서버에 대한 인증 정보를 리턴해 주기 위한 클래스
    */
   public class SMTPAuthenticator extends Authenticator {
      /**
       * 메일 발송시 계정 정보를 리턴해 주는 역할을 한다.
       */
      @Override
      public PasswordAuthentication getPasswordAuthentication() {
         // 자신의 Gmail 메일 주소와 비밀번호 입력.
         return new PasswordAuthentication("bumki8967@gmail.com", "qjfzmsla8967!@");
      }
   }

   // 구매 상태에 따라 날짜 반환하는 함수
   public ArrayList<Date> currentOrderStatusDate(ArrayList<OrderProduct> op) {
      ArrayList<Date> currentOrderStatusDate = new ArrayList<Date>();

      for (int i = 0; i < op.size(); i++) {
         OrderProduct orderProduct = op.get(i);
         Date date = orderProduct_service.selectOrderStatusDate(orderProduct);
         currentOrderStatusDate.add(date);
      }
      return currentOrderStatusDate;
   }

   // 현재 달의 시작일 : String 으로 return 하기
   public String getMonthStartDay() {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int day = cal.get(Calendar.DAY_OF_MONTH);
      String month_start = year + "/" + month + "/" + 1;

      return month_start;
   }

   // 현재 달의 마지막
   public String getMonthEndDay() {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      String month_end = year + "/" + month + "/" + day;

      return month_end;
   }

   // 고객 등급 매 달 갱신 메서드
   // 관리자가 지정한건 유지 못함 : 하려면 관리자가 지정해준 type 필요
   public String updateUserRating(String user_id, int countOrderMonth) {
	   String user_rating = "";
      if (countOrderMonth <= 5) {
         user_rating = "Silver";
      } else if (countOrderMonth > 5 && countOrderMonth <= 10) {
         user_rating = "Gold";
      } else if (countOrderMonth > 10 && countOrderMonth <= 15) {
         user_rating = "Diamond";
      } else if (countOrderMonth > 15) {
         user_rating = "VIP";
      }
      user_Service.updateUserRating(user_id, user_rating);

      return user_rating;
   }

   // 주문 상태 카운트
   public ArrayList<Integer> orderStateList(String user_id) {
      ArrayList<Integer> orderStatus_list = new ArrayList<Integer>();

      for (int order_status = 1; order_status <= 5; order_status++) {
         int countStatus = orderProduct_service.countOrderByOrderStatus(user_id, order_status);
         orderStatus_list.add(countStatus);
      }

      return orderStatus_list;
   }

}