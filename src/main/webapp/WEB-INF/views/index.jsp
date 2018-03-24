<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@include file="/WEB-INF/views/templates/header.jsp"%>


<!-- Carosel
================================================== -->
<div id="myCarousel" class="carousel slide" data-ride="carousel">
	<!-- Indicators -->
	<ol class="carousel-indicators">
		<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
		<li data-target="#myCarousel" data-slide-to="1"></li>
		<li data-target="#myCarousel" data-slide-to="2"></li>
	</ol>
	<div class="carousel-inner" role="listbox">
		<div class="item active">
			<img class="first-slide base-image"
				src="resources/site-images/cover1.jpg" alt="First slide">
			<div class="container">
				<div class="carousel-caption">
					<h1>Welcome to the ITESCA Store!</h1>
					<p>Find the perfect gift for you.</p>

				</div>
			</div>
		</div>
		<div class="item">
			<img class="second-slide base-image"
				src="resources/site-images/cover-4.jpg" alt="Second slide">
			<div class="container">
				<div class="carousel-caption">
					<h1>Express your personality!</h1>
					<p>Express your style by wearing the perfect ensemble.</p>
				</div>
			</div>
		</div>
		<div class="item">
			<img class="third-slide base-image"
				src="resources/site-images/cover-8.jpg" alt="Third slide">
			<div class="container">
				<div class="carousel-caption">
					<h1>Let the music be with you!</h1>
					<p>At all times find the perfect song for you.</p>
				</div>
			</div>
		</div>
	</div>
	<a class="left carousel-control" href="#myCarousel" role="button"
		data-slide="prev"> <span class="glyphicon glyphicon-chevron-left"
		aria-hidden="true"></span> <span class="sr-only">Previous</span>
	</a> <a class="right carousel-control" href="#myCarousel" role="button"
		data-slide="next"> <span class="glyphicon glyphicon-chevron-right"
		aria-hidden="true"></span> <span class="sr-only">Next</span>
	</a>
</div>
<!-- /.carousel -->


<!-- Marketing messaging and featurettes
================================================== -->
<!-- Wrap the rest of the page in another container to center all the content. -->

<div class="container marketing">

	<!-- Three columns of text below the carousel -->
	<div class="row">
		<div class="col-lg-4">
			<img class="img-circle" src="resources/site-images/circle-1.jpg"
				width="140" height="140">
			<h2>HD Cam Sumergible</h2>
			<p>Do not miss any special moment, capture them in HD with our products.</p>
			<p>
				<a class="btn btn-default" href="products" role="button">more
					&raquo;</a>
			</p>
		</div>
		<!-- /.col-lg-4 -->
		<div class="col-lg-4">
			<img class="img-circle" src="resources/site-images/circle-3.jpg"
				width="140" height="140">
			<h2>Shelter for Lady</h2>
			<p>Make the weather your best ally, wear your style with this seasonal coat.</p>
			<p>
				<a class="btn btn-default" href="products" role="button">more
					&raquo;</a>
			</p>
		</div>
		<!-- /.col-lg-4 -->
		<div class="col-lg-4">
			<img class="img-circle" src="resources/site-images/circle-2.jpg"
				width="140" height="140">
			<h2>Desktop Gamer</h2>
			<p>Enjoy your games in HD with this desktop computer with graphics dedicated to give you the best gaming experience.</p>
			<p>
				<a class="btn btn-default" href="products" role="button">more
					&raquo;</a>
			</p>
		</div>
		<!-- /.col-lg-4 -->
	</div>
	<!-- /.row -->
</div>
<br>
<br>
<!-- /.container -->

<%@include file="/WEB-INF/views/templates/footer.jsp"%>