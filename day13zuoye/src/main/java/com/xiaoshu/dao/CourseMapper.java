package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.entity.Course;

public interface CourseMapper {

	List<Course> findByAll();

	void addCourse(Course course);

}
