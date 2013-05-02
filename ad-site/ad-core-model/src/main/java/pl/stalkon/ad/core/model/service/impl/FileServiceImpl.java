package pl.stalkon.ad.core.model.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
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
			"gif");

	@Autowired
	private ServletContext servletContext;

	@Override
	public String saveFile(String fileName, CommonsMultipartFile multipartFile,
			int width, int height) throws IOException {
		String ext = getExtension(multipartFile.getOriginalFilename());
		String fileNameToCreate = fileName + "." + ext;
		File file = new File(fileNameToCreate);
		BufferedImage img = ImageIO.read(multipartFile.getInputStream());
		BufferedImage scaledImg = Scalr.resize(img, Mode.FIT_TO_HEIGHT, width,
				height);
		ImageIO.write(scaledImg, ext, file);
		return ext;
	}

	@Override
	public void validateFile(BindingResult bindingResult,
			CommonsMultipartFile multipartFile, String objectName, long maxSize) {
		// if (multipartFile.getSize() > maxSize) {
		// bindingResult.addError(new ObjectError(objectName,
		// new String[] {"{constraints.file.size}"}, new Object[] {},
		// "File too big"));
		// }
		// if (!extensions.contains(getExtension(multipartFile
		// .getOriginalFilename()))) {
		// bindingResult.addError(new ObjectError(objectName,
		// new String[] {"{constraints.file.type}"}, new Object[] {},
		// "Wrong file type"));
		// }
	}

	private String getExtension(String fileName) {
		String extension = fileName.replaceAll("^.*\\.", "");
		return extension;
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
}
