package pl.stalkon.ad.core.model.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import pl.stalkon.ad.core.model.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private static final List<String> extensions = Arrays.asList("png", "jpg",
			"jpeg", "gif");

	private String filesRootPath;
	private String rootFolder;

	public FileServiceImpl(String filesRootPath, String rootFolder) {
		super();
		this.filesRootPath = filesRootPath;
		this.rootFolder = rootFolder;
	}

	public FileServiceImpl() {

	}

	private Logger logger = Logger.getLogger(FileServiceImpl.class);

	@Autowired
	private ServletContext servletContext;

	@Override
	public String saveImageFile(CommonsMultipartFile multipartFile, int width,
			int height, Mode mode) {
		try {
			BufferedImage scaledImage = getScaledImage(multipartFile, width,
					height, mode);
			String relativePath = saveImageFile(scaledImage,
					multipartFile.getOriginalFilename());
			return relativePath;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	private BufferedImage getScaledImage(CommonsMultipartFile multipartFile,
			int width, int height, Mode mode) throws IOException {
		BufferedImage img = ImageIO.read(multipartFile.getInputStream());
		BufferedImage scaledImg = Scalr.resize(img, mode, width,
				height);
		return scaledImg;
	}

	private String saveImageFile(BufferedImage image, String originalStringName)
			throws IOException {
		String ext = getExtension(originalStringName);
		String relativePath = getRelativePath(ext);
		File file = new File(filesRootPath + relativePath);
		file.mkdirs();
		ImageIO.write(image, ext, file);
		return relativePath;
	}

	private String getRelativePath(String ext) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		String fileName = getFileName();
		return rootFolder + "/" + year + "/" + month + "/" + fileName + "."
				+ ext;
	}

	private String getFileName() {
		Long time = new Date().getTime();
		String random = RandomStringUtils.random(6, true, false);
		return time + "_" + random;
	}

	@Override
	public void validateFile(BindingResult bindingResult,
			CommonsMultipartFile multipartFile, String objectName) {
		if (!validateFile(multipartFile)) {
			bindingResult.addError(new ObjectError(objectName,
					new String[] { "{constraints.file.type}" },
					new Object[] {}, "Wrong file type"));
		}
	}

	private String getExtension(String fileName) {
		return FilenameUtils.getExtension(fileName);
	}

	@Override
	public String getPath(String folder, String filePrefix, String fileSuffix,
			String id) {
		String path = null;
		if (fileSuffix != null) {
			path = servletContext.getRealPath("resources") + "/" + folder + "/"
					+ filePrefix + "-" + id + "-" + fileSuffix;
		} else {
			path = servletContext.getRealPath("resources") + "/" + folder + "/"
					+ filePrefix + "-" + id;
		}
		return path;
	}

	@Override
	public boolean validateFile(CommonsMultipartFile multipartFile) {
		if (extensions.contains(getExtension(multipartFile
				.getOriginalFilename())))
			return true;
		return false;

	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}
}
