package com.project.ezkit.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.ezkit.classification.Classification;
import com.project.ezkit.classification.ClassificationService;
import com.project.ezkit.inquiry.Inquiry;
import com.project.ezkit.inquiry.InquiryService;
import com.project.ezkit.notice.Notice;
import com.project.ezkit.notice.NoticeService;
import com.project.ezkit.order.OrderProduct;
import com.project.ezkit.order.OrderProductService;
import com.project.ezkit.qna.QnA;
import com.project.ezkit.qna.QnAService;
import com.project.ezkit.qna_option.QnAOption;
import com.project.ezkit.qna_option.QnAOptionService;
import com.project.ezkit.user.User;
import com.project.ezkit.user.UserService;

@Controller
public class AdminController {

   @Autowired
   private AdminService admin_Service;

   @Autowired
   private OrderProductService orderProduct_service;

   @Autowired
   private UserService user_Service;

   @Autowired
   private InquiryService inquiry_Service;

   @Autowired
   private ClassificationService classification_Service;

   @Autowired
   private QnAOptionService qnaOption_Service;

   @Autowired
   private QnAService qna_Service;

   @Autowired
   private NoticeService notice_Service;

   public static String basePath = "C:\\EZKitImg\\";

   @RequestMapping(value = "/admin/main")
   public ModelAndView main() {
      ModelAndView mav = new ModelAndView("/admin/main");

      ArrayList<Inquiry> inquiryList = (ArrayList<Inquiry>) inquiry_Service.selectInquiryByResult(1); // 1????????? 2??????
      mav.addObject("inquiryList", inquiryList);
      return mav;
   }

   @GetMapping(value = "/admin/user_Info")
   public String user_InfoForm() {
      return "/admin/user_Info";
   }

   // ----- ?????? ?????? -----
   @RequestMapping(value = "/admin/order_Info")
   public ModelAndView order_info(@RequestParam(value = "order_status") int order_status) {
      System.out.println("order_info ?????? \n");
      ModelAndView mav = new ModelAndView("/admin/order_State");

      try {
         ArrayList<OrderProduct> orderProduct_list = orderProduct_service
               .selectOrderProductByOrderStatus(order_status);
         ArrayList<Date> currentOrderStatusDate_list = currentOrderStatusDate(orderProduct_list);
         ArrayList<String> orderUserName_list = orderUserName(orderProduct_list);
         System.out.println("orderProduct_list : \n" + orderProduct_list);
         System.out.println("currentOrderStatusDate_list : \n" + currentOrderStatusDate_list);
         System.out.println("orderUserName_list : \n" + orderUserName_list);
         String orderStatus = orderStatusText(order_status);
         System.out.println("status : \n" + orderStatus);

         mav.addObject("orderStatus", orderStatus);
         mav.addObject("orderProduct_list", orderProduct_list);
         mav.addObject("currentOrderStatusDate_list", currentOrderStatusDate_list);
         mav.addObject("orderUserName_list", orderUserName_list);

      } catch (Exception e) {
         e.printStackTrace();
      }

      System.out.println("order_info ??? \n");
      return mav;
   }

   // ?????? ?????? ??????
   @RequestMapping(value = "/admin/updateOrderStatus")
   public String updateOrderStatus(HttpServletRequest request, RedirectAttributes redirect) {
//         ModelAndView mav = new ModelAndView("/admin/order_Info");
      System.out.println("updateOrderStatus ?????? \n");
      int order_status = 0;
      try {
         ArrayList<Integer> orderProductNum_list = returnList(request.getParameterValues("order_product_num"));
         System.out.println("orderProductNum_list : \n" + orderProductNum_list);
         order_status = Integer.parseInt(request.getParameter("update_status"));
         System.out.println("request.getParameter(\"update_status\") : \n" + order_status);
         updateOrderStatusDateType(order_status, orderProductNum_list);
      } catch (Exception e) {
         e.printStackTrace();
      }

      redirect.addAttribute("order_status", order_status);
      System.out.println("updateOrderStatus ??? \n");
      return "redirect:/admin/order_Info";
   }

   // ?????? ????????? ?????? ?????? ?????? ??????
   public void updateOrderStatusDateType(int status, ArrayList<Integer> orderProductNum_list) {

      for (int i = 0; i < orderProductNum_list.size(); i++) {
         int order_product_num = orderProductNum_list.get(i);
         if (status == 2) {
            orderProduct_service.updateProductPreparationDate(order_product_num);
         } else if (status == 3) {
            orderProduct_service.updateDeliveryPreparationDate(order_product_num);
         } else if (status == 4) {
            orderProduct_service.updateDelieveryDate(order_product_num);
         } else if (status == 5) {
            orderProduct_service.updateDeliveryCompletedDate(order_product_num);
         }
      }
   }

   // ????????? ?????? -> int list???
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

   // ?????? ????????? ?????? ?????? ???????????? ??????
   public ArrayList<Date> currentOrderStatusDate(ArrayList<OrderProduct> op) {
      ArrayList<Date> currentOrderStatusDate = new ArrayList<Date>();

      for (int i = 0; i < op.size(); i++) {
         OrderProduct orderProduct = op.get(i);
         Date date = orderProduct_service.selectOrderStatusDate(orderProduct);
         currentOrderStatusDate.add(date);
      }
      return currentOrderStatusDate;
   }

   // ??? ?????? ?????? ??? ????????? ??????
   public ArrayList<String> orderUserName(ArrayList<OrderProduct> op) {
      ArrayList<String> orderUserName_list = new ArrayList<String>();

      for (int i = 0; i < op.size(); i++) {
         String currentOrderUserId = op.get(i).getUser_id();
         User orderUser = user_Service.selectUserById(currentOrderUserId);
         String orderUserName = orderUser.getUser_name();
         orderUserName_list.add(orderUserName);
      }
      return orderUserName_list;
   }

   public String orderStatusText(int order_status) {
      String status = "";

      switch (order_status) {
      case 1:
         status = "????????????";
         break;
      case 2:
         status = "????????????";
         break;
      case 3:
         status = "????????????";
         break;
      case 4:
         status = "?????????";
         break;
      case 5:
         status = "????????????";
         break;
      }
      return status;
   }

   // ----- ???????????? -----

   // ???????????? ??????
   @RequestMapping(value = "/admin/user_Info")
   public ModelAndView user_Info(@RequestParam(value = "user_id") String user_id) {

      ModelAndView mav = new ModelAndView("/admin/user_Info");
      User u = user_Service.selectUserById(user_id);
      mav.addObject("u", u);

      return mav;
   }

   // ???????????? ??????
   @RequestMapping(value = "/admin/user_Info_JSON")
   public ModelAndView user_Info_JSON(@RequestParam(value = "user_id") String user_id) {
      ModelAndView mav = new ModelAndView("/admin/user_Info_JSON");
      User u = user_Service.selectUserById(user_id);

      if (u == null) {
         mav.addObject("result", 0);
      }
      // ???????????? ??????
      else {
         mav.addObject("result", 1);
      }
      return mav;
   }

   // ???????????? ??????
   @RequestMapping(value = "/admin/updateUserRating")
   public ModelAndView updateUserPoint(@RequestParam(value = "user_rating") String user_rating,
         @RequestParam(value = "user_id") String user_id) {
      ModelAndView mav = new ModelAndView("/admin/user_Info");

      System.out.println("user_id" + user_id);
      user_Service.updateUserRating(user_id, user_rating);
      User u = user_Service.selectUserById(user_id);
      mav.addObject("u", u);

      return mav;

   }

   // ??????????????? ??????
   @RequestMapping(value = "/admin/updateUserPoint")
   public ModelAndView updateUserPoint(@RequestParam(value = "user_point") int user_point,
         @RequestParam(value = "user_id") String user_id) {

      ModelAndView mav = new ModelAndView("/admin/user_Info");

      User u = user_Service.selectUserById(user_id);
      u.setUser_point(user_point);
      user_Service.updateUserPoint(u);
      mav.addObject("u", u);

      return mav;
   }

   // ?????? ??????
   @RequestMapping(value = "/admin/deleteUser")
   public String deleteUser(RedirectAttributes redirect, @RequestParam(value = "user_id") String user_id) {
      redirect.addAttribute("user_id", user_id);
      user_Service.deleteUser(user_id);
      return "redirect:/admin/user_Info";
   }

   // ----- ?????? -----

   // ?????? list
   @RequestMapping(value = "/admin/inquiry_AdminList")
   public ModelAndView inquiry_AdminList(@RequestParam(value = "inquiry_result") int inquiry_result) {

      ModelAndView mav = new ModelAndView("/admin/inquiry_AdminList");

      ArrayList<Inquiry> inquiryList = (ArrayList<Inquiry>) inquiry_Service.selectInquiryByResult(inquiry_result);
      ArrayList<String> userNickList = new ArrayList<String>();

      for (int i = 0; i < inquiryList.size(); i++) {
         String user_id = inquiryList.get(i).getUser_id();
         User u = user_Service.selectUserById(user_id);
         userNickList.add(u.getUser_nick());
      }

      mav.addObject("i", inquiryList);
      mav.addObject("u", userNickList);
      return mav;
   }

   // ????????? ?????? ?????????
   @RequestMapping(value = "/admin/inquiry_AdminWritingForm")
   public ModelAndView inquiry_AdminWritingForm(@RequestParam(value = "inquiry_num") int inquiry_num) {

      ModelAndView mav = new ModelAndView("/admin/inquiry_AdminWriting");
      Inquiry i = inquiry_Service.selectInquiryByNum(inquiry_num);
      User u = user_Service.selectUserById(i.getUser_id());
      mav.addObject("i", i);
      mav.addObject("u", u);
      return mav;
   }

   // ?????? ?????? ?????? ??? ??????
   @RequestMapping(value = "/admin/inquiry_AdminWriting")
   public String inquiry_AdminWriting(RedirectAttributes redirect, Inquiry i) {

      inquiry_Service.updateInquiryReply(i);
      redirect.addAttribute("inquiry_num", i.getInquiry_num());

      return "redirect:/admin/inquiry_AdminView";
   }

   // ?????? ?????? ?????????
   @RequestMapping(value = "/admin/inquiry_AdminView")
   public ModelAndView inquiry_AdminView(@RequestParam(value = "inquiry_num") int inquiry_num) {

      ModelAndView mav = new ModelAndView("/admin/inquiry_AdminView");
      Inquiry i = inquiry_Service.selectInquiryByNum(inquiry_num);
      User u = user_Service.selectUserById(i.getUser_id());
      mav.addObject("i", i);
      mav.addObject("u", u);
      return mav;
   }

   // ?????? ?????? ?????????
   @RequestMapping(value = "/admin/inquiry_AdminEditForm")
   public ModelAndView inquiry_AdminEditForm(int inquiry_num) {
      ModelAndView mav = new ModelAndView("/admin/inquiry_AdminEdit");
      Inquiry i = inquiry_Service.selectInquiryByNum(inquiry_num);
      User u = user_Service.selectUserById(i.getUser_id());
      mav.addObject("user_nick", u.getUser_nick());
      mav.addObject("i", i);
      return mav;
   }

   // ----- ?????? ?????? -----

   @RequestMapping(value = "/admin/event_Banner")
   public ModelAndView event_Banner() {
      ModelAndView mav = new ModelAndView("/admin/event_Banner");

      /* ????????? ????????? ???????????? */
      ArrayList<String> fileList = new ArrayList<String>();
      String path = basePath + "Banner";

      File imgDir = new File(path);

      if (imgDir.exists()) {
         String[] files = imgDir.list();
         for (int i = 0; i < files.length; i++) {
            fileList.add(files[i]);
         }
      }

      mav.addObject("fileList", fileList);

      return mav;
   }

   // ??????????????? ?????????
   @RequestMapping(value = "/admin/insertBanner")
   public String insertBanner(Event e) {

      ArrayList<MultipartFile> bannerList = (ArrayList<MultipartFile>) e.getBanner_img();

      for (int i = 0; i < bannerList.size(); i++) {
         saveImg(bannerList.get(i));
      }

      return "redirect:/admin/event_Banner";
   }

   // ??????????????? ????????? ??????
   public void saveImg(MultipartFile file) {

      String fileName = file.getOriginalFilename();

      if (fileName != null && !fileName.equals("")) {
         File dir = new File(basePath + "Banner");
         File f = null;

         if (!dir.exists()) {
            dir.mkdir(); // ????????? ????????????, ??? ?????? ?????? ???????????? ????????????.
         }

         f = new File(basePath + "Banner\\" + fileName);

         try {
            file.transferTo(f);
         } catch (IllegalStateException e) {
            // TODO: handle exception
            e.printStackTrace();
         } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
         }
      }
   }

   // ????????? ????????????
   @RequestMapping("BannerImg")
   public ResponseEntity<byte[]> getImg(String fname) {

      String path = basePath + "Banner\\" + fname;

      File f = new File(path);
      HttpHeaders header = new HttpHeaders();
      ResponseEntity<byte[]> result = null;

      try {
         header.add("Content-Type", Files.probeContentType(f.toPath()));
         result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
      } catch (IOException e) {
         e.printStackTrace();
      }
      return result;
   }

   // ????????? ??????
   @RequestMapping(value = "admin/deleteBanner")
   public String deleteBanner(@RequestParam(value = "fname") String fname) {
      String path = basePath + "Banner\\";
      File imgDir = new File(path);

      if (imgDir.exists()) {
         File f = new File(path + fname);
         f.delete();
      }

      return "redirect:/admin/event_Banner";
   }

   // ----- ???????????? -----

   // ???????????? Edit ???
   @RequestMapping(value = "/admin/classification_EditPage")
   public ModelAndView classification_EditPage() {
      ModelAndView mav = new ModelAndView("/admin/classification_EditPage");

      ArrayList<Classification> classificationList = (ArrayList<Classification>) classification_Service
            .selectClassificationAll();
      mav.addObject("classificationList", classificationList);

      return mav;
   }

   // ?????? ?????? ??????
   @RequestMapping(value = "/admin/insertClassification")
   public String insertClassification(Classification c) {

      classification_Service.insertClassification(c);
      return "redirect:/admin/classification_EditPage";
   }

   // ?????? ?????? ??????
   @RequestMapping(value = "/admin/updateClassification")
   public String updateClassification(Classification c) {

      classification_Service.updateClassification(c);
      return "redirect:/admin/classification_EditPage";
   }

   // ?????? ?????? ??????
   @RequestMapping(value = "/admin/deleteClassification")
   public String deleteClassification(@RequestParam(value = "classification_num") int classification_num) {
      
      classification_Service.deleteClassification(classification_num);
      return "redirect:/admin/classification_EditPage";
   }

   // ----- QnA -----

   // QnA Edit ???
   @RequestMapping(value = "/admin/QnA_EditPage")
   public ModelAndView QnA_EditPage() {
      ModelAndView mav = new ModelAndView("/admin/QnA_EditPage");
      ArrayList<QnAOption> qnaOptionList = (ArrayList<QnAOption>) qnaOption_Service.selectQnaOptionAll();
      mav.addObject("q", qnaOptionList);
      return mav;
   }

   // QnA ?????? ???
   @RequestMapping(value = "/admin/QnA_WritingForm")
   public ModelAndView QnA_WritingForm(@RequestParam(value = "qna_option_name") String qna_option_name) {

      ModelAndView mav = new ModelAndView("/admin/QnA_Writing");
      mav.addObject("qna_option_name", qna_option_name);

      return mav;
   }

   // QnA ??????
   @RequestMapping(value = "/admin/QnA_Writing")
   public String QnA_Writing(QnA qna) {

      qna_Service.insertQna(qna);
      return "redirect:/admin/QnA_EditPage";
   }

   // QnA ?????? ??????
   @RequestMapping(value = "/admin/insertQnAOption")
   public String insertQnAOption(QnAOption qnaOption) {

      qnaOption_Service.insertQnaOption(qnaOption);
      return "redirect:/admin/QnA_EditPage";
   }

   // QnA ?????? ??????
   @RequestMapping(value = "/admin/updateQnAOption")
   public String updateQnAOption(QnAOption qnaOption) {

      qnaOption_Service.updateQnaOption(qnaOption);
      return "redirect:/admin/QnA_EditPage";
   }

   // QnA ?????? ??????
   @RequestMapping(value = "/admin/deleteQnAOption")
   public String deleteQnAOption(@RequestParam(value = "qna_option_num") int qna_option_num) {

      qnaOption_Service.deleteQnaOption(qna_option_num);
      return "redirect:/admin/QnA_EditPage";
   }

   // ----- ???????????? Notice -----

   // ???????????? list
   @RequestMapping(value = "/admin/notice_List")
   public ModelAndView noticeList() {
      ModelAndView mav = new ModelAndView("/notice/notice_List");

      ArrayList<Notice> noticeList = (ArrayList<Notice>) notice_Service.selectAllNotice();
      mav.addObject("noticeList", noticeList);

      return mav;
   }

   // ???????????? ?????? ?????????
   @RequestMapping(value = "/admin/notice_WritingForm")
   public String notice_WritingForm() {

      return "/notice/notice_Writing";
   }

   // ???????????? ??????
   @RequestMapping(value = "/admin/notice_Writing")
   public String notice_Writing(RedirectAttributes redirect, Notice n) {

      int notice_num = notice_Service.getNoticeNum();
      n.setNotice_num(notice_num);
      notice_Service.insertNotice(n);
      redirect.addAttribute("notice_num", notice_num);
      return "redirect:/admin/notice_View";
   }

   // ???????????? ??????
   @RequestMapping(value = "/admin/notice_View")
   public ModelAndView notice_View(@RequestParam(value = "notice_num") int notice_num) {

      ModelAndView mav = new ModelAndView("/notice/notice_View");
      notice_Service.noticeHits(notice_num);
      Notice n = notice_Service.selectNoticeByNum(notice_num);
      mav.addObject("n", n);
      return mav;
   }

   // ???????????? ?????? ?????????
   @RequestMapping(value = "/admin/notice_EditForm")
   public ModelAndView notice_EditForm(@RequestParam(value = "notice_num") int notice_num) {
      ModelAndView mav = new ModelAndView("/notice/notice_Edit");
      Notice n = notice_Service.selectNoticeByNum(notice_num);
      mav.addObject("n", n);
      return mav;
   }

   // ???????????? ??????
   @RequestMapping(value = "/admin/notice_Edit")
   public String notice_Edit(RedirectAttributes redirect, Notice n) {

      notice_Service.updateNotice(n);
      redirect.addAttribute("notice_num", n.getNotice_num());
      return "redirect:/admin/notice_View";
   }
   
   // ???????????? ??????
   @RequestMapping(value = "/admin/notice_Delete")
   public String notice_Delete(@RequestParam(value="notice_num") int notice_num) {

      notice_Service.deleteNotice(notice_num);
      
      return "redirect:/admin/notice_List";
   }
   
   

}