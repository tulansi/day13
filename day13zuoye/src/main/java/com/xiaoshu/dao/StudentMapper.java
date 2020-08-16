package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;

public interface StudentMapper {

	List<StudentVo> findUserPage(StudentVo sv);

	void updateStu(Student student);

	void addStu(Student student);

	void deleteStu(int parseInt);

	List<StudentVo> echartdFindAll();

}
