package pl.stalkon.ad.core.model.service;



import org.imgscalr.Scalr.Mode;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface FileService {
	public String saveImageFile(CommonsMultipartFile multipartFile, int width,
			int height, Mode mode);

	public void validateFile(BindingResult bindingResult,
			CommonsMultipartFile multipartFile, String objectName);

	public String getPath(String folder, String filePrefix, String fileSuffix,
			String id);

	public boolean validateFile(CommonsMultipartFile multipartFile);
}
