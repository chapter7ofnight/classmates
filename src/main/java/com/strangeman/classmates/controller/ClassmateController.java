package com.strangeman.classmates.controller;

import com.strangeman.classmates.bean.Classmate;
import com.strangeman.classmates.bean.Member;
import com.strangeman.classmates.service.ClassmateService;
import com.strangeman.classmates.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/classmate")
public class ClassmateController {
    @Autowired
    private ClassmateService classmateService;

    @RequestMapping(value = "/myclassmates",method = RequestMethod.GET)
    public String myClassmates(){
        return "classmate/myclassmate";
    }

    @ResponseBody
    @RequestMapping(value = "/myclassmates",method = RequestMethod.POST)
    public ResultInfo myClassamtes(HttpSession session){
        ResultInfo result;

        Member member= (Member) session.getAttribute("member");
        if(member!=null&&!StringUtils.isEmpty(member.getId())){
            List<Classmate> classmates=classmateService.getClassmatesByUserId(member.getId());
            result=ResultInfo.success("").add("classmates",classmates);
        }
        else {
            result=ResultInfo.fail("无法获取用户数据");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/classmateDetail",method = RequestMethod.POST)
    public ResultInfo classmateDetail(HttpSession session,String classmateId){
        ResultInfo result;

        Member member= (Member) session.getAttribute("member");
        if(member!=null){
            Classmate classmate=classmateService.getCompleteClassmateById(classmateId);
            if(classmate!=null){
                if(haveReadPermission(member,classmate)){
                    result=ResultInfo.success("").add("classmate",classmate);
                }
                else{
                    result=ResultInfo.fail("无查看权限");
                }
            }
            else {
                result=ResultInfo.fail("无法查询该同学录数据");
            }
        }
        else{
            result=ResultInfo.fail("无法查询用户数据，请重新登录");
        }

        return result;
    }

    @RequestMapping(value = "/classmateDetail",method = RequestMethod.GET)
    public String classmateDetail(Model model,String classmateId){
        model.addAttribute("classmateId",classmateId);

        return "classmate/classmatedetail";
    }

    @ResponseBody
    @RequestMapping(value = "/createClassmate")
    public ResultInfo createClassmate(HttpSession session,Classmate classmate){
        Member member= (Member) session.getAttribute("member");
        String memberId;

        if(member==null){
            return ResultInfo.fail("查不到该用户数据，请重新登录");
        }
        else{
            memberId=member.getId();
        }

        if(classmate==null||StringUtils.isEmpty(classmate.getName())||StringUtils.isEmpty(classmate.getSchool())||StringUtils.isEmpty(classmate.getClazz())){
            return ResultInfo.fail("输入数据不完整，请重试");
        }
        else {
            classmate.setOwnerId(memberId);
        }

        if(classmateService.createClassmate(classmate)){
            return ResultInfo.success("创建同学录成功");
        }
        return ResultInfo.fail("创建同学录失败");
    }

    @ResponseBody
    @RequestMapping(value = "/photoWall",method = RequestMethod.POST)
    public ResultInfo getPhotoWall(HttpSession session,String classmateId){
        ResultInfo result;

        Member member= (Member) session.getAttribute("member");
        if(member==null) {
            result=ResultInfo.fail("查不到用户数据，请重新登录");
        }
        else {
            if (StringUtils.isEmpty(classmateId))
                result=ResultInfo.fail("同学录ID为空");
            else {
                Classmate classmate = classmateService.getSimpleClassmateById(classmateId);
                if (classmate != null) {
                    if (haveReadPermission(member, classmate)) {
                        result = ResultInfo.success("").add("classmate", classmate);
                    } else {
                        result = ResultInfo.fail("无该同学录查看权限");
                    }
                } else {
                    result = ResultInfo.fail("无法查询该同学录数据");
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/photoWall",method = RequestMethod.GET)
    public String getPhotoWall(Model model,String classmateId){
        model.addAttribute("classmateId",classmateId);

        return "classmate/photowall";
    }

    private boolean haveReadPermission(Member member,Classmate classmate) {
        return member != null && classmate != null && classmate.getOwnerId() != null && classmate.getOwnerId().equals(member.getId());
    }
}
