package pl.stalkon.ad.core.model.service;

import java.io.IOException;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface FileService {
	public String saveFile(String fileName, CommonsMultipartFile multipartFile, int width, int height)throws IOException ;
	public void validateFile(BindingResult bindingResult, CommonsMultipartFile multipartFile,String objectName);
	public String getPath(String folder, String filePrefix,String fileSuffix, String id);
}
