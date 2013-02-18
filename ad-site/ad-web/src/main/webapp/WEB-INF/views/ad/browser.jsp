<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<section class="demots">
	<article>
		<div class="ad ${ad.type}" id="ad${ad.id}">
			<h2 style="display: none;">${ad.adData.title}</h2>
			<div class="">
				<a href="/${ad.adData.title},1,${ad.id}" class="">
					<iframe width="848" height="480" frameborder="0" scrolling="no"
						src="http://api.dmcloud.net/player/embed/<user_id>/<media_id>?auth=<auth_token>"></iframe>
				</a>
			</div>
			<!-- .demot_pic -->

			<section class="fb_like_button">
				<div class="fb-like fb_edge_widget_with_comment fb_iframe_widget"
					data-href="http://demotywatory.pl/4053353" data-send="false"
					data-layout="button_count" data-width="130" data-show-faces="false"
					data-font="arial" fb-xfbml-state="rendered">
					<span style="height: 20px; width: 93px;"><iframe
							id="f3bf687488" name="f29c5007f" scrolling="no"
							style="border: none; overflow: hidden; height: 20px; width: 93px;"
							title="Like this content on Facebook." class="fb_ltr"
							src="http://www.facebook.com/plugins/like.php?api_key=167367663212&amp;locale=pl_PL&amp;sdk=joey&amp;channel_url=http%3A%2F%2Fstatic.ak.facebook.com%2Fconnect%2Fxd_arbiter.php%3Fversion%3D18%23cb%3Df19791ed9%26origin%3Dhttp%253A%252F%252Fdemotywatory.pl%252Ff3c58362fc%26domain%3Ddemotywatory.pl%26relation%3Dparent.parent&amp;href=http%3A%2F%2Fdemotywatory.pl%2F4053353&amp;node_type=link&amp;width=130&amp;font=arial&amp;layout=button_count&amp;colorscheme=light&amp;show_faces=false&amp;send=false&amp;extended_social_context=false"></iframe></span>
				</div>
			</section>
			<section class="share-widgets">
				<div class="votes">
					<a href="/demotivator/vote/4053353/up"
						onclick="votowanie(this); return false;" class="voteup"> Mocne
					</a> <a href="/demotivator/vote/4053353/down"
						onclick="votowanie(this); return false;" class="votedown">
						Słabe </a> <span> <strong class="upvotes">+267</strong> <small
						class="count"> (286) </small>
					</span> <span class="vote_result"></span>
				</div>
				<div class="external-share">
					<a class="share-facebook" href="javascript:void(0)"
						onclick="return fb_share('http://demotywatory.pl/4053353')"
						title="Wrzuć na facebooka"> </a> <a class="share-popkorn"
						href="http://popkorn.pl/dodaj?page_url=http%3A%2F%2Fdemotywatory.pl%2F4053353%2FZ-pamietnika-mlodego-ojca&amp;url=http%3A%2F%2Fdemotywatory.pl%2Fuploads%2F201302%2F1361050376_yeidwr_600.jpg&amp;utm_source=demotywatory&amp;utm_medium=addbutton"
						title="Dodaj do Popkorn.pl" target="_blank"> </a> <a
						class="share-stylowi"
						href="http://stylowi.pl/dodaj?page_url=http%3A%2F%2Fdemotywatory.pl%2F4053353%2FZ-pamietnika-mlodego-ojca&amp;url=http%3A%2F%2Fdemotywatory.pl%2Fuploads%2F201302%2F1361050376_yeidwr_600.jpg&amp;utm_source=demotywatory&amp;utm_medium=addbutton"
						title="Dodaj do Stylowi.pl" target="_blank"> </a>
				</div>
			</section>
			<nav>
				<div class="demot_info_stats">

					<!-- docelowo dla sekcji specjalnych -->
					<div class="demot_extra_area"></div>
					<!-- docelowo dla sekcji specjalnych end -->


					<ul>
						<li><time datetime="16 lutego 2013 o 22:32">16 lutego
								2013 o 22:32</time> przez <a href="/user/RAFALOSKAR">RAFALOSKAR</a></li>

						<script>
							if (user_id > 0 && user_id != 829952) {
								document
										.write('<li><span id="obs_info_4053353"><a class="observe" href="javascript:void(0)" onClick="observe(\'RAFALOSKAR\', \'add\', \'4053353\')">obserwuj</a></span></li>');
							}
						</script>

						<li><a href="http://demotywatory.pl/4053353#comments"
							class="comment" style="">Skomentuj (2)</a></li>

						<li><a href="/index.php/user/add_favorite/4053353"
							class="favorite"> Do ulubionych </a></li>

					</ul>
					<br>
				</div>
				<!-- .demot_info_stats -->
			</nav>

			<div class="true_demotivator"></div>

			<div class="admin_menu"></div>

		</div>
		<!-- .demotivator -->
	</article>
</section>