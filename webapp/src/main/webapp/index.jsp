<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html lang="en">
<head>
    <title>Conference Engineer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Funky Android Ltd.">
    <meta name="theme-color" content="#2980B9">
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/style.css'/>" >
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <link href="css/landing-page.css" rel="stylesheet">
    <style>.footer { border-top: 1px solid #eee } </style>
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top topnav">
    <div class="container topnav">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value='/' />"><div class="logostyle">CE</div></a>
        </div>
        <div class="navbar-collapse collapse">
            <form class="navbar-form navbar-right" name="logindetails" action="<c:url value='/Login' />" method="POST"
                  accept-charset="UTF-8" role="form">
                <div class="form-group">
                    <input type="text" id="username" name="username" class="form-control" placeholder="Username"/>
                </div>
                <div class="form-group">
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password"/>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Login</button>
                </div>
            </form>
        </div>
    </div>
</nav>

<div class="modal fade" id="loginBox" tabindex="-1" role="dialog" aria-labelledby="loginBox" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Sign In</h4>
            </div>
            <form class="form-signin" name="logindetails" action="<c:url value='/Login' />" method="POST"
                  accept-charset="UTF-8">
                <div class="modal-body">
                    <input type="text" id="username" name="username" class="form-control" placeholder="Username"/>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password"/>
                    <div align="right">
                        <a href="<c:url value='/Forgotten' />" class="btn btn-link btn-sm">Forgotten Password</a>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Login</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="intro-header">
    <c:if test="${not empty sessionScope.error}">
        <div class="row"><div class="alert alert-danger text-center">${sessionScope.error}</div></div>
        <c:set scope="session" var="error" value="" />
    </c:if>

    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="intro-message">
                    <h1>Conference Engineer</h1>
                    <h3>Helping to make conferences easier to plan and run.</h3>
                    <ul class="list-inline">
                        <li><a data-toggle="modal" href="#loginBox" class="btn btn-default btn-lg" role="button">Sign-in</a></li>
                        <li><a href="<c:url value='/Register' />" class="btn btn-primary btn-lg" role="button">Sign-up</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="content-section-a">
    <div class="container">
        <div class="row">
            <div class="col-lg-5 col-sm-6">
                <hr class="section-heading-spacer">
                <div class="clearfix"></div>
                <h2 class="section-heading">About</h2>
                <p class="lead">Conference Engineer is designed to help conference organisers; It allows you to plan conferences
                    by arranging the tracks, speakers, and sessions of your conference into the scheulde you want, and
                    allows you to download that information in a format suitable for use by attendees using an open-source
                    <a href="http://github.com/funkyandroid/iosched/">modified version of Googles IOsched application</a>.</p>
                <p class="lead">Conference Engineer is currently free to use, and we welcome any feedback.</p>
            </div>
            <div class="col-lg-5 col-lg-offset-2 col-sm-6 text-right">
                <hr class="section-heading-spacer" style="visibility: hidden">
                <div class="clearfix"></div>
                <h2 class="section-heading">Latest News</h2>
                <p class="lead"><i>7th January 2015</i><br/>We've merged the latest changes to Googles I/O app into <a href="https://github.com/funkyandroid/iosched/" target="_blank">our GitHub repository</a>.</p>
                <p class="lead"><i>6th November 2014</i><br/>We've added the ability to download your conference schedule as a single HTML page. See the &quot;App Static Data&quot; panel in your dashboard for the link.</p>
            </div>
        </div>
    </div>
</div>


<div class="content-section-b">
    <div class="container">
        <div class="row">
        <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <p class="lead">Conference Engineer was created to initially serve one conference; Droidcon London 2013. After
                the <a href="https://play.google.com/store/apps/details?id=com.funkyandroid.droidcon.uk.iosched" target="_blank">
                    Droidcon London 2013 app</a> was complete we starting working on implementing the ideas we'd
                received from conference organisers.</p>
            <p class="lead">We've recently completed our modifications to the 2014 version of Googles I/O app
                to create an app for <a href="https://play.google.com/store/apps/details?id=com.conferenceengineer.android.iosched.event1002" target="_blank">
                    Droidcon London 2014</a>, and we'll be using it as the basis for apps for other conferences .</p>
            <p class="lead">If you'd like to see an example of the type of apps which can be powered by Conference Engineer
                you can download any of the following from Googles' Play Store;
                <a href="https://play.google.com/store/apps/details?id=com.funkyandroid.droidcon.uk.iosched" target="_blank">
                    Droidcon London 2013</a>,
                <a href="https://play.google.com/store/apps/details?id=com.conferenceengineer.android.iosched.conference348" target="_blank">
                    droidconNL 2013</a>,
                <a href="https://play.google.com/store/apps/details?id=com.conferenceengineer.android.iosched.conference348" target="_blank">
                    ADD 2014</a>, or
                <a href="https://play.google.com/store/apps/details?id=com.conferenceengineer.android.iosched.event1002" target="_blank">
                    Droidcon London 2014</a>, or search for one of the third party created apps such as the one for
                <a href="https://play.google.com/store/apps/details?id=com.conferenceengineer.android.iosched.devfestnl14" target="_blank">
                    DevFestNL</a>.
            </p>
        </div>
        <div class="col-lg-5 col-sm-pull-6  col-sm-6">
            <img class="img-responsive" src="logo.png" alt="Logo"/>
        </div>
    </div>
</div>

<div id="footer">
    <div class="container">
        <hr />
        <p class="text-center">Website and back-end software : &copy;Copyright 2013-2015 <a target="_blank" href="http://funkyandroid.com/">Funky Android Ltd.</a>, All rights reserved.</p>
    </div>
</div>

</body>
</html>
