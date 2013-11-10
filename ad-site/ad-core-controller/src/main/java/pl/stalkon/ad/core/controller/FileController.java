package pl.stalkon.ad.core.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import pl.stalkon.ad.core.model.service.BrandService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.extensions.UploadingFileException;

@Controller
public class FileController {

	@Autowired
	private FileService fileService;

	@Autowired
	private UserService userService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ContestService contestService;

	public FileController() {
	}

	@RequestMapping(value = "user/upload/image", method = RequestMethod.POST)
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> uploadAvatar(
			@RequestParam("file") CommonsMultipartFile commonsMultipartFile,
			Principal principal) throws UploadingFileException {
		if (fileService.validateFile(commonsMultipartFile)) {
			if (!commonsMultipartFile.isEmpty()) {
				String relativePath = fileService.saveImageFile(
						commonsMultipartFile, 43, 43, Mode.FIT_EXACT);
				if (relativePath != null) {
					SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
							.getPrincipal();
					userService.setUserThumbnail(relativePath,
							socialLoggedUser.getId());
					controllerHelperBean.reathenticateUser(socialLoggedUser
							.getUsername());
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.setContentType(MediaType.TEXT_PLAIN);
					return new ResponseEntity<String>(relativePath,
							responseHeaders, HttpStatus.OK);
				}
			}
		}
		throw new UploadingFileException();
	}

	@RequestMapping(value = "brand/{brandId}/upload/image", method = RequestMethod.POST)
	// @ResponseBody
//	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> uploadBrandLogo(
			@RequestParam("file") CommonsMultipartFile commonsMultipartFile,
			Principal principal, @PathVariable("brandId") Long brandId,
			HttpServletRequest request, HttpServletResponse res)
			throws UploadingFileException {
		if (!controllerHelperBean.isUserBrandOwner(request, principal,
				brandId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
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
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.setContentType(MediaType.TEXT_PLAIN);
					return new ResponseEntity<String>(map.toString(),
							responseHeaders, HttpStatus.OK);
				}
			}
		}
		throw new UploadingFileException();
	}

	@RequestMapping(value = "contest/{contestId}/upload/image", method = RequestMethod.POST)
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> uploadContestImage(
			@RequestParam("file") CommonsMultipartFile commonsMultipartFile,
			Principal principal, @PathVariable("contestId") Long contestId,
			HttpServletRequest request) throws UploadingFileException {
		if (!controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		if (fileService.validateFile(commonsMultipartFile)) {
			if (!commonsMultipartFile.isEmpty()) {
				String relativePath = fileService.saveImageFile(
						commonsMultipartFile, 120, 120, Mode.FIT_TO_HEIGHT);
				if (relativePath != null) {
					contestService.setContestImage(relativePath, contestId);
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.setContentType(MediaType.TEXT_PLAIN);
					return new ResponseEntity<String>(relativePath,
							responseHeaders, HttpStatus.OK);
				}
			}
		}
		throw new UploadingFileException();
	}

	@ExceptionHandler(UploadingFileException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<String> sendAjaxErrorResponse() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>("error uploading file",
				responseHeaders, HttpStatus.BAD_REQUEST);
	}

}
