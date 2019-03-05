package com.yimukeji.yuelaoge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;
import com.yimukeji.yuelaoge.api.Domain;
import com.yimukeji.yuelaoge.data.MemberDao;
import com.yimukeji.yuelaoge.data.MemberDaoImpl;

/**
 * Servlet implementation class ApiServlet
 */
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 定义限制文件大小：
	private int maxSize = 1024 * 1024 * 100;// 102400KB以内(100MB)
	// 要保存到web目录下的哪个路径下
	private String uploadDectory = "uploads";
	private String uploadCacheDectory = "WEB-INF/cache";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		Domain<JSONObject> domain = uploadAvatar(request);
		response.getWriter().append(domain.toJson().toJSONString());
	}

	private Domain<JSONObject> uploadAvatar(HttpServletRequest request) {
		Domain<JSONObject> domain = new Domain<JSONObject>();
		String savePath = getServletContext().getRealPath(uploadDectory);
		File saveFileDir = new File(savePath);
		if (!saveFileDir.exists()) {
			saveFileDir.mkdirs();
		}
		String tmpPath = getServletContext().getRealPath(uploadCacheDectory);
		File tmpFile = new File(tmpPath);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		String userid = "";
		String avatarName = "";
		try {
			// 1.创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置上传时生成的临时文件的保存目录
			factory.setRepository(tmpFile);
			// 2.创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 监听文件上传进度
			upload.setProgressListener(new ProgressListener() {
				@Override
				public void update(long readedBytes, long totalBytes, int currentItem) {
					// TODO Auto-generated method stub
					System.out.println("当前已处理：" + readedBytes + "-----------文件大小为：" + totalBytes + "--" + currentItem);
				}
			});
			if (!ServletFileUpload.isMultipartContent(request)) {
				domain.msg = "非表单提交头像！";
				return domain;
			}
			// 设置上传单个文件的最大值
			upload.setFileSizeMax(maxSize);
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					if (name.equals("userid")) {
						userid = item.getString();
					}
				} else {
					String fileName = item.getName();
					System.out.println("文件名：" + fileName);
					if (fileName == null && fileName.trim().length() == 0) {
						domain.msg = "文件名不存在";
						continue;
					}
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					// 检查文件大小
					if (item.getSize() == 0 || item.getSize() > maxSize) {
						domain.msg = "文件大小不正确";
						continue;
					}
					// 得到存文件的文件名
					String saveFileName = makeFileName(fileName);
					// 保存文件方法一// 获取item中的上传文件的输入流
					InputStream is = item.getInputStream();
					// 创建一个文件输出流
					FileOutputStream out = new FileOutputStream(savePath + "\\" + saveFileName);
					// 创建一个缓冲区
					byte buffer[] = new byte[1024];
					// 判断输入流中的数据是否已经读完的标致
					int len = 0;
					while ((len = is.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					// 关闭输出流
					out.close();
					// 关闭输入流
					is.close();
					// 删除临时文件
					item.delete();
					avatarName = saveFileName;
				}
			}

		} catch (FileSizeLimitExceededException e) {
			domain.msg = "文件大小超过限制";
			e.printStackTrace();
		} catch (Exception e) {
			domain.msg = "上传异常";
			e.printStackTrace();
		}

		if (userid == null || userid.isEmpty()) {
			domain.msg = "用户ID不存在";
			return domain;
		}
		if (avatarName == null || avatarName.isEmpty()) {
			return domain;
		}
		int id = -1;
		try {
			id = Integer.parseInt(userid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (id < 0) {
			domain.msg = "用户ID不正确";
			return domain;
		}

		MemberDao dao = new MemberDaoImpl();

		if (dao.updateAvatar(id, avatarName)) {
			domain.code = 1;
			domain.msg = "更新成功！";
		} else {
			domain.code = 0;
			domain.msg = "更新失败";
		}

		return domain;

	}

	private String makeFileName(String fileName) {
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString().replaceAll("-", "") + "_" + fileName;
	}
}
