package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Log;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.CourseService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.StudentService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("student")
public class StudentController extends LogController{
	static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private StudentService ss;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("studentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Course> cList = cs.findByAll();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("cList", cList);
		return "student";
	}
	
	
	@RequestMapping(value="studentList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit,StudentVo sv) throws Exception{
		try {
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StudentVo> page= ss.findUserPage(sv,pageNum,pageSize,ordername,order);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",page.getTotal() );
			jsonObj.put("rows", page.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	// 新增或修改
		@RequestMapping("reserveStu")
		public void reserveUser(HttpServletRequest request,Student student,HttpServletResponse response){
			Integer sid = student.getSid();
			JSONObject result=new JSONObject();
			try {
				if (sid != null) {   // userId不为空 说明是修改
						student.setSid(sid);
						ss.updateStu(student);
						result.put("success", true);
					
				}else {   // 添加
						ss.addStu(student);
						result.put("success", true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("保存用户信息错误",e);
				result.put("success", true);
				result.put("errorMsg", "对不起，操作失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		// 添加专业
		@RequestMapping("addCourse")
		public void addCourse(HttpServletRequest request,Course course,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
					cs.addCourse(course);
					result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("保存用户信息错误",e);
				result.put("success", true);
				result.put("errorMsg", "对不起，操作失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		
		@RequestMapping("deleteStu")
		public void delUser(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				String[] ids=request.getParameter("ids").split(",");
				for (String id : ids) {
					ss.deleteStu(Integer.parseInt(id));
				}
				result.put("success", true);
				result.put("delNums", ids.length);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("删除用户信息错误",e);
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		
		@RequestMapping("echartsStu")
		public void echartsStu(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				List<StudentVo> list=ss.echartdFindAll();
				result.put("success", true);
				result.put("data", list);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("删除用户信息错误",e);
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		@RequestMapping("exportStu")
		public void exportStu(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
				HSSFSheet sheet = wb.createSheet("操作记录备份");//第一个sheet
				HSSFRow rowFirst = sheet.createRow(0);//第一个sheet第一行为标题
				String [] handers={"学生编号","学生姓名","学生年龄","课程编号","所属年级","入校时间","创建时间","所选课程"};
				for (int i = 0; i < handers.length; i++) {
					rowFirst.createCell(i).setCellValue(handers[i]);
				}
				
				List<StudentVo> list=ss.findAll(new StudentVo());
				
				for (int i = 0; i < list.size(); i++) {
					StudentVo vo=list.get(i);
					HSSFRow row = sheet.createRow(i+1);
					row.createCell(0).setCellValue(vo.getSid());
					row.createCell(1).setCellValue(vo.getSname());
					row.createCell(2).setCellValue(vo.getAge());
					row.createCell(3).setCellValue(vo.getScode());
					row.createCell(4).setCellValue(vo.getGrade());
					row.createCell(5).setCellValue(TimeUtil.formatTime(vo.getEntrytime(), "yyyy-MM-dd"));
					row.createCell(6).setCellValue(TimeUtil.formatTime(vo.getCreatetime(), "yyyy-MM-dd"));
					row.createCell(7).setCellValue(vo.getCname());
					
				}
				
				OutputStream os;
				File file = new File("D:\\aaa\\导出.xls");
				
				if (!file.exists()){//若此目录不存在，则创建之  
					file.createNewFile();  
					logger.debug("创建文件夹路径为："+ file.getPath());  
	            } 
				os = new FileOutputStream(file);
				wb.write(os);
				os.close();
				
				result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("删除用户信息错误",e);
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
}
