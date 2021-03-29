package com.project.ezkit;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.ezkit.product.Product;
import com.project.ezkit.product.ProductService;

@Controller
public class MyController {

   public static String basePath = "C:\\EZKitImg\\";

   @Autowired
   private ProductService product_Service;

   @RequestMapping(value = "/")
   public ModelAndView index() {

      ModelAndView mav = new ModelAndView("/index");

      // 실시간 베스트 6개
      ArrayList<Product> hitList = (ArrayList<Product>) product_Service.selectProductByHit();
      ArrayList<Product> hitIndexList = new ArrayList<Product>();
      ArrayList<String> hitFileList = new ArrayList<String>();

      // 최신 메뉴 5개
      ArrayList<Product> list = (ArrayList<Product>) product_Service.selectProductAll();
      ArrayList<Product> indexList = new ArrayList<Product>();
      ArrayList<String> fileList = new ArrayList<String>();

      Product p = new Product();
      String path = "";

      try {
         /* 등록된 이미지 가져오기 */
        ArrayList<String> bannerList = new ArrayList<String>();
        String bannerpath = basePath + "Banner";

        File bannerImgDir = new File(bannerpath);

        if (bannerImgDir.exists()) {
           String[] files = bannerImgDir.list();
           for (int i = 0; i < files.length; i++) {
              bannerList.add(files[i]);
           }
        }

        mav.addObject("bannerList", bannerList);
         
         
         System.out.println("hitList.size : " + hitList.size());

         // 실시간 베스트 6개
         for (int i = 0; i < 6; i++) {
            System.out.println("반복문 i : " + i);
            // 전체 상품수가 6개 미만일 때
            if (hitList.size() < 6) {
               // 마지막 상품으로 채운다
               if (i < hitList.size()) {
                  p = hitList.get(i);
                  System.out.println("반복문 if hitList.get(i) : " + i + " " + p);
               } else {
                  p = hitList.get(hitList.size() - 1);
                  System.out.println("반복문 else hitList.get(i) : " + i + " " + p);
               }
               path = basePath + p.getProduct_num() + "\\";
               File imgDir = new File(path);

               if (imgDir.exists()) {
                  String[] files = imgDir.list();
                  hitFileList.add(files[0]);
               }
               hitIndexList.add(p);
               System.out.println("if문 p = " + p);
            }
            // 상품 수가 6개 이상일 때
            else {
               p = hitList.get(i);
               path = basePath + p.getProduct_num() + "\\";
               File imgDir = new File(path);

               if (imgDir.exists()) {
                  String[] files = imgDir.list();
                  hitFileList.add(files[0]);
               }
               System.out.println("p = " + p);
               hitIndexList.add(p);
               System.out.println("hitIndexList에 add 성공" + i + "번쨰");
            }
         }

         System.out.println("list.size : " + list.size());
         // 최신 메뉴 5개
         for (int i = 0; i <= 5; i++) {
            System.out.println("반복문 i : " + i);
            // 전체 상품수가 5개 미만일 때
            if (i < 5) {
               // 마지막 상품으로 채운다
               if (i < list.size()) {
                  p = list.get(i);
                  System.out.println("반복문 if list.get(i) : " + i + " " + p);
               } else {
                  p = list.get(list.size() - 1);
                  System.out.println("반복문 else list.get(i) : " + i + " " + p);
               }
               path = basePath + p.getProduct_num() + "\\";
               File imgDir = new File(path);

               if (imgDir.exists()) {
                  String[] files = imgDir.list();
                  fileList.add(files[0]);
               }
               indexList.add(p);
               System.out.println("if문 p = " + p);
            } else {
               continue;
//                상품 수가 5개 이상일 때
//               p = list.get(i);
//               path = basePath + p.getProduct_num() + "\\";
//               File imgDir = new File(path);
//
//               if (imgDir.exists()) {
//                  String[] files = imgDir.list();
//                  fileList.add(files[0]);
//               }
//               System.out.println("p = " + p);
//               indexList.add(p);
//               System.out.println("indexList에 add 성공" + i + "번쨰");
            }
         }
         System.out.println("hitIndexList : " +hitIndexList);
         System.out.println("hitFileList : " + hitFileList);
         mav.addObject("hitFileList", hitFileList);
         mav.addObject("hitIndexList", hitIndexList);
         
         System.out.println("indexList : " +indexList);
         System.out.println("fileList : " + fileList);
         mav.addObject("fileList", fileList);
         mav.addObject("indexList", indexList);

      } catch (IndexOutOfBoundsException e) {
         e.printStackTrace();
      }

      return mav;
   }

}