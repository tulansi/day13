package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.entity.Course;

@Service
public class CourseService {

	@Autowired
	private CourseMapper cm;
	
	public List<Course> findByAll() {
		// TODO Auto-generated method stub
		return cm.findByAll();
	}

	public void addCourse(Course course) {
		// TODO Auto-generated method stub
		cm.addCourse(course);
	}

}
