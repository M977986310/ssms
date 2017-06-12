package servlet;

import entity.Publish_info;
import entity.Stu;
import entity.Sub_credit;
import net.sf.json.JSONArray;
import service.CourseService;
import service.Publish_infoService;
import service.StuService;
import service.Sub_creditService;
import service.impl.CourseServiceImpl;
import service.impl.Publish_infoServiceImpl;
import service.impl.StuServiceImpl;
import service.impl.Sub_creditServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/6/7.
 */
@WebServlet(name = "StuServlet", urlPatterns = "/StuServlet")
public class StuServlet extends HttpServlet {

    private StuService stuService = new StuServiceImpl();
    private CourseService courseService = new CourseServiceImpl();
    private Publish_infoService publish_infoService = new Publish_infoServiceImpl();
    private Publish_info publish_info = new Publish_info();
    private Sub_credit sub_credit = new Sub_credit();
    private Sub_creditService sub_creditService = new Sub_creditServiceImpl();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        String method = req.getParameter("method");
        if (method.equals("login")) {
            login(req, resp);
        } else if (method.equals("showCourse")) {
            showCourse(req, resp);
        } else if (method.equals("showPublish_info")) {
            showPublish_info(req, resp);
        } else if (method.equals("showCourses")) {
            showCourses(req, resp);
        } else if (method.equals("showSubject")) {
            showSubject(req, resp);
        }


//        JSONArray jsonArray = JSONArray.fromObject(list);

//        resp.getWriter().print(jsonArray);

    }

    private void showSubject(HttpServletRequest req, HttpServletResponse resp) {
        String name = (String) req.getSession().getAttribute("stuName");
        Object number = req.getSession().getAttribute("stuNumber");
        System.out.println(number);
        List<Map<String,Object>> listInfo = sub_creditService.listInfo((Integer) number);
        JSONArray jsonArray = JSONArray.fromObject(listInfo);
        System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222");
        System.out.println(listInfo);
        try {
            resp.getWriter().print(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCourses(HttpServletRequest req, HttpServletResponse resp) {
        String name = (String) req.getSession().getAttribute("stuName");
        List<Map<String,Object>> listInfo = courseService.outCourse(name);
        JSONArray jsonArray = JSONArray.fromObject(listInfo);
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(jsonArray);
        try {
            resp.getWriter().print(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPublish_info(HttpServletRequest req, HttpServletResponse resp) {

        List<Map<String, Object>> listInfo = publish_infoService.outPublish_info();
        System.out.println(listInfo);
        JSONArray jsonArray = JSONArray.fromObject(listInfo);
        try {
            System.out.println(jsonArray);
            resp.getWriter().print(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showCourse(HttpServletRequest req, HttpServletResponse resp) {
        String stu_name = (String) req.getSession().getAttribute("stuName");
        List<Map<String, Object>> listCourse = courseService.outCourse(stu_name);
        JSONArray jsonArray = JSONArray.fromObject(listCourse);
        System.out.println("1111111111111111111111111111111111");
        System.out.println(jsonArray);
        try {
            System.out.println(listCourse);
            resp.getWriter().print(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(123);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int number = Integer.parseInt(req.getParameter("number"));
        String s_password = req.getParameter("password");
        Stu stu = new Stu();
        stu.setStu_number(number);
        stu.setPassword(s_password);
        Map<String, Object> mapInfo = stuService.infoMap(number);

        if (stu == null) {
            req.setAttribute("info", "登录失败！用户名与密码不能为空！");
            req.getRequestDispatcher("/jsp/login/login.jsp");
        } else {
            if (stuService.login(stu)) {
                req.getSession().setAttribute("stu",stu);
                req.getSession().setAttribute("stuName", mapInfo.get("stu_name"));
                req.getSession().setAttribute("stuType", mapInfo.get("type"));
                req.getSession().setAttribute("stuNumber",number);
                resp.sendRedirect("/jsp/stu/index.jsp");
            } else {
                req.setAttribute("info", "登录失败！请检查用户名与密码！");
                req.getRequestDispatcher("/jsp/login/login.jsp");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}