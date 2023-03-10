package com.coding5.el.class_comm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding5.el.class_comm.service.ClassCommService;
import com.coding5.el.class_comm.vo.ClassCommVo;
import com.coding5.el.class_comm.vo.CommentVo;
import com.coding5.el.common.page.PageVo;
import com.coding5.el.common.page.Pagination;
import com.google.gson.Gson;
//import com.sun.tools.classfile.InnerClasses_attribute.Info;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequestMapping("class")
@RequestMapping(value = "class", produces = "application/text; charset=utf8")
@Controller
public class ClassCommController {
	
	@Autowired
	private ClassCommService ccs;
	
	@Autowired
	private Gson gson;
	
	@GetMapping("writeModify")
	public String writeModify(String cNo, Model model) {
		
		ClassCommVo modifyData = ccs.modifyData(cNo);
		model.addAttribute("modifyData", modifyData);
		
		return "class_comm/write_modify";
	}
	
	@PostMapping("writeModify")
	public String writeModify(ClassCommVo classVo) {
		
		log.info("classVo :::" + classVo.getCateNo());
		int result = ccs.modify(classVo);
		
		if(classVo.getCateNo().equals("1")) {
			return "redirect:/class/qna";
		}else if(classVo.getCateNo().equals("2")) {
			return "redirect:/class/study";
		}else if (classVo.getCateNo().equals("3")){
			return "redirect:/class/free";
		}else {
			return "";
		}
		
		
	}
	
	//likeup ajax
	@PostMapping("likeupAjax")
	@ResponseBody
	public String likeupAjax(String memberNo, String classCommNo, HashMap<String, String> likeupMap) {
		 
		likeupMap.put("memberNo", memberNo);
		likeupMap.put("classCommNo", classCommNo);
		
		
		// 0 ?????? ???????????? , 1?????? ?????? ?????? ????????? // ???????????? ?????? == ????????? ???????????????? ????????? ?????? ???????????? ????????
		int likeCheckInt = ccs.likeCheck(likeupMap);
		log.info("likeCheck?????? :: " + likeCheckInt);
		String likeCheck = Integer.toString(likeCheckInt);
		
		likeupMap.put("likeCheck", likeCheck);
		
		
		//????????? ????????? ?????????
		String likeCntAjax = ccs.likeCntAjax(classCommNo);
		likeupMap.put("likeCntAjax", likeCntAjax);
		
//		String s = gson.toJson(r);
		log.info("likeupMap" + likeupMap);
		
		return gson.toJson(likeupMap);
	}
	
	//?????? ???????????????
	@GetMapping("reportSuccess")
	public String reportSuccess() {
		return "class_comm/reportSuccess";
	}
	
	//????????? ?????? (????????????)
	@PostMapping("deleteAjax")
	public String deleteAjax(String classCommNo) {
		
		int result = ccs.deleteWrite(classCommNo);
		
		log.info("result ::" + result);
		
		
		return "class_comm/qna";
	}

	//????????? ??????(??????)
	@GetMapping("write")
	public String write() {
		return "class_comm/comm_write";
	}
	
	//????????? ??????
	@PostMapping("write")
	public String write(ClassCommVo vo) {
		
		int result = ccs.write(vo);
		
		if(result != 1) {
			return "common/error";
		}
		
		return "redirect:/class/qna";
	}
	
	//?????? ??????
	@GetMapping("qna")
	public String qna(Model model) {
		
		List<ClassCommVo> qnaList = ccs.qnaList();
		
		//?????? ?????? ?????????
		List<ClassCommVo> qnaCommentList = ccs.qnaCommentList(qnaList);
		log.info("qnaCommentList :: " + qnaCommentList);
		model.addAttribute("qnaCommentList", qnaCommentList);
		
		
		
		log.info("???????????? ?????????" + qnaList);
		model.addAttribute("qnaList", qnaList);
		
		
		
		if(qnaList == null) {
			return "common/error";
		}
		
		return "class_comm/qna";
	}
	
	//????????? ??????
	@PostMapping("qna")
	public String qna() {
		

		
		return "class_comm/qna";
	}
	
	//????????? ??????(??????)
	@GetMapping("detail")
	public String detail(String classCommNo, String lc, Model model, HashMap<String, String> likeMap) {
		
		//????????? ??????
		int result = ccs.increaseHit(classCommNo);
		
		log.info("lc ::" + lc);
		likeMap.put("classCommNo", classCommNo);
		likeMap.put("memberNo", lc);
		log.info("likeMap ::" + likeMap);
		
		ClassCommVo detailVo = ccs.detailVo(likeMap);
		log.info("??????????????????" + detailVo);
		
		model.addAttribute("detailVo", detailVo);
		
		
		
		//?????? ?????? ??????
		List<CommentVo> commentList = ccs.commentList(classCommNo);
		log.info("???????????????" + commentList);
		model.addAttribute("commentList", commentList);
		model.addAttribute("classCommNo", classCommNo);
		
		return "class_comm/detail";
	}
	
	//?????? ????????????

	@PostMapping("commentAjax")
	@ResponseBody
	public String comment(String content, String memberNo, String classCommNo) {
		
		//?????? ?????????
		CommentVo vo = new CommentVo();
		vo.setComContent(content);
		vo.setComWriterNo(memberNo);
		vo.setComCommentNo(classCommNo);
		
		int result = ccs.writeComment(vo);
		log.info("vo ::" + vo);
		
		//?????? ?????????
//		List<CommentVo> commentOne = ccs.commemtOne(vo);
		List<CommentVo> commentList = ccs.commentList(classCommNo);
		
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("commentList", commentList);
		String commentListString = gson.toJson(commentList);
		
		log.info("commentOneString ::" + commentListString);
		
		
		return commentListString;
		
	}
	
	//??????(??????) >> ?????? ?????? ??? ??? ???!
	@GetMapping("report")
	public String report(String blacklist, String refortTitle, Model model) {
		
		log.info("???????????????" + blacklist);
		log.info("??? ??????" + refortTitle);

	    Gson gson = new Gson();
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    
	    // key-value ????????? ?????? ??????
	    map.put("blacklist", blacklist);
	    
	    model.addAttribute("reportMap", map);
	   
//	   // ?????? JSON Object??? ????????? ?????? ???????????? ??????
//	    String jsonString = gson.toJson(map);
		
		return "class_comm/report_write";
		
	}
	

	
	//????????????
	@PostMapping("reportInfo")
	public String reportInfo(String blacklistNo, String accusor, String board, Model model) {
		
		ClassCommVo reportVo = new ClassCommVo();
		reportVo.setBlacklistNo(blacklistNo);
		reportVo.setAccusor(accusor);
		reportVo.setBoard(board);
		log.info("reportVo :: " + reportVo);
		
		int result = ccs.reportInfo(reportVo);
		
		log.info("result :: " + result);
		
		
		
		return "class_comm/report_write";
	}
	
	//??????
	@PostMapping("report")
	
	public String report(String blacklistNo, String accusor, String board, String type, HashMap<String, String> reportMap ) {
		
		ClassCommVo reportInfo = ccs.selectReportInfo();
		
		reportMap.put("blacklistNo", reportInfo.getBlacklistNo());
		reportMap.put("accusor", reportInfo.getAccusor());
		reportMap.put("board", reportInfo.getBoard());
		reportMap.put("type", type);
		reportMap.put("cate_no", "1");
		
		int reportResult = ccs.insertReport(reportMap);
		

		
		log.info("?????? :: "+type);
		log.info("reportInfo :: "+reportInfo);

		
		return "class_comm/reportSuccess";
	}
	


	
	//?????????
	@GetMapping("study")
	public String study(String orderBy, String pNo, String keyword,  Model model, Map<String, String> search) {
		
		
		//????????? ?????????
		if(pNo == null) {
			pNo = "1";
		}
		
		String commCateNo = "2";
		
		search.put("keyword", keyword);
		search.put("commCateNo", commCateNo);
		search.put("orderBy", orderBy);
		
		log.info("??? : " + search);
		
		
		
		//PageVo ?????? ?????????
		int listCount = ccs.selectCnt(search);
		int currentPage =  Integer.parseInt(pNo);
		int pageLimit = 5;
		int boardLimit = 3;
		
		PageVo pv = Pagination.getPageVo(listCount, currentPage, pageLimit, boardLimit);
		
		
		
		List<ClassCommVo> studyList = ccs.studyList( pv, search);
		
		List<String> studyCommentCountList = ccs.studyCommentCountList(studyList);
		
		
		log.info("studyCommentCountList?????????" + studyCommentCountList);
		log.info("pv :: " + pv);
		model.addAttribute("studyList", studyList);
		model.addAttribute("studyCommentCountList", studyCommentCountList);
		model.addAttribute("pv", pv);
		model.addAttribute("search", search);
		
		if(studyList == null) {
			return "common/error";
		}
		return "class_comm/study";
	}
	
	//???????????????
	@GetMapping("free")
	public String free(String orderBy, Model model) {
		
		List<ClassCommVo> freeList = ccs.freeList(orderBy);
//		log.info("?????????" + studyList);
		model.addAttribute("freeList", freeList);
		if(freeList == null) {
			return "common/error";
		}
		
		return "class_comm/free";
	}
	
	


	
	//????????????
	@GetMapping("sidebar")
	public String sidebar() {
		return "class_comm/sidebar";
	}
	
	
}
