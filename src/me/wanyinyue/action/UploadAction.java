package me.wanyinyue.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import me.wanyinyue.model.Tab;
import me.wanyinyue.model.TabPic;
import me.wanyinyue.model.User;
import me.wanyinyue.service.TabManager;
import me.wanyinyue.service.TabPicManager;
import me.wanyinyue.utils.ImageUtils;
import me.wanyinyue.utils.MySessionContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "uploadAction")
@Scope(value = "prototype")
public class UploadAction extends BaseAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3993289307845760969L;

	private String name;
	private String singer;
	private String Filedata;
	private String total;
	private String order;
	private String sess;

	private TabManager tabManager;
	private TabPicManager tabPicManager;

	private int tabId;
	private Tab tab;
	private TabPic tabPic;

	private ImageUtils imageUtils;

	private static String BASEPATH = ServletActionContext.getServletContext()
			.getRealPath("/");

	public String uploadTab() throws Exception {
		User u = null;
		HttpSession session = null;
		if (sess != null) {
			session = MySessionContext.getSession(sess);
		} else {
			session = ServletActionContext.getRequest().getSession();
			MySessionContext.addSession(session);
			sess = session.getId();
		}
		if (session.getAttribute("USER") != null)
			u = (User) session.getAttribute("USER");
		else
			return "login";
		if (name != null && singer != null && total != null && order != null
				&& Filedata != null) {
			if (order.equals("1")) {
				tab = new Tab();
				tab.setName(name);
				tab.setSinger(singer);
				tab.setTotalPicNum(Integer.parseInt(total));
				tab.setUploadUser(u);
				if (u.getId() == 1)
					tab.setFeatured(1);
				else
					tab.setFeatured(0);
				tabManager.addTab(tab);
			} else {
				String hql = "from me.wanyinyue.model.Tab t where t.name='"
						+ name + "' and t.singer='" + singer
						+ "' and t.tabPic.size <" + total + " and t.uploadUser ="
						+ u.getId();
				List<Tab> tabs = tabManager.find(hql);
				tab = tabs.get(0);
			}
			tabPic = new TabPic();
			tabId = tab.getId();
			String src = tabId + "_" + order + ".jpg";
			String thumb = tabId + "_thumb" + ".jpg";
			tabPic.setSrc(src);
			tabPic.setTab(tab);
			File srcFile = new File(Filedata);
			String uploadPath = BASEPATH + "tabs";
			File toFile = new File(uploadPath, src);
			File toThumbFile = new File(uploadPath, thumb);
			BufferedImage bi = ImageIO.read(srcFile);
			tabPic.setHeight(bi.getHeight());
			tabPic.setWidth(bi.getWidth());
			tabPicManager.addTabPic(tabPic);
			imageUtils.saveSourceFile(srcFile, toFile);
			if (order.equals("1"))
				imageUtils.saveThumb(328, 482, toFile, toThumbFile);
			return "success";
		} else {
			return "input";
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getFiledata() {
		return Filedata;
	}

	public void setFiledata(String filedata) {
		Filedata = filedata;
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	@Resource(name = "tabManager")
	public void setTabManager(TabManager tabManager) {
		this.tabManager = tabManager;
	}

	public TabPicManager getTabPicManager() {
		return tabPicManager;
	}

	@Resource(name = "tabPicManager")
	public void setTabPicManager(TabPicManager tabPicManager) {
		this.tabPicManager = tabPicManager;
	}

	public ImageUtils getImageUtils() {
		return imageUtils;
	}

	@Resource(name = "imageUtils")
	public void setImageUtils(ImageUtils imageUtils) {
		this.imageUtils = imageUtils;
	}

	public String getSess() {
		return sess;
	}

	public void setSess(String sess) {
		this.sess = sess;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}
	

}