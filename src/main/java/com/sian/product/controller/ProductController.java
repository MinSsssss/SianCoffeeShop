package com.sian.product.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sian.cart.service.CartService;
import com.sian.category.service.CategoryService;
import com.sian.common.page.Criteria;
import com.sian.common.page.PageDTO;
import com.sian.member.service.MemberService;
import com.sian.product.dto.ProductAttachDTO;
import com.sian.product.dto.ProductDTO;
import com.sian.product.service.ProductService;
import com.sian.review.service.ReviewService;
import com.sian.wish.service.WishService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProductController {
	private final ProductService productService;
	private final CategoryService categoryService;
	private final ReviewService reviewService;

	/*
	 * ALL
	 */
	@GetMapping("/product/productList")
	public void productList(@RequestParam(value = "category_no", required = false) int category_no, Model model) {
		model.addAttribute("categoryList", categoryService.getCategoryList("product"));
		model.addAttribute("category", categoryService.categoryRead(category_no));

		model.addAttribute("productList", productService.memberProductList(category_no));
	}

	@GetMapping({ "/product/productRead", "/admin/product/productRead", "/admin/product/productModify" })
	public void productRead(@RequestParam("product_no") int product_no, Model model) {
		ProductDTO product = productService.getProduct(product_no);
		String thumbImg = product.getProduct_thumb_img();
		String sThumbImg = product.getProduct_s_thumb_img();
		String imgRealPath = "";

		List<ProductAttachDTO> paDTO = productService.getAttachList(product_no);
		List<String> images = new ArrayList<String>();
		List<String> s_images = new ArrayList<String>();
		System.out.println("padtosize : " + paDTO.size());
		for (int i = 0; i < paDTO.size(); i++) {

			try {
				imgRealPath = URLEncoder.encode(
						paDTO.get(i).getUploadPath() + "/" + paDTO.get(i).getUuid() + "_" + paDTO.get(i).getFileName(),
						"UTF-8");

				images.add(imgRealPath);

				/* ---------- */

				imgRealPath = URLEncoder.encode(paDTO.get(i).getUploadPath() + "/s_" + paDTO.get(i).getUuid() + "_"
						+ paDTO.get(i).getFileName(), "UTF-8");

				s_images.add(imgRealPath);

			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}	

		}
		
		/*
		 * ????????????????????????
		 */
		
		images.remove(images.indexOf(thumbImg));
		images.add(0, thumbImg);
		product.setProduct_imgs(images);

		s_images.remove(s_images.indexOf(sThumbImg));
		s_images.add(0, sThumbImg);
		product.setProduct_s_imgs(s_images);

		
		product.setAttachList(productService.getAttachList(product_no));

		model.addAttribute("product", product);

		model.addAttribute("categoryList", categoryService.getCategoryList("product"));

		System.out.println(categoryService.getCategoryList("product"));
		model.addAttribute("reviewList", reviewService.getReviewList(product_no));

//		 model.addAttribute("images",productService.getAttachList(product_no));
	}

	/*
	 * ADMIN ONLY
	 */
//	@GetMapping("/admin/product/productList")
//	public void adminProductList(Model model)  {
//		model.addAttribute("categoryList", categoryService.getCategoryList("product"));
//	
//		model.addAttribute("productList", productService.getProductList());
//	}
	@GetMapping("/admin/product/productList")
	public void adminProductList(Model model, Criteria cri) {
		model.addAttribute("categoryList", categoryService.getCategoryList("product"));

		model.addAttribute("productList", productService.getListPaging(cri));

		int totaol = productService.getTotal();

		PageDTO page = new PageDTO(cri, totaol);

		model.addAttribute("page", page);
	}

	@GetMapping("/admin/product/productRegister")
	public void productRegister(Model model) {
		model.addAttribute("categoryList", categoryService.getCategoryList("product"));
	}

	@PostMapping("/admin/product/productRegisterProc")
	public String productRegisterProc(ProductDTO productDTO) {

		if (productDTO.getAttachList() != null) {
			productDTO.getAttachList().forEach(attach -> System.out.println(attach));
		}

		productService.productRegister(productDTO);

		return "redirect:/admin/product/productList";
	}

	@PostMapping("/admin/product/productModifyProc")
	public String productModifyProc(ProductDTO productDTO) {

		productService.productModify(productDTO);

		return "redirect:/admin/product/productList";

	}

//	 @GetMapping({"/admin/product/productRead","/admin/product/productModify"})
//	 public void productRead(@RequestParam("product_no")int product_no,Model model) {
//		 ProductDTO product = productService.getProduct(product_no);
//		 if(product.getCategory_name()==null) {
//			 product.setCategory_name("??????????????????");
//		 }
//		 model.addAttribute("product",product);
//		 
//		 model.addAttribute("category",categoryService.getCategoryList("product"));
//		 
//		 model.addAttribute("reviewList", reviewService.getReviewList(product_no));
//		 
//	 }
	/*
	 * MEMBER ONLY @PreAuthorize("hasRole('ROLE_MEMBER')")
	 */

}
