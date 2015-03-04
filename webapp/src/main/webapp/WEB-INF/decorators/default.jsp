<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<!DOCTYPE html>
<html>
<head>
    <title><decorator:title default="Conference Engineer"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Funky Android Ltd.">
    <meta name="theme-color" content="#2980B9">
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/style.css'/>" >
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <decorator:head />
</head>
<body>
<div id="wrap">
    <div class="navbar navbar-default navbar-fixed-top">
        <div class="container">
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
    </div>
    <div class="container">
        <decorator:body/>
    </div>
</div>
<div id="footer">
    <div class="container">
        <p class="text-center">Website and back-end software : &copy;Copyright 2013-2015 <a target="_blank" href="http://funkyandroid.com/">Funky Android Ltd.</a>, All rights reserved.</p>
    </div>
</div>
</body>
</html>
