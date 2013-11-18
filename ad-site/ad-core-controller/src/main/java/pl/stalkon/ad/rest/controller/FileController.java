package pl.stalkon.ad.rest.controller;

import java.util.HashMap;
import java.util.Map;


import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import pl.stalkon.ad.core.model.service.BrandService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.ad.extensions.UploadingFileException;

@Controller
public class FileController {

	@Autowired
	private FileService fileService;

	@Autowired
	private UserService userService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ContestService contestService;

	public FileController() {
	}

	@RequestMapping(value = "user/{userId}/upload/image", method = RequestMethod.POST)
	@PreAuthorize("principal.id.equals(#userId)")
	@ResponseBody
	public String uploadAvatar(@PathVariable("userId") Long userId,
			@RequestParam("file") CommonsMultipartFile commonsMultipartFile)
			throws UploadingFileException {
		if (fileService.validateFile(commonsMultipartFile)) {
			if (!commonsMultipartFile.isEmpty()) {
				String relativePath = fileService.saveImageFile(
						commonsMultipartFile, 43, 43, Mode.FIT_EXACT);
				if (relativePath != null) {
					userService.setUserThumbnail(relativePath, userId);
					return relativePath;
				}
			}
		}
		throw new UploadingFileException();
	}

	@RequestMapping(value = "brand/{brandId}/upload/image", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isUserBrandOwner(principal.id, #brandId)")
	@ResponseBody
	public Map<String, String> uploadBrandLogo(
			@RequestParam("file") CommonsMultipartFile commonsMultipartFile,
			@PathVariable("brandId") Long brandId)
			throws UploadingFileException {
		if (fileService.validateFile(commonsMultipartFile)) {
			if (!commonsMultipartFile.isEmpty()) {
				String relativePathBig = fileService.saveImageFile(
						commonsMultipartFile, 250, 90, Mode.FIT_TO_HEIGHT);
				String relativePathSmall = fileService.saveImageFile(
						commonsMultipartFile, 120, 40, Mode.FIT_TO_HEIGHT);
				if (relativePathBig != null && relativePathSmall != null) {
					brandService.setBrandLogo(relativePathBig,
							relativePathSmall, brandId);
					Map<String, String> map = new HashMap<String, String>(2);
					map.put("big", relativePathBig);
					map.put("small", relativePathSmall);
					return map;
				}
			}
		}
		throw new UploadingFileException();
	}

	@RequestMapping(value = "contest/{contestId}/upload/image", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isContestAdmin(principal.id, #contestId)")
	@ResponseBody
	public String uploadContestImage(
			@RequestParam("file") CommonsMultipartFile commonsMultipartFile,
			@PathVariable("contestId") Long contestId)
			throws UploadingFileException {
		if (fileService.validateFile(commonsMultipartFile)) {
			if (!commonsMultipartFile.isEmpty()) {
				String relativePath = fileService.saveImageFile(
						commonsMultipartFile, 120, 120, Mode.FIT_TO_HEIGHT);
				if (relativePath != null) {
					contestService.setContestImage(relativePath, contestId);
					return relativePath;
				}
			}
		}
		throw new UploadingFileException();
	}

//	@ExceptionHandler(UploadingFileException.class)
//	// @ResponseStatus(HttpStatus.BAD_REQUEST)
//	private ResponseEntity<String> sendAjaxErrorResponse() {
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.setContentType(MediaType.TEXT_PLAIN);
//		return new ResponseEntity<String>("error uploading file",
//				responseHeaders, HttpStatus.BAD_REQUEST);
//	}

}
