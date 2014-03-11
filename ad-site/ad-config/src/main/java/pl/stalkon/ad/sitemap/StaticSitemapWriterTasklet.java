package pl.stalkon.ad.sitemap;

import java.util.Date;

import org.apache.commons.exec.ExecuteException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl.Options;

import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.stalkon.ad.rest.controller.AdController;
import pl.stalkon.ad.rest.controller.ContestController;

public class StaticSitemapWriterTasklet implements Tasklet {

	@Autowired
	private WebSitemapGeneratorWrapper sitemapGeneratorWrapper;

	@Autowired
	private AdService adService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private PhantomJSCaller phantomJSCaller;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		AdSearchDto adSearchDto = new AdSearchDto();
		adSearchDto.setPlace(Place.MAIN);
		AdBrowserWrapper adBrowserWrapper = adService.get(adSearchDto,
				new Paging(1, AdController.AD_PER_PAGE), true);
		Long adTotal = adBrowserWrapper.getTotal();
		iterate("glowna",
				getPageCount(adTotal.intValue(), AdController.AD_PER_PAGE));

		adSearchDto.setPlace(Place.WAITING);
		adBrowserWrapper = adService.get(adSearchDto, new Paging(1,
				AdController.AD_PER_PAGE), true);
		adTotal = adBrowserWrapper.getTotal();
		iterate("poczekalnia",
				getPageCount(adTotal.intValue(), AdController.AD_PER_PAGE));

		ContestBrowserWrapper contestBrowserWrapper = contestService
				.get(new Paging(1, ContestController.CONTESTS_PER_PAGE));
		iterate("konkursy",
				getPageCount(contestBrowserWrapper.getTotal().intValue(),
						ContestController.CONTESTS_PER_PAGE));

		phantomJSCaller.write("marki");
		sitemapGeneratorWrapper.getGenerator().addUrl(new WebSitemapUrl(new Options(
				sitemapGeneratorWrapper.getBaseUrl() + "#!/marki").changeFreq(
				ChangeFreq.ALWAYS).lastMod(new Date())));
		return RepeatStatus.FINISHED;
	}

	private void iterate(String folder, int count) throws Exception {
		for (int i = 1; i <= count; i++) {
			sitemapGeneratorWrapper.getGenerator()
					.addUrl(new WebSitemapUrl(new Options(
							sitemapGeneratorWrapper.getBaseUrl() + "#!/"
									+ folder + "?page=" + i).changeFreq(
							ChangeFreq.ALWAYS).lastMod(new Date())));
			phantomJSCaller.write(folder + "?page=" + i);
		}
	}

	private int getPageCount(int itemsCount, int itemsPerPage) {
		return (int) Math.ceil(((float) itemsCount) / ((float) itemsPerPage));
	}

}
