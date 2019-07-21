package org.kang.bill.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    /**
     * 导出pdf
     * @author Changhai
     * @param response
     * @return
     * 浏览器访问：www.localhost:8080/exportpdf
     */
    @RequestMapping(value={"/pdf"})
    public String exportPdf(HttpServletResponse response) throws UnsupportedEncodingException {
        // 指定解析器
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        //String path = request.getSession().getServletContext().getRealPath("/upload/");
        String filename="练习2.pdf";
        String path="C:\\Users\\Dell\\Desktop\\小图标\\qr";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + URLEncoder.encode(filename, "UTF-8"));
        OutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        try {
            os = response.getOutputStream();
            // 2 读入pdf表单
            reader = new PdfReader(path+ "/"+filename);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            /*BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);*/
            BaseFont bf = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H", false);
            form.addSubstitutionFont(bf);
            // 6查询数据================================================
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("name", "康怀安");
            data.put("sex", "男");
            data.put("age", "21");
            data.put("phone", "17629316947");
            data.put("email", "1468140258@qq.com");
            data.put("card", "415725");

            // 7遍历data 给pdf表单表格赋值
            for (String key : data.keySet()) {
                form.setField(key,data.get(key).toString());
            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            ps.setFormFlattening(true);

            //-----------------------------pdf 添加图片----------------------------------
            // 通过域名获取所在页和坐标，左下角为起点
            System.out.println("pdf 添加图片");
            String imgpath="C:\\Users\\Dell\\Desktop\\小图标\\qr\\92021339.jpg";
            int pageNo = form.getFieldPositions("icon").get(0).page;
            Rectangle signRect = form.getFieldPositions("icon").get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            // 读图片
            Image image = Image.getInstance(imgpath);
            // 获取操作的页面
            PdfContentByte under = ps.getOverContent(pageNo);
            // 根据域的大小缩放图片
            image.scaleToFit(signRect.getWidth(), signRect.getHeight());
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);

            //-------------------------------------------------------------
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
           System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
/*
美女主播qq：143822816
 */
